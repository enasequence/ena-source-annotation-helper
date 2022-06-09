package uk.ac.ebi.ena.annotation.helper.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.*;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.exception.ErrorResponse;
import uk.ac.ebi.ena.annotation.helper.mapper.CollectionMapper;
import uk.ac.ebi.ena.annotation.helper.mapper.InstituteMapper;
import uk.ac.ebi.ena.annotation.helper.mapper.SVResponseMapper;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;
import uk.ac.ebi.ena.annotation.helper.service.SVService;
import uk.ac.ebi.ena.annotation.helper.service.helper.SVCollectionServiceHelper;
import uk.ac.ebi.ena.annotation.helper.service.helper.SVInstituteServiceHelper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SVErrorCode.*;

@Service
@Slf4j
public class SVServiceImpl implements SVService {

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private SVInstituteServiceHelper svInstituteServiceHelper;

    @Autowired
    private SVCollectionServiceHelper svCollectionServiceHelper;

    @Autowired
    InstituteMapper instituteMapper;

    @Autowired
    CollectionMapper collectionMapper;

    @Autowired
    SVResponseMapper svResponseMapper;

    @Value("${query.results.limit}")
    private int QUERY_RESULTS_LIMIT;

    @Override
    public ResponseDto findByInstituteStringFuzzyWithQTArray(String name, String[] qualifierType) {
        List<Institute> instituteList;
        if (isEmpty(qualifierType)) {
            instituteList = instituteRepository.findByInstituteFuzzy(name);
        } else {
            List<String> listQT = Arrays.asList(qualifierType);
            instituteList = instituteRepository
                    .findByInstituteFuzzyAndQualifierTypeArray(name, listQT, QUERY_RESULTS_LIMIT);
        }
        if (!instituteList.isEmpty()) {
            return new InstituteResponseDto(instituteList.stream().map(instituteMapper::toDto).collect(Collectors.toList()),
                    true, LocalDateTime.now());
        }
        ErrorResponse error = ErrorResponse.builder().message(RecordNotFoundMessage).code(RecordNotFoundError).build();
        return new ResponseDto(false, LocalDateTime.now(), Collections.singletonList(error));
    }

    @Override
    public ResponseDto findCollectionsByInstUniqueName(String instUniqueName, String[] qualifierType) {

        Optional<Institute> optionalInstitute = instituteRepository.findByUniqueName(instUniqueName);

        if (!optionalInstitute.isPresent()) {
            log.info("No matching institute found for institute -- {}", instUniqueName);
            ErrorResponse error = ErrorResponse.builder().message(NoMatchingInstituteMessage).code(NoMatchingInstituteError).build();
            return new ResponseDto(false, LocalDateTime.now(), Collections.singletonList(error));
        }

        InstituteDto instituteDto = instituteMapper.toDto(optionalInstitute.get());
        List<Collection> listCollection;
        if (isEmpty(qualifierType)) {
            listCollection = collectionRepository
                    .findByInstId(optionalInstitute.get().getInstId());
        } else {
            List<String> listQT = Arrays.asList(qualifierType);
            listCollection = collectionRepository
                    .findByInstIdAndQualifierTypeArray(optionalInstitute.get().getInstId(), listQT);
        }

        if (!listCollection.isEmpty()) {
            instituteDto.setCollections(listCollection.stream().map(collectionMapper::toDto).collect(Collectors.toList()));
            return new InstituteResponseDto(Collections.singletonList(instituteDto),
                    true, LocalDateTime.now());
        }

        log.info("No matching collection found for institute -- {}", instUniqueName);
        ErrorResponse error = ErrorResponse.builder().message(NoMatchingCollectionMessage).code(NoMatchingCollectionError).build();
        return new ResponseDto(false, LocalDateTime.now(), Collections.singletonList(error));

    }

