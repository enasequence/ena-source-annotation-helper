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
    CollectionDto toDto(Collection entity);

    @Mapping(source = "collectionCode", target = "collCode")
    @Mapping(source = "collectionName", target = "collName")
    @Mapping(source = "collectionUrl", target = "collUrl")
    Collection toNewEntity(CollectionDto dto);

}
