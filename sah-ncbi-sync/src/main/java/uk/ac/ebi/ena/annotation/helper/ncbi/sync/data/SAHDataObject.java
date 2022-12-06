package uk.ac.ebi.ena.annotation.helper.ncbi.sync.data;

import uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity.Institution;

import java.util.HashMap;
import java.util.Map;

public class SAHDataObject {

    public static Map<Integer, Institution> mapInstitutions = new HashMap<Integer, Institution>();
    public static Map<Integer, Collection> mapCollections = new HashMap<Integer, Collection>();

}
