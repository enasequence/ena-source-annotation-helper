package uk.ac.ebi.ena.annotation.helper.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ena.annotation.helper.dto.MatchDto;
import uk.ac.ebi.ena.annotation.helper.dto.SAHResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.ValidationSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.*;
import static uk.ac.ebi.ena.annotation.helper.utils.SAHConstants.*;

@Component
@Slf4j
public class SAHResponseMapper {

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

    private SAHResponseDto buildSuccessMatchResponse(ValidationSearchResult validationSearchResult) {
        //mean only single entry in response array
        log.debug("Building Success Match Response");
        List<MatchDto> matchDtoList = new ArrayList();
        log.info("Building success response for the given input -- {}", validationSearchResult.getInputParams());
        if (validationSearchResult.isCollectionAvailable()) {
            log.debug("Collection Available");
            for (Collection collection : validationSearchResult.getCollections()) {
                String instUniqueName = validationSearchResult.getInstituteIdNameMap().get(collection.getInstId());
                String qualifierValueStr = buildQualifierValueString(instUniqueName, collection.getCollCode(),
                        validationSearchResult.getIdentifier(), true);
                matchDtoList.add(MatchDto.builder().match(qualifierValueStr).qualifierType(collection.getQualifierType()).build());
            }
        } else {
            log.debug("Collection Not Available");
            for (Institute institute : validationSearchResult.getInstitutes()) {
                String instUniqueName = institute.getUniqueName();
                String qualifierValueStr = buildQualifierValueString(instUniqueName, null,
                        validationSearchResult.getIdentifier(), false);
                matchDtoList.add(MatchDto.builder().match(qualifierValueStr).qualifierType(institute.getQualifierType()).build());
            }
        }
        //build object and return
        return SAHResponseDto.builder()
                .matches(matchDtoList)
                .success(true)
                .matchLevel(validationSearchResult.getMatch() == EXACT_MATCH ? MATCH_LEVEL_EXACT : MATCH_LEVEL_PARTIAL)
                .message(validationSearchResult.getMessage() != null ? validationSearchResult.getMessage() : null)
                .timestamp(LocalDateTime.now())
                .build();
    }

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
        //build error object and return
        log.info("Too many matches found for the given input -- {}", validationSearchResult.getInputParams());
        return SAHResponseDto.builder().success(false)
                .error(ErrorResponse.builder().code(TooManyMatchesError).message(TooManyMatchesMessage).build())
                .matchLevel(MATCH_LEVEL_TOO_MANY)
                .inputValue(validationSearchResult.getInputParams()).build();
    }

    private SAHResponseDto buildMatchErrorResponse(ValidationSearchResult validationSearchResult) {
        //build error object and return
        log.info("No match found for the given input -- {}", validationSearchResult.getInputParams());
        return SAHResponseDto.builder().success(false)
                .error(ErrorResponse.builder().code(NoMatchError).message(NoMatchMessage).build())
                .matchLevel(MATCH_LEVEL_NONE)
                .inputValue(validationSearchResult.getInputParams()).build();
    }

}
