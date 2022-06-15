package uk.ac.ebi.ena.annotation.helper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Arrays;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstitutionDto {

    private String institutionCode;
    private String uniqueName;
    private String institutionName;
    private String country;
    private String address;
    private String collectionType;
    private String[] qualifierType;
    private String homeUrl;
    private List<CollectionDto> collections;

    @Override
    public String toString() {
        return "InstituteDto{" +
                "instituteCode='" + institutionCode + '\'' +
                ", uniqueName='" + uniqueName + '\'' +
                ", instituteName='" + institutionName + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", collectionType='" + collectionType + '\'' +
                ", qualifierType=" + Arrays.toString(qualifierType) +
                ", homeUrl='" + homeUrl + '\'' +
                '}';
    }
}
