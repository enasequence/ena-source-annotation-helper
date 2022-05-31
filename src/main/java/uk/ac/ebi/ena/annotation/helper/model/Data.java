package uk.ac.ebi.ena.annotation.helper.model;

import java.util.Map;

@lombok.Data
public class Data {

    private String query;
    private Map<String, Object> variables;
}
