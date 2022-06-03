package uk.ac.ebi.ena.annotation.helper.dto;

import lombok.Data;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;

import java.util.List;

@Data
public class InstituteResponse {

    int match;
    List<Institute> institutes;
    private String message;
    private boolean success;

}
