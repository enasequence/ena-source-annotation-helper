package uk.ac.ebi.ena.annotation.helper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.ac.ebi.ena.annotation.helper.dto.InstitutionDto;
import uk.ac.ebi.ena.annotation.helper.entity.Institution;

@Mapper(componentModel = "spring")
public interface InstituteMapper {

    @Mapping(source="instCode", target="institutionCode")
    @Mapping(source="instName", target="institutionName")
    InstitutionDto toDto(Institution entity);

    @Mapping(source="institutionCode", target="instCode")
    @Mapping(source="institutionName", target="instName")
    Institution toNewEntity(InstitutionDto dto);

}
