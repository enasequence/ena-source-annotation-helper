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
@Document(indexName = "collection")
public class Collection {

    @Id
    private String id;
    @Field(name = "coll_id")
    private int coll_id;
    @Field(name = "inst_id")
    private int instId;
    @Field(name = "coll_code")
    private String collCode;
    @Field(name = "coll_name")
    private String collName;
    @Field(name = "collection_type")
    private String collectionType;
    @Field(name = "qualifier_type")
    private String[] qualifierType;
    @Field(name = "coll_url")
    private String collUrl;

    @Override
    public String toString() {
        return "Collection{" +
                "id='" + id + '\'' +
                ", coll_id=" + coll_id +
                ", instId=" + instId +
                ", collCode='" + collCode + '\'' +
                ", collName='" + collName + '\'' +
                ", collectionType='" + collectionType + '\'' +
                ", qualifierType=" + Arrays.toString(qualifierType) +
                ", collUrl='" + collUrl + '\'' +
                '}';
    }
}
