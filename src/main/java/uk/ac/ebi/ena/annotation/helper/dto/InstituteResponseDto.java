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
import lombok.Getter;
import lombok.Setter;
import uk.ac.ebi.ena.annotation.helper.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstituteResponseDto extends ResponseDto {

    List<InstitutionDto> institutions;

    public InstituteResponseDto(List<InstitutionDto> institutions) {
        this.institutions = institutions;
    }

    public InstituteResponseDto(List<InstitutionDto> institutions, boolean success, LocalDateTime timestamp) {
        super(success, timestamp);
        this.institutions = institutions;
    }

    public InstituteResponseDto(List<InstitutionDto> institutions, boolean success, LocalDateTime timestamp,
                                List<ErrorResponse> errors) {
        super(success, timestamp, errors);
        this.institutions = institutions;
    }
}
