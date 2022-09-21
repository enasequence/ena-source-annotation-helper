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
import uk.ac.ebi.ena.annotation.helper.dto.CollectionDto;
import uk.ac.ebi.ena.annotation.helper.entity.Collection;

@Mapper(componentModel = "spring")
public interface CollectionMapper {

    @Mapping(source = "collCode", target = "collectionCode")
    @Mapping(source = "collName", target = "collectionName")
    @Mapping(source = "collUrl", target = "collectionUrl")
    @Mapping(source = "collType", target = "collectionType")
    CollectionDto toDto(Collection entity);

    @Mapping(source = "collectionCode", target = "collCode")
    @Mapping(source = "collectionName", target = "collName")
    @Mapping(source = "collectionUrl", target = "collUrl")
    @Mapping(source = "collectionType", target = "collType")
    Collection toNewEntity(CollectionDto dto);

}
