package by.giorgi.jobportalback.mapper;

import by.giorgi.jobportalback.model.dto.CvDto;
import by.giorgi.jobportalback.model.entity.Cv;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CvMapper {

    @Mapping(target = "user", ignore = true)
    Cv cvDtoToCV(CvDto cvDTO);

    CvDto cvToCVDto(Cv cv);

    List<CvDto> cvsToCvDtos(List<Cv> cvs);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCVFromDTO(CvDto cvDTO, @MappingTarget Cv cv);
}