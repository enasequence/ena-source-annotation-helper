package uk.ac.ebi.ena.annotation.helper.dto;

import lombok.Builder;
import lombok.Data;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static org.springframework.util.ObjectUtils.isEmpty;

@Data
@Builder
public class ValidationSearchResult {

    int match;
    String specimenId;
    List<Institute> institutes;
    Map<Integer, String> instituteIdNameMap;
    List<Collection> collections;
    private String message;
    private boolean success;
    private boolean collectionAvailable;

    private String inputParams;

    public void setInputsParams(String instCode, String collCode, String specimenId) {
        StringJoiner sjInputsParams = new StringJoiner(":");
        sjInputsParams.add(instCode);
        if (!isEmpty(collCode)) {
            sjInputsParams.add(collCode);
        }
        sjInputsParams.add(specimenId);
        this.inputParams = sjInputsParams.toString();
    }

    public void setInputsParams(String specimenVoucher) {
        this.inputParams = specimenVoucher;
    }

}
