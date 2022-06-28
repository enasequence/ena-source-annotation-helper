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
    @Field(name = "coll_type")
    private String collType;
    @Field(name = "qualifier_type")
    private String[] qualifierType;
    @Field(name = "coll_url")
    private String collUrl;
    @Field(name = "coll_url_rule")
    private String collUrlRule;

    @Override
    public String toString() {
        return "Collection{" +
                "id='" + id + '\'' +
                ", coll_id=" + coll_id +
                ", instId=" + instId +
                ", collCode='" + collCode + '\'' +
                ", collName='" + collName + '\'' +
                ", collType='" + collType + '\'' +
                ", qualifierType=" + Arrays.toString(qualifierType) +
                ", collUrl='" + collUrl + '\'' +
                ", collUrlRule='" + collUrlRule + '\'' +
                '}';
    }
}
