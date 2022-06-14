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
import uk.ac.ebi.ena.annotation.helper.mapper.SAHResponseMapper;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.repository.InstituteRepository;
import uk.ac.ebi.ena.annotation.helper.service.SAHService;
import uk.ac.ebi.ena.annotation.helper.service.helper.SAHCollectionServiceHelper;
import uk.ac.ebi.ena.annotation.helper.service.helper.SAHInstituteServiceHelper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.*;

@Service
@Slf4j
public class SAHServiceImpl implements SAHService {

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private SAHInstituteServiceHelper sahInstituteServiceHelper;

    @Autowired
    private SAHCollectionServiceHelper sahCollectionServiceHelper;

    @Autowired
    InstituteMapper instituteMapper;

    @Autowired
    CollectionMapper collectionMapper;

    @Autowired
    SAHResponseMapper sahResponseMapper;

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
    public ResponseDto findByInstUniqueNameAndCollCode(String instUniqueName, String collCode, String[] qualifierType) {
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
            List<String> listQT = Arrays.asList(qualifierType);
            listCollection =
                    collectionRepository.findByInstIdAndCollNameFuzzyWithQualifierType(optionalInstitute.get().getInstId(),
                            collCode, listQT);
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
    public SAHResponseDto validate(String qualifierValue, String[] qualifierType) {

        log.debug("Validating the qualifier value -- " + qualifierValue);

        String[] tokenizedQV = qualifierValue.split(":");

        if (tokenizedQV.length < 2 || tokenizedQV.length > 3) {
            log.info("Invalid qualifier format -- {} ", qualifierValue);
            return SAHResponseDto.builder().success(false).error(ErrorResponse.builder().
                    code(InvalidFormatProvidedError).message(InvalidFormatProvidedMessage).build()).build();
        }

        if (tokenizedQV.length == 2) {
            return validateAndConstruct(tokenizedQV[0], null, tokenizedQV[1], qualifierType);
        } else {
            return validateAndConstruct(tokenizedQV[0], tokenizedQV[1], tokenizedQV[2], qualifierType);
        }
    }

    @Override
    public SAHResponseDto construct(String instUniqueName, String collCode, String identifier, String[] qualifierType) {
        if (isEmpty(instUniqueName)) {
            log.info("Missing institute unique name");
            return SAHResponseDto.builder().success(false).error(ErrorResponse.builder().
                    code(InstituteMissingError).message(InstituteMissingMessage).build()).build();
        }

        if (isEmpty(identifier)) {
            log.info("Missing identifier");
            return SAHResponseDto.builder().success(false).error(ErrorResponse.builder().
                    code(IdentifierMissingError).message(IdentifierMissingMessage).build()).build();
        }

        return validateAndConstruct(instUniqueName, collCode, identifier, qualifierType);
    }

    private SAHResponseDto validateAndConstruct(String instUniqueName, String collCode, String identifier, String[] qualifierType) {
        //[<Institution Unique Name>:]<identifier>
        ValidationSearchResult validationSearchResult = sahInstituteServiceHelper.validateInstitute(instUniqueName, qualifierType);
        validationSearchResult.setIdentifier(identifier);
        //set the input string for logs and reporting purpose
        validationSearchResult.setInputsParams(instUniqueName, collCode, identifier);

        //invalid institute scenario
        if (!validationSearchResult.isSuccess()) {
            return sahResponseMapper.mapResponseDto(validationSearchResult);
        }

        if (isEmpty(collCode)) {
            //collection code not provided. return with results
            return sahResponseMapper.mapResponseDto(validationSearchResult);
        }

        //[<Institution Unique Name>:[<collection-code>:]]<identifier>
        //also within suggestions limits
        validationSearchResult.setCollectionAvailable(true);
        //Map will further help in contructing the final QV strings
        Map<Integer, InstituteDto> mapInstIdUniqueName = new HashMap();
        for (Institute institute : validationSearchResult.getInstitutes()) {
            mapInstIdUniqueName.put(institute.getInstId(), instituteMapper.toDto(institute));
        }
        validationSearchResult.setInstituteIdNameMap(mapInstIdUniqueName);

        validationSearchResult = sahCollectionServiceHelper
                .validateMultipleInstIdsAndCollName(validationSearchResult, collCode, qualifierType);
        return sahResponseMapper.mapResponseDto(validationSearchResult);
    }


}