package uk.ac.ebi.ena.annotation.helper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import uk.ac.ebi.ena.annotation.helper.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SAHResponseDto {

    private boolean success;

    private String matchLevel;

    private LocalDateTime timestamp;

    private String message;

    private String inputValue;

    List<MatchDto> matches;

    ErrorResponse error;

}
