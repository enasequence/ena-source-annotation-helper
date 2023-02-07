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

package uk.ac.ebi.ena.annotation.helper.ncbi.sync.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Institution {
    private String id;
    private int instId;
    private String instCode;
    private String uniqueName;
    private String synonyms;
    private String instName;
    private String country;
    private String address;
    private String collType;
    private String qualifierType;
    private String homeUrl;
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
