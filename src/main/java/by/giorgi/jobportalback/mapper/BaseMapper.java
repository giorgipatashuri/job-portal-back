package by.giorgi.jobportalback.mapper;


import by.giorgi.jobportalback.model.dto.UserDto;
import by.giorgi.jobportalback.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BaseMapper {
    UserDto userToUserDto(User user);
}
