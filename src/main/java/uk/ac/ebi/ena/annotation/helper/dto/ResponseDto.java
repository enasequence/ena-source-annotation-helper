package uk.ac.ebi.ena.annotation.helper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import uk.ac.ebi.ena.annotation.helper.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {

    private boolean success;
    private LocalDateTime timestamp;
    private List<ErrorResponse> errors;

    public ResponseDto() {
    }

    public ResponseDto(boolean success, LocalDateTime timestamp) {
        this.success = success;
        this.timestamp = timestamp;
    }

    public ResponseDto(boolean success, LocalDateTime timestamp, List<ErrorResponse> errors) {
        this.success = success;
        this.timestamp = timestamp;
        this.errors = errors;
    }
}
