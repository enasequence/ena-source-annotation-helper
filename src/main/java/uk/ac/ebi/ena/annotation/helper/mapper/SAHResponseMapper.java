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

package uk.ac.ebi.ena.annotation.helper.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ena.annotation.helper.dto.InstitutionDto;
import uk.ac.ebi.ena.annotation.helper.dto.MatchDto;
import uk.ac.ebi.ena.annotation.helper.dto.SAHResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.ValidationSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.entity.Institution;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.utils.SAHConstants.*;

@Component
@Slf4j
public class SAHResponseMapper {

    @Autowired
    CollectionMapper collectionMapper;

    @Autowired
    InstituteMapper instituteMapper;

    /**
     * mapResponseDto - Mapper to Map ValidationSearchResult with ResponseDto.
     *
     * @param validationSearchResult
     * @return
     */
    public SAHResponseDto mapResponseDto(ValidationSearchResult validationSearchResult) {

        log.debug("Processing Search Result with {}", validationSearchResult.getMatch());
        //build and return exact match response
        if (validationSearchResult.getMatch() == EXACT_MATCH || validationSearchResult.getMatch() == MULTI_NEAR_MATCH) {
            return buildSuccessMatchResponse(validationSearchResult);
        }

        //build and return exact match response
        if (validationSearchResult.getMatch() == TOO_MANY_MATCH) {
            return buildTooManyMatchErrorResponse(validationSearchResult);
        }

        // NO_MATCH condition
        if (validationSearchResult.getMatch() == NO_MATCH) {
            return buildMatchErrorResponse(validationSearchResult);
        }

        //no match again -- should not be reached
        return buildMatchErrorResponse(validationSearchResult);
    }

    /**
     * buildSuccessMatchResponse.
     *
     * @param validationSearchResult
     * @return
     */
    private SAHResponseDto buildSuccessMatchResponse(ValidationSearchResult validationSearchResult) {
        //mean only single entry in response array
        log.debug("Building Success Match Response");
        List<MatchDto> matchDtoList = new ArrayList();
        log.info("Building success response for the given input -- {}", validationSearchResult.getInputParams());
        if (validationSearchResult.isCollectionAvailable()) {
            log.debug("Collection Available");
            for (Collection collection : validationSearchResult.getCollections()) {
                InstitutionDto institutionDto = validationSearchResult.getInstituteIdNameMap().get(collection.getInstId());
                String qualifierValueStr = buildQualifierValueString(institutionDto.getUniqueName(), collection.getCollCode(),
                        validationSearchResult.getIdentifier(), true);
                institutionDto.setCollections(Collections.singletonList(collectionMapper.toDto(collection)));
                matchDtoList.add(MatchDto.builder().match(qualifierValueStr)
                        .institution(institutionDto)
                        .build());
            }
        } else {
            log.debug("Collection Not Available");
            for (Institution institution : validationSearchResult.getInstitutions()) {
                String instUniqueName = institution.getUniqueName();
                String qualifierValueStr = buildQualifierValueString(instUniqueName, null,
                        validationSearchResult.getIdentifier(), false);
                matchDtoList.add(MatchDto.builder().match(qualifierValueStr)
                        .institution(instituteMapper.toDto(institution))
                        .build());
            }
        }
        //build object and return
        return SAHResponseDto.builder()
                .matches(matchDtoList)
                .success(true)
                .inputValue(validationSearchResult.getInputParams())
                .matchLevel(validationSearchResult.getMatch() == EXACT_MATCH ? MATCH_LEVEL_EXACT : MATCH_LEVEL_PARTIAL)
                .message(validationSearchResult.getMessage() != null ? validationSearchResult.getMessage() : null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * buildQualifierValueString.
     *
     * @param instUniqueName
     * @param collCode
     * @param identifier
     * @param collectionAvailable
     * @return
     */
    private String buildQualifierValueString(String instUniqueName, String collCode,
                                             String identifier, boolean collectionAvailable) {
        StringJoiner sjQualifierValue = new StringJoiner(":");
        sjQualifierValue.add(instUniqueName);
        if (!isEmpty(collCode)) {
            sjQualifierValue.add(collCode);
        }
        sjQualifierValue.add(identifier);
        return sjQualifierValue.toString();
    }

    private SAHResponseDto buildTooManyMatchErrorResponse(ValidationSearchResult validationSearchResult) {
        //build an empty object and return
        log.info("Too many matches found for the given input -- {}", validationSearchResult.getInputParams());
        return SAHResponseDto.builder().success(false)
                .matchLevel(MATCH_LEVEL_TOO_MANY)
                .matches(new ArrayList<MatchDto>())
                .inputValue(validationSearchResult.getInputParams())
                .build();
    }

    private SAHResponseDto buildMatchErrorResponse(ValidationSearchResult validationSearchResult) {
        //build an empty object and return
        log.info("No match found for the given input -- {}", validationSearchResult.getInputParams());
        return SAHResponseDto.builder().success(false)
                .matchLevel(MATCH_LEVEL_NONE)
                .matches(new ArrayList<MatchDto>())
                .inputValue(validationSearchResult.getInputParams())
                .build();
    }
}