    @Override
    public ResponseDto findByInstUniqueNameAndCollCode(String instUniqueName, String collCode, String qualifierType) {
        Optional<Institute> optionalInstitute = instituteRepository.findByUniqueName(instUniqueName);
        if (!optionalInstitute.isPresent()) {
            log.info("No matching institute found for institute -- {}", instUniqueName);
            ErrorResponse error = ErrorResponse.builder().message(NoMatchingInstituteMessage).code(NoMatchingInstituteError).build();
            return new ResponseDto(false, LocalDateTime.now(), Collections.singletonList(error));
        }

        InstituteDto instituteDto = instituteMapper.toDto(optionalInstitute.get());
        List<Collection> listCollection;
        if (isEmpty(qualifierType)) {
            listCollection =
                    collectionRepository.findByInstIdAndCollNameFuzzy(optionalInstitute.get().getInstId(), collCode);
        } else {
            listCollection =
                    collectionRepository.findByInstIdAndCollNameFuzzyWithQualifierType(optionalInstitute.get().getInstId(),
                            collCode, qualifierType);
        }

        if (!listCollection.isEmpty()) {
            instituteDto.setCollections(listCollection.stream().map(collectionMapper::toDto).collect(Collectors.toList()));
            return new InstituteResponseDto(Collections.singletonList(instituteDto),
                    true, LocalDateTime.now());
        }

        log.info("No matching collection found for institute -- {}", instUniqueName);
        ErrorResponse error = ErrorResponse.builder().message(NoMatchingCollectionMessage).code(NoMatchingCollectionError).build();
        return new ResponseDto(false, LocalDateTime.now(), Collections.singletonList(error));

    }


    @Override
    public SAHResponseDto validateSV(String specimenVoucher, String qualifierType) {

        log.debug("Validating the specimen voucher value -- " + specimenVoucher);

        String[] tokenizedSV = specimenVoucher.split(":");

        if (tokenizedSV.length < 2 || tokenizedSV.length > 3) {
            log.info("Invalid specimen voucher format -- {} ", specimenVoucher);
            return SAHResponseDto.builder().success(false).error(ErrorResponse.builder().
                    code(InvalidFormatProvidedError).message(InvalidFormatProvidedMessage).build()).build();
        }

        if (tokenizedSV.length == 2) {
            return validateAndConstruct(tokenizedSV[0], null, tokenizedSV[1], qualifierType);
        } else {
            return validateAndConstruct(tokenizedSV[0], tokenizedSV[1], tokenizedSV[2], qualifierType);
        }
    }

    @Override
    public SAHResponseDto constructSV(String instUniqueName, String collCode, String specimenId, String qualifierType) {
        if (isEmpty(instUniqueName)) {
            log.info("Missing institute unique name");
            return SAHResponseDto.builder().success(false).error(ErrorResponse.builder().
                    code(InstituteMissingError).message(InstituteMissingMessage).build()).build();
        }

        if (isEmpty(specimenId)) {
            log.info("Missing specimen id");
            return SAHResponseDto.builder().success(false).error(ErrorResponse.builder().
                    code(SpecimenIdMissingError).message(SpecimenIdMissingMessage).build()).build();
        }

        return validateAndConstruct(instUniqueName, collCode, specimenId, qualifierType);
    }

    private SAHResponseDto validateAndConstruct(String instUniqueName, String collCode, String specimenId, String qualifierType) {
        //[<Institution Unique Name>:]<specimen_id>
        ValidationSearchResult validationSearchResult = svInstituteServiceHelper.validateInstitute(instUniqueName, qualifierType);
        validationSearchResult.setSpecimenId(specimenId);
        //set the input string for logs and reporting purpose
        validationSearchResult.setInputsParams(instUniqueName, collCode, specimenId);

        //invalid institute scenario
        if (!validationSearchResult.isSuccess()) {
            return svResponseMapper.mapResponseDto(validationSearchResult);
        }

        if (isEmpty(collCode)) {
            //collection code not provided. return with results
            return svResponseMapper.mapResponseDto(validationSearchResult);
        }

        //[<Institution Unique Name>:[<collection-code>:]]<specimen_id>
        //also within suggestions limits
        validationSearchResult.setCollectionAvailable(true);
        //Map will further help in contructing the final SV strings
        Map mapInstIdUniqueName = validationSearchResult.getInstitutes().stream()
                .collect(Collectors.toMap(Institute::getInstId, Institute::getUniqueName));
        validationSearchResult.setInstituteIdNameMap(mapInstIdUniqueName);

        validationSearchResult = svCollectionServiceHelper
                .validateMultipleInstIdsAndCollName(validationSearchResult, collCode, qualifierType);
        return svResponseMapper.mapResponseDto(validationSearchResult);
    }


}