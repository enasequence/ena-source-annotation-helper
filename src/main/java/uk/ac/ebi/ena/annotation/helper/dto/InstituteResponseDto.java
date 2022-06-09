package uk.ac.ebi.ena.annotation.helper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;
import uk.ac.ebi.ena.annotation.helper.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstituteResponseDto extends ResponseDto {

    List<InstituteDto> institutes;

    public InstituteResponseDto(List<InstituteDto> institutes) {
        this.institutes = institutes;
    }

    public InstituteResponseDto(List<InstituteDto> institutes, boolean success, LocalDateTime timestamp) {
        super(success, timestamp);
        this.institutes = institutes;
    }

    public InstituteResponseDto(List<InstituteDto> institutes, boolean success, LocalDateTime timestamp,
                                List<ErrorResponse> errors) {
        super(success, timestamp, errors);
        this.institutes = institutes;
    }
}
