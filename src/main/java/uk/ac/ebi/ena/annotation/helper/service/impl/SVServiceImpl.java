package uk.ac.ebi.ena.annotation.helper.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.*;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.exception.ErrorResponse;
import uk.ac.ebi.ena.annotation.helper.exception.RecordNotFoundException;
import uk.ac.ebi.ena.annotation.helper.mapper.SVResponseMapper;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;
import uk.ac.ebi.ena.annotation.helper.service.SVService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SVErrorCode.*;

@Service
@Slf4j
public class SVServiceImpl implements SVService {

//    @Value("${ena.annotation.helper.suggestions.limit}")
//    private int SUGGESTIONS_LIMIT;

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private SVInstituteServiceHelper svInstituteServiceHelper;

    @Autowired
    private SVCollectionServiceHelper svCollectionServiceHelper;

    @Autowired
    SVResponseMapper svResponseMapper;


    @Override
    public SVResponseDto validateSV(String specimenVoucher) {

        log.debug("Validating the specimen voucher value -- " + specimenVoucher);

        String[] tokenizedSV = specimenVoucher.split(":");

        if (tokenizedSV.length < 2 || tokenizedSV.length > 3) {
            //todo improve - error reporting
            return SVResponseDto.builder().success(false).error(ErrorResponse.builder().
                    code(InvalidFormatProvidedError).message(InvalidFormatProvidedMessage).build()).build();
        }

        //[<Institution Unique Name>:]<specimen_id>
        SVSearchResult svSearchResult = svInstituteServiceHelper.validateInstitute(tokenizedSV[0]);

        //invalid institute scenario
        if (!svSearchResult.isSuccess()) {
            return svResponseMapper.mapResponseDto(svSearchResult);
        }

        //set specimen id for further processing
        if (tokenizedSV.length == 2) {
            //collection code not provided.
            svSearchResult.setSpecimenId(tokenizedSV[1]);
            return svResponseMapper.mapResponseDto(svSearchResult);
        } else {
            svSearchResult.setSpecimenId(tokenizedSV[2]);
        }

        //[<Institution Unique Name>:[<collection-code>:]]<specimen_id>
        //also within suggestions limits
        svSearchResult.setCollectionAvailable(true);
        //Map will further help in contructing the final SV strings
        Map mapInstIdUniqueName = svSearchResult.getInstitutes().stream()
                .collect(Collectors.toMap(Institute::getInstId, Institute::getUniqueName));
        svSearchResult.setInstituteIdNameMap(mapInstIdUniqueName);

        svSearchResult = svCollectionServiceHelper
                .validateMultipleInstIdsAndCollName(svSearchResult, tokenizedSV[1]);
        return svResponseMapper.mapResponseDto(svSearchResult);


//        if (svSearchResult.isSuccess()) {
//            int responseSize = svSearchResult.getInstitutes().size();
//            if (responseSize == 0 && tokenizedSV.length == 2) {
//                //no match scenario
//                return svResponseMapper.mapResponseDto(svSearchResult);
//            } else if (responseSize == 1 && tokenizedSV.length == 2) {
//                //since collection code is not available, build the response object and return
//                return svResponseMapper.mapResponseDto(svSearchResult);
//            } else if (responseSize > 1 && tokenizedSV.length == 2 && responseSize <= SUGGESTIONS_LIMIT) {
//                //generate multiple valid string and return
//                return svResponseMapper.mapResponseDto(svSearchResult);
//            } else if (responseSize > SUGGESTIONS_LIMIT) {
//                //set and return too many hits.. please verify / be more accurate -- the institute unique name entered..
//                return svResponseMapper.mapResponseDto(svSearchResult);
//            }
//        }

    }

    @Override
    public SVResponseDto constructSV(String instUniqueName, String collCode, String specimenId) {

        if (isEmpty(instUniqueName)) {
            return SVResponseDto.builder().success(false).error(ErrorResponse.builder().
                    code(InstituteMissingError).message(InstituteMissingMessage).build()).build();
        }

        if (isEmpty(specimenId)) {
            return SVResponseDto.builder().success(false).error(ErrorResponse.builder().
                    code(SpecimenIdMissingError).message(SpecimenIdMissingMessage).build()).build();
        }

        //[<Institution Unique Name>:]<specimen_id>
        SVSearchResult svSearchResult = svInstituteServiceHelper.validateInstitute(instUniqueName);
        svSearchResult.setSpecimenId(specimenId);

        //invalid institute scenario
        if (!svSearchResult.isSuccess()) {
            return svResponseMapper.mapResponseDto(svSearchResult);
        }

        if (isEmpty(collCode)) {
            //collection code not provided. return with results
            return svResponseMapper.mapResponseDto(svSearchResult);
        }

        //[<Institution Unique Name>:[<collection-code>:]]<specimen_id>
        //also within suggestions limits
        svSearchResult.setCollectionAvailable(true);
        //Map will further help in contructing the final SV strings
        Map mapInstIdUniqueName = svSearchResult.getInstitutes().stream()
                .collect(Collectors.toMap(Institute::getInstId, Institute::getUniqueName));
        svSearchResult.setInstituteIdNameMap(mapInstIdUniqueName);

        svSearchResult = svCollectionServiceHelper
                .validateMultipleInstIdsAndCollName(svSearchResult, collCode);
        return svResponseMapper.mapResponseDto(svSearchResult);
    }


