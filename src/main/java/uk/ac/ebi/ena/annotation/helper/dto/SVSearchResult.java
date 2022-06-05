package uk.ac.ebi.ena.annotation.helper.dto;

import lombok.Builder;
import lombok.Data;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SVSearchResult {

    int match;
    String specimenId;
    List<Institute> institutes;
    Map<Integer, String> instituteIdNameMap;
    List<Collection> collections;
    private String message;
    private boolean success;
    private boolean collectionAvailable;

}
