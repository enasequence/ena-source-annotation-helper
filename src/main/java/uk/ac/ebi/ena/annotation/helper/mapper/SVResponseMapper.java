package uk.ac.ebi.ena.annotation.helper.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ena.annotation.helper.dto.SVResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.SVSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SVErrorCode.*;
import static uk.ac.ebi.ena.annotation.helper.utils.SVConstants.*;

@Component
@Slf4j
public class SVResponseMapper {

    public SVResponseDto mapResponseDto(SVSearchResult svSearchResult) {

        log.debug("Processing Search Result with {}", svSearchResult.getMatch());
        //build and return exact match response
        if (svSearchResult.getMatch() == EXACT_MATCH || svSearchResult.getMatch() == MULTI_NEAR_MATCH) {
            return buildSuccessMatchResponse(svSearchResult);
        }

        //build and return exact match response
        if (svSearchResult.getMatch() == TOO_MANY_MATCH) {
            return buildTooManyMatchErrorResponse(svSearchResult);
        }

        // NO_MATCH condition
        if (svSearchResult.getMatch() == NO_MATCH) {
            return buildMatchErrorResponse(svSearchResult);
        }

        //no match again -- should not be reached
        return buildMatchErrorResponse(svSearchResult);
    }

    private SVResponseDto buildSuccessMatchResponse(SVSearchResult svSearchResult) {
        //mean only single entry in response array
        log.debug("Building Success Match Response");
        List svList = new ArrayList<String>();
        String specimenVoucherStr;
        log.info("Building success response for the given input -- {}", svSearchResult.getInputParams());
        if (svSearchResult.isCollectionAvailable()) {
            log.debug("Collection Available");
            for (Collection collection : svSearchResult.getCollections()) {
                String instUniqueName = svSearchResult.getInstituteIdNameMap().get(collection.getInstId());
                specimenVoucherStr = buildSpecimenVoucherString(instUniqueName, collection.getCollCode(),
                        svSearchResult.getSpecimenId(), true);
                svList.add(specimenVoucherStr);
            }
        } else {
            log.debug("Collection Not Available");
            for (Institute institute : svSearchResult.getInstitutes()) {
                String instUniqueName = institute.getUniqueName();
                specimenVoucherStr = buildSpecimenVoucherString(instUniqueName, null,
                        svSearchResult.getSpecimenId(), false);
                svList.add(specimenVoucherStr);
            }
        }
        //build object and return
        return SVResponseDto.builder().specimenVoucher(svList)
                .success(true).timestamp(LocalDateTime.now()).build();
    }

    private String buildSpecimenVoucherString(String instUniqueName, String collCode,
                                              String specimenId, boolean collectionAvailable) {
        StringJoiner sjSpecimenVoucher = new StringJoiner(":");
        sjSpecimenVoucher.add(instUniqueName);
        if(!isEmpty(collCode)) {
            sjSpecimenVoucher.add(collCode);
        }
        sjSpecimenVoucher.add(specimenId);
        return sjSpecimenVoucher.toString();
    }

    private SVResponseDto buildTooManyMatchErrorResponse(SVSearchResult svSearchResult) {
        //build error object and return
        log.info("Too many matches found for the given input -- {}", svSearchResult.getInputParams());
        return SVResponseDto.builder().success(false).error(ErrorResponse.builder().
                code(TooManyMatchesError).message(TooManyMatchesMessage).build()).build();
    }

    private SVResponseDto buildMatchErrorResponse(SVSearchResult svSearchResult) {
        //build error object and return
        log.info("No match found for the given input -- {}", svSearchResult.getInputParams());
        return SVResponseDto.builder().success(false).error(ErrorResponse.builder().
                code(NoMatchError).message(NoMatchMessage).build()).build();
    }

}
