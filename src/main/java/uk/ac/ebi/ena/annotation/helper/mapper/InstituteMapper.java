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

package uk.ac.ebi.ena.annotation.helper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.ac.ebi.ena.annotation.helper.dto.InstitutionDto;
import uk.ac.ebi.ena.annotation.helper.entity.Institution;

@Mapper(componentModel = "spring")
public interface InstituteMapper {

    @Mapping(source = "instCode", target = "institutionCode")
    @Mapping(source = "instName", target = "institutionName")
    InstitutionDto toDto(Institution entity);

    @Mapping(source = "institutionCode", target = "instCode")
    @Mapping(source = "institutionName", target = "instName")
    Institution toNewEntity(InstitutionDto dto);

}
