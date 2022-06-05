package uk.ac.ebi.ena.annotation.helper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.exception.SVErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstituteResponseDto extends ResponseDto{

    List<Institute> institutes;

    public InstituteResponseDto(List<Institute> institutes) {
        this.institutes = institutes;
    }

    public InstituteResponseDto(String message, boolean success, LocalDateTime timestamp, List<SVErrorResponse> errors, List<Institute> institutes) {
        super(message, success, timestamp, errors);
        this.institutes = institutes;
    }
}
