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

package uk.ac.ebi.ena.sah.biocollections.importer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceHealthResponseDto {

    private boolean success;
    private LocalDateTime timestamp;
    private String error;

    public ServiceHealthResponseDto() {
    }

    public ServiceHealthResponseDto(boolean success, LocalDateTime timestamp) {
        this.success = success;
        this.timestamp = timestamp;
    }

    public ServiceHealthResponseDto(boolean success, LocalDateTime timestamp, String error) {
        this.success = success;
        this.timestamp = timestamp;
        this.error = error;
    }

}
