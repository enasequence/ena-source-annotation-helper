package uk.ac.ebi.ena.annotation.helper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Arrays;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionDto {

    private String collectionCode;
    private String collectionName;
    private String collectionType;
    private String[] qualifierType;
    private String collectionUrl;

    @Override
    public String toString() {
        return "CollectionDto{" +
                "collectionCode='" + collectionCode + '\'' +
                ", collectionName='" + collectionName + '\'' +
                ", collectionType='" + collectionType + '\'' +
                ", qualifierType=" + Arrays.toString(qualifierType) +
                ", collectionUrl='" + collectionUrl + '\'' +
                '}';
    }
}