    @Override
    public ResponseDto findByInstName(String instName) {
        List<Institute> instituteList = instituteRepository.findByInstName(instName);
        if (!instituteList.isEmpty()) {
            return new InstituteResponseDto(instituteList, true, LocalDateTime.now());
        }
        ErrorResponse error = ErrorResponse.builder().message(RecordNotFoundMessage).code(RecordNotFoundError).build();
        return new ResponseDto(false, LocalDateTime.now(), error);
    }

    @Override
    public ResponseDto findByInstituteStringFuzzy(String name) {
        List<Institute> instituteList = instituteRepository.findByInstituteStringFuzzy(name);
        if (!instituteList.isEmpty()) {
            return new InstituteResponseDto(instituteList, true, LocalDateTime.now());
        }
        ErrorResponse error = ErrorResponse.builder().message(RecordNotFoundMessage).code(RecordNotFoundError).build();
        return new ResponseDto(false, LocalDateTime.now(), error);
    }

    @Override
    public ResponseDto findByInstCode(String instCode) {
        Optional<Institute> optionalInstitute = instituteRepository.findByInstCode(instCode);
        if (optionalInstitute.isPresent()) {
            return new InstituteResponseDto(Collections.singletonList(optionalInstitute.get()),
                    true, LocalDateTime.now());
        } else {
            ErrorResponse error = ErrorResponse.builder().message(RecordNotFoundMessage).code(RecordNotFoundError).build();
            return new ResponseDto(false, LocalDateTime.now(), error);
        }
    }

    @Override
    public ResponseDto findByUniqueName(String uniqueName) throws RecordNotFoundException {
        Optional<Institute> optionalInstitute = instituteRepository.findByUniqueName(uniqueName);
        if (optionalInstitute.isPresent()) {
            return new InstituteResponseDto(Collections.singletonList(optionalInstitute.get()),
                    true, LocalDateTime.now());
        } else {
            ErrorResponse error = ErrorResponse.builder().message(RecordNotFoundMessage).code(RecordNotFoundError).build();
            return new ResponseDto(false, LocalDateTime.now(), error);
        }
    }

    @Override
    public ResponseDto findByInstUniqueNameAndCollCode(String instUniqueName, String collCode) {
        Optional<Institute> optionalInstitute = instituteRepository.findByUniqueName(instUniqueName);
        if (optionalInstitute.isPresent()) {
            Optional<Collection> optionalCollection =
                    collectionRepository.findByInstIdAndCollCode(optionalInstitute.get().getInstId(), collCode);
            if (optionalCollection.isPresent()) {
                return new CollectionResponseDto(Collections.singletonList(optionalCollection.get()),
                        true, LocalDateTime.now());
            }
        }
        ErrorResponse error = ErrorResponse.builder().message(RecordNotFoundMessage).code(RecordNotFoundError).build();
        return new ResponseDto(false, LocalDateTime.now(), error);

    }

    @Override
    public ResponseDto findByInstIdAndCollCode(int instId, String collCode) {
        Optional<Collection> optionalCollection = collectionRepository.findByInstIdAndCollCode(instId, collCode);
        if (optionalCollection.isPresent()) {
            return new CollectionResponseDto(Collections.singletonList(optionalCollection.get()),
                    true, LocalDateTime.now());
        } else {
            ErrorResponse error = ErrorResponse.builder().message(RecordNotFoundMessage).code(RecordNotFoundError).build();
            return new ResponseDto(false, LocalDateTime.now(), error);
        }
    }

    @Override
    public ResponseDto findByCollCode(String collCode) throws RecordNotFoundException {
        List<Collection> listCollection = collectionRepository.findByCollCode(collCode);
        if (!listCollection.isEmpty()) {
            return new CollectionResponseDto(listCollection, true, LocalDateTime.now());
        }
        ErrorResponse error = ErrorResponse.builder().message(RecordNotFoundMessage).code(RecordNotFoundError).build();
        return new ResponseDto(false, LocalDateTime.now(), error);
    }

    @Override
    public ResponseDto findByCollNameFuzzy(String name) {
        List<Collection> listCollection = collectionRepository.findByCollNameFuzzy(name);
        if (!listCollection.isEmpty()) {
            return new CollectionResponseDto(listCollection, true, LocalDateTime.now());
        }
        ErrorResponse error = ErrorResponse.builder().message(RecordNotFoundMessage).code(RecordNotFoundError).build();
        return new ResponseDto(false, LocalDateTime.now(), error);
    }
}