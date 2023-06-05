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

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Institution {

    @Id
    private String id;
    @JsonProperty("inst_id")
    private int instId;
    @JsonProperty("inst_code")
    private String instCode;
    @JsonProperty("unique_name")
    private String uniqueName;
    @JsonProperty("synonyms")
    private String synonyms;
    @JsonProperty("inst_name")
    private String instName;
    private String country;
    private String address;
    @JsonProperty("coll_type")
    private String collType;
    @JsonProperty("qualifier_type")
    private String[] qualifierType;

    @JsonProperty("home_url")
    private String homeUrl;
    @JsonProperty("url_rule")
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
