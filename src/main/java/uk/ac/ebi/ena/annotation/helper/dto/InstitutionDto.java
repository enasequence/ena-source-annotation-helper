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
