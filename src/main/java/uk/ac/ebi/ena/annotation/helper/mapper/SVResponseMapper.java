package uk.ac.ebi.ena.annotation.helper.mapper;

import uk.ac.ebi.ena.annotation.helper.dto.SVResponseDto;
import uk.ac.ebi.ena.annotation.helper.dto.SVSearchResult;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static uk.ac.ebi.ena.annotation.helper.exception.SVErrorCode.TooManyMatchesMessage;
import static uk.ac.ebi.ena.annotation.helper.exception.SVErrorCode.ValidationFailedMessage;
import static uk.ac.ebi.ena.annotation.helper.utils.SVConstants.*;

public class SVResponseMapper {

    public SVResponseDto mapResponseDto(SVSearchResult svSearchResult) {

        //build and return exact match response
        if (svSearchResult.getMatch() == EXACT_MATCH || svSearchResult.getMatch() == MULTI_NEAR_MATCH) {
            return buildSuccessMatchResponse(svSearchResult);
        }

        //build and return exact match response
        if (svSearchResult.getMatch() == TOO_MANY_MATCH) {
            return buildTooManyMatchErrorResponse(svSearchResult);
        }

        // NO_MATCH condition
        return buildMatchErrorResponse(svSearchResult);
    }

    private SVResponseDto buildSuccessMatchResponse(SVSearchResult svSearchResult) {
        //mean only single entry in response array
        List svList = new ArrayList<String>();
        String specimenVoucherStr;
        if (svSearchResult.isCollectionAvailable()) {
            for (Collection collection : svSearchResult.getCollections()) {
                String instUniqueName = svSearchResult.getInstituteIdNameMap().get(collection.getInstId());
                specimenVoucherStr = buildSpecimenVoucherString(instUniqueName, collection.getCollCode(),
                        svSearchResult.getSpecimenId(), true);
                svList.add(specimenVoucherStr);
            }
        } else {
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
        sjSpecimenVoucher.add(collCode);
        sjSpecimenVoucher.add(specimenId);
        return sjSpecimenVoucher.toString();
    }

    private SVResponseDto buildTooManyMatchErrorResponse(SVSearchResult svSearchResult) {
        //build error object and return
        return SVResponseDto.builder().message(TooManyMatchesMessage)
                .success(false).timestamp(LocalDateTime.now()).build();
    }

    private SVResponseDto buildMatchErrorResponse(SVSearchResult svSearchResult) {
        //build error object and return
        return SVResponseDto.builder().message(ValidationFailedMessage)
                .success(false).timestamp(LocalDateTime.now()).build();
    }

}
