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

package uk.ac.ebi.ena.annotation.helper.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


import java.util.Arrays;
import java.util.List;

import static uk.ac.ebi.ena.annotation.helper.utils.SAHConstants.INDEX_INSTITUTION;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(indexName = INDEX_INSTITUTION)
public class Institution {

    @Id
    private String id;
    @Field(name = "inst_id")
    private int instId;
    @Field(name = "inst_code")
    private String instCode;
    @Field(name = "unique_name")
    private String uniqueName;
    @Field(name = "synonyms")
    private String synonyms;
    @Field(name = "inst_name")
    private String instName;
    private String country;
    private String address;
    @Field(name = "coll_type")
    private String collType;
    @Field(name = "qualifier_type")
    private List<String> qualifierType;

    @Field(name = "home_url")
    private String homeUrl;
    @Field(name = "url_rule")
    private String urlRule;

    @Override
    public String toString() {
        return "Institution{" +
                "id='" + id + '\'' +
                ", instId=" + instId +
                ", instCode='" + instCode + '\'' +
                ", uniqueName='" + uniqueName + '\'' +
                ", synonyms='" + synonyms + '\'' +
                ", instName='" + instName + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", collType='" + collType + '\'' +
                ", qualifierType=" + qualifierType +
                ", homeUrl='" + homeUrl + '\'' +
                ", urlRule='" + urlRule + '\'' +
                '}';
    }
}
