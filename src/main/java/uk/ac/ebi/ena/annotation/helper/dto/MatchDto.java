package uk.ac.ebi.ena.annotation.helper.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchDto {

    String match;
    InstituteDto institute;

}
