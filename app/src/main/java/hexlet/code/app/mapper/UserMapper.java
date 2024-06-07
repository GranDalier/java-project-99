package hexlet.code.app.mapper;

import hexlet.code.app.dto.user.UserDTO;
import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.dto.user.UserUpdateDTO;
import hexlet.code.app.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    public abstract UserDTO map(User model);

    public abstract User map(UserCreateDTO dto);

    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);
}
