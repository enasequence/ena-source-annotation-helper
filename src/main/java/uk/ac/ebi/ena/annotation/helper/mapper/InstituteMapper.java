package uk.ac.ebi.ena.annotation.helper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.ac.ebi.ena.annotation.helper.dto.InstituteDto;
import uk.ac.ebi.ena.annotation.helper.entity.Institute;

@Mapper(componentModel = "spring")
public interface InstituteMapper {

    @Mapping(source="instCode", target="instituteCode")
    @Mapping(source="instName", target="instituteName")
    InstituteDto toDto(Institute entity);

    @Mapping(source="instituteCode", target="instCode")
    @Mapping(source="instituteName", target="instName")
    Institute toNewEntity(InstituteDto dto);

}
