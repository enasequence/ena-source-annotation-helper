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

    List<Institute> institutes;

    public InstituteResponseDto(List<Institute> institutes) {
        this.institutes = institutes;
    }

    public InstituteResponseDto(List<Institute> institutes, boolean success, LocalDateTime timestamp) {
        super(success, timestamp);
        this.institutes = institutes;
    }

    public InstituteResponseDto(List<Institute> institutes, boolean success, LocalDateTime timestamp,
                                ErrorResponse error) {
        super(success, timestamp, error);
        this.institutes = institutes;
    }
}
