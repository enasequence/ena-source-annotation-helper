package uk.ac.ebi.ena.annotation.helper.ncbi.sync.data;

import uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity.Collection;
import uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity.Institution;

import java.util.HashMap;
import java.util.Map;

public class SAHDataObject {

    public static Map<Long, Institution> mapInstitutions = new HashMap<Long, Institution>();
    public static Map<Long, Collection> mapCollections = new HashMap<Long, Collection>();

}
