package uk.ac.ebi.ena.annotation.helper.dto;

import lombok.Data;

import java.util.Map;

@Data
@Deprecated
public class GraphMetaData {

    private String query;
    private Map<String, Object> variables;
}
