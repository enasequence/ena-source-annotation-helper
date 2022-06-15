package uk.ac.ebi.ena.annotation.helper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uk.ac.ebi.ena.annotation.helper.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstituteResponseDto extends ResponseDto {

    List<InstitutionDto> institutions;

    public InstituteResponseDto(List<InstitutionDto> institutions) {
        this.institutions = institutions;
    }

    public InstituteResponseDto(List<InstitutionDto> institutions, boolean success, LocalDateTime timestamp) {
        super(success, timestamp);
        this.institutions = institutions;
    }

    public InstituteResponseDto(List<InstitutionDto> institutions, boolean success, LocalDateTime timestamp,
                                List<ErrorResponse> errors) {
        super(success, timestamp, errors);
        this.institutions = institutions;
    }
}
