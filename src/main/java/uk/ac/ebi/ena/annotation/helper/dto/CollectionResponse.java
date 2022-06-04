package uk.ac.ebi.ena.annotation.helper.dto;

import lombok.Builder;
import lombok.Data;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;

import java.util.List;

@Data
@Builder
public class CollectionResponse {

    int match;
    List<Collection> collections;
    private String message;
    private boolean success;

}
