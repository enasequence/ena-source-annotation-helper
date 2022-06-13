package uk.ac.ebi.ena.annotation.helper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Arrays;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstituteDto {

    private String instituteCode;
    private String uniqueName;
    private String instituteName;
    private String country;
    private String address;
    private String collectionType;
    private String[] qualifierType;
    private String homeUrl;
    private List<CollectionDto> collections;

    @Override
    public String toString() {
        return "InstituteDto{" +
                "instituteCode='" + instituteCode + '\'' +
                ", uniqueName='" + uniqueName + '\'' +
                ", instituteName='" + instituteName + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", collectionType='" + collectionType + '\'' +
                ", qualifierType=" + Arrays.toString(qualifierType) +
                ", homeUrl='" + homeUrl + '\'' +
                '}';
    }
}
