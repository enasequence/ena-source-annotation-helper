package uk.ac.ebi.ena.annotation.helper.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Arrays;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(indexName = "institute")
public class Institute {

    @Id
    private String id;
    @Field(name = "inst_id")
    private int instId;
    @Field(name = "inst_code")
    private String instCode;
    @Field(name = "unique_name")
    private String uniqueName;
    @Field(name = "inst_name")
    private String instName;
    private String country;
    private String address;
    @Field(name = "collection_type")
    private String collectionType;
    @Field(name = "qualifier_type")
    private String[] qualifierType;

    @Field(name = "home_url")
    private String homeUrl;
    @Field(name = "url_rule")
    private String urlRule;

    @Override
    public String toString() {
        return "Institute{" +
                "id='" + id + '\'' +
                ", instId=" + instId +
                ", instCode='" + instCode + '\'' +
                ", uniqueName='" + uniqueName + '\'' +
                ", instName='" + instName + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", collectionType='" + collectionType + '\'' +
                ", qualifierType=" + Arrays.toString(qualifierType) +
                ", homeUrl='" + homeUrl + '\'' +
                ", urlRule='" + urlRule + '\'' +
                '}';
    }
}
