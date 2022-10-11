/*
 * ******************************************************************************
 *  * Copyright 2021 EMBL-EBI, Hinxton outstation
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package uk.ac.ebi.ena.annotation.helper.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ena.annotation.helper.dto.*;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.entity.Institution;
import uk.ac.ebi.ena.annotation.helper.exception.BadRequestException;
import uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode;
import uk.ac.ebi.ena.annotation.helper.mapper.CollectionMapper;
import uk.ac.ebi.ena.annotation.helper.mapper.InstituteMapper;
import uk.ac.ebi.ena.annotation.helper.mapper.SAHResponseMapper;
import uk.ac.ebi.ena.annotation.helper.repository.CollectionRepository;
import uk.ac.ebi.ena.annotation.helper.repository.InstitutionRepository;
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
    private InstitutionRepository institutionRepository;

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
        List<Institution> institutionList;

        //lookout for exact match first
        Optional<Institution> optionalInstitute;
        if (isEmpty(qualifierType)) {
            optionalInstitute  = institutionRepository.findByUniqueNameExact(name);
        } else {
            List<String> listQT = Arrays.asList(qualifierType);
            optionalInstitute = institutionRepository
                    .findByUniqueNameAndQualifierTypeArray(name, listQT);
        }

        //TODO discuss and later can be optimised for implementation
        if (!optionalInstitute.isPresent()) {
            if (isEmpty(qualifierType)) {
                optionalInstitute  = institutionRepository.findByUniqueNameExact(name.toUpperCase(Locale.ROOT));
            } else {
                List<String> listQT = Arrays.asList(qualifierType);
                optionalInstitute = institutionRepository
                        .findByUniqueNameAndQualifierTypeArray(name, listQT);
            }
        }

        if (optionalInstitute.isPresent()) {
            return new InstituteResponseDto(Collections.singletonList(instituteMapper.toDto(optionalInstitute.get())),
                    true, LocalDateTime.now());
        }

        //perform a fuzzy search next
        if (isEmpty(qualifierType)) {
            institutionList = institutionRepository.findByInstituteFuzzy(name, PageRequest.of(0, QUERY_RESULTS_LIMIT,
                    Sort.by("_score").descending())
            );
        } else {
            List<String> listQT = Arrays.asList(qualifierType);
            institutionList = institutionRepository
                    .findByInstituteFuzzyAndQualifierTypeArray(name, listQT,
                            PageRequest.of(0, QUERY_RESULTS_LIMIT,
                                    Sort.by("_score").descending())
                    );
        }
        if (!institutionList.isEmpty()) {
            return new InstituteResponseDto(institutionList.stream().map(instituteMapper::toDto).collect(Collectors.toList()),
                    true, LocalDateTime.now());
        }
        //no record found scenario
        return new InstituteResponseDto(institutionList.stream().map(instituteMapper::toDto).collect(Collectors.toList()),
                false, LocalDateTime.now());
    }

    @Override
    public ResponseDto findCollectionsByInstUniqueName(String instUniqueName, String[] qualifierType) {

        Optional<Institution> optionalInstitute = institutionRepository.findByUniqueNameExact(instUniqueName);

        if (!optionalInstitute.isPresent()) {
            //no record found scenario
            log.info("No matching institute found for institute -- {}", instUniqueName);
            return new InstituteResponseDto(new ArrayList<InstitutionDto>(),
                    false, LocalDateTime.now());
        }

        InstitutionDto institutionDto = instituteMapper.toDto(optionalInstitute.get());
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
            institutionDto.setCollections(listCollection.stream().map(collectionMapper::toDto).collect(Collectors.toList()));
            return new InstituteResponseDto(Collections.singletonList(institutionDto),
                    true, LocalDateTime.now());
        }

        //no record found scenario
        log.info("No matching collection found for institute -- {}", instUniqueName);
        institutionDto.setCollections(new ArrayList<CollectionDto>());
        return new InstituteResponseDto(Collections.singletonList(institutionDto),
                false, LocalDateTime.now());
    }

    @Override
    public ResponseDto findByInstUniqueNameAndCollCode(String instUniqueName, String collCode, String[] qualifierType) {
        Optional<Institution> optionalInstitute = institutionRepository.findByUniqueNameExact(instUniqueName);
        if (!optionalInstitute.isPresent()) {
            //no record found scenario
            log.info("No matching institute found for institute -- {}", instUniqueName);
            return new InstituteResponseDto(new ArrayList<InstitutionDto>(),
                    false, LocalDateTime.now());
        }

        InstitutionDto institutionDto = instituteMapper.toDto(optionalInstitute.get());
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
            institutionDto.setCollections(listCollection.stream().map(collectionMapper::toDto).collect(Collectors.toList()));
            return new InstituteResponseDto(Collections.singletonList(institutionDto),
                    true, LocalDateTime.now());
        }

        //no record found scenario
        log.info("No matching collection found for institute -- {}", instUniqueName);
        institutionDto.setCollections(new ArrayList<CollectionDto>());
        return new InstituteResponseDto(Collections.singletonList(institutionDto),
                false, LocalDateTime.now());

    }


    @Override
    public SAHResponseDto validate(String qualifierValue, String[] qualifierType) {

        log.debug("Validating the attribute value -- " + qualifierValue);

        String[] tokenizedQV = qualifierValue.split(":");

        if (tokenizedQV.length < 2 || tokenizedQV.length > 3) {
            log.info("Invalid attribute format -- {} ", qualifierValue);
            throw new BadRequestException(InvalidFormatProvidedError, InvalidFormatProvidedMessage);
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
            throw new BadRequestException(InstituteMissingError, InstituteMissingMessage);
        }

        if (isEmpty(identifier)) {
            log.info("Missing identifier");
            throw new BadRequestException(IdentifierMissingError, IdentifierMissingMessage);
        }

        return validateAndConstruct(instUniqueName, collCode, identifier, qualifierType);
    }

    @Override
    public ResponseDto getErrorCodes() {
        return new ResponseDto(true, LocalDateTime.now(), SAHErrorCode.errorCodesMap);
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
        Map<Integer, InstitutionDto> mapInstIdUniqueName = new HashMap();
        for (Institution institution : validationSearchResult.getInstitutions()) {
            mapInstIdUniqueName.put(institution.getInstId(), instituteMapper.toDto(institution));
        }
        validationSearchResult.setInstituteIdNameMap(mapInstIdUniqueName);

        validationSearchResult = sahCollectionServiceHelper
                .validateMultipleInstIdsAndCollName(validationSearchResult, collCode, qualifierType);
        return sahResponseMapper.mapResponseDto(validationSearchResult);
    }


}