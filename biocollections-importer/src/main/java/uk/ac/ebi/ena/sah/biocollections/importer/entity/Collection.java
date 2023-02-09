/*
 * ******************************************************************************
 *  * Copyright 2021 EMBL-EBI, Hinxton outstation
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package uk.ac.ebi.ena.sah.biocollections.importer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import uk.ac.ebi.ena.sah.biocollections.importer.utils.AppConstants;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(indexName = AppConstants.INDEX_COLLECTION_ALIAS)
public class Collection {

    @Id
    private String id;
    @Field(name = "coll_id")
    @JsonProperty("coll_id")
    private int collId;
    @Field(name = "inst_id")
    @JsonProperty("inst_id")
    private int instId;
    @Field(name = "coll_code")
    @JsonProperty("coll_code")
    private String collCode;
    @Field(name = "coll_name")
    @JsonProperty("coll_name")
    private String collName;
    @Field(name = "coll_type")
    @JsonProperty("coll_type")
    private String collType;
    @Field(name = "qualifier_type")
    @JsonProperty("qualifier_type")
    private String qualifierType;
    @Field(name = "coll_url")
    @JsonProperty("coll_url")
    private String collUrl;
    @Field(name = "coll_url_rule")
    @JsonProperty("coll_url_rule")
    private String collUrlRule;

    @Override
    public String toString() {
        return "Collection{" +
                "id='" + id + '\'' +
                ", coll_id=" + collId +
                ", instId=" + instId +
                ", collCode='" + collCode + '\'' +
                ", collName='" + collName + '\'' +
                ", collType='" + collType + '\'' +
                ", qualifierType=" + qualifierType +
                ", collUrl='" + collUrl + '\'' +
                ", collUrlRule='" + collUrlRule + '\'' +
                '}';
    }
}
