package uk.ac.ebi.ena.annotation.helper.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SVErrorResponse {

    private String field;

    private String fieldValue;

    private String message;
}
