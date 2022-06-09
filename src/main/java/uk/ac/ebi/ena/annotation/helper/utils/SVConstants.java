package uk.ac.ebi.ena.annotation.helper.utils;

public interface SVConstants {

    int EXACT_MATCH = 0;
    int MULTI_NEAR_MATCH = 1;
    int TOO_MANY_MATCH = 2;
    int NO_MATCH = -1;

    String MATCH_LEVEL_EXACT = "Exact";
    String MATCH_LEVEL_PARTIAL = "Partial";
    String MATCH_LEVEL_TOO_MANY = "Too Many Partial Matches";
    String MATCH_LEVEL_NONE = "None";

}
