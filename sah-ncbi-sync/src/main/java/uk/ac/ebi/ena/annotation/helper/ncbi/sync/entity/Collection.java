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
public class Collection {

    private String id;
    private int coll_id;
    private int instId;
    private String collCode;
    private String collName;
    private String collType;
    private String qualifierType;
    private String collUrl;
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
                ", qualifierType=" + qualifierType +
                ", collUrl='" + collUrl + '\'' +
                ", collUrlRule='" + collUrlRule + '\'' +
                '}';
    }
}
