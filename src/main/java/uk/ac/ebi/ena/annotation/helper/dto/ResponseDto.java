package uk.ac.ebi.ena.annotation.helper.dto;

import uk.ac.ebi.ena.annotation.helper.exception.SVErrorResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {

    private String message;
    private boolean success;
    private LocalDateTime timestamp;
    private List<SVErrorResponse> errors;

    public ResponseDto() {
    }

    public ResponseDto(String message, boolean success, LocalDateTime timestamp, List<SVErrorResponse> errors) {
        this.message = message;
        this.success = success;
        this.timestamp = timestamp;
        this.errors = errors;
    }
}
