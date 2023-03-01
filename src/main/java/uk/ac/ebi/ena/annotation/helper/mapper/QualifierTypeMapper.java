package uk.ac.ebi.ena.annotation.helper.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum QualifierTypeMapper {

    SPECIMEN_V("specimen_voucher", "specimen-voucher"),
    BIO_MAT("bio_material", "bio-material"),
    CULTURE_COLL("culture_collection", "culture-collection");

    private static Map<String, QualifierTypeMapper> valueToTextMapping;

    private final String key;
    private final String value;

    QualifierTypeMapper(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static void initMapping() {
        valueToTextMapping = new HashMap<>();
        for (QualifierTypeMapper s : values()) {
            valueToTextMapping.put(s.key, s);
        }
    }

    public static QualifierTypeMapper getQualifierTypeMapper(String k) {
        if (valueToTextMapping == null) {
            initMapping();
        }
        return valueToTextMapping.get(k);
    }

    public static List<String> getValueList(String[] keys) {
        List<String> values = new ArrayList<>();
        for (String k : keys) {
            values.add(getQualifierTypeMapper(k).getValue());
        }
        return values;
    }

}
