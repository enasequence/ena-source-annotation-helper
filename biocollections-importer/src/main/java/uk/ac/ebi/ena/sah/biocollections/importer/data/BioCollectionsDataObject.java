package uk.ac.ebi.ena.sah.biocollections.importer.data;

import uk.ac.ebi.ena.sah.biocollections.importer.entity.Collection;
import uk.ac.ebi.ena.sah.biocollections.importer.entity.Institution;

import java.util.HashMap;
import java.util.Map;

public class BioCollectionsDataObject {

    public static Map<Integer, Institution> mapInstitutions = new HashMap<Integer, Institution>();
    public static Map<Integer, Collection> mapCollections = new HashMap<Integer, Collection>();

}
