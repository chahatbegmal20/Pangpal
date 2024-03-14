package dev.n7meless.mapper;

import dev.n7meless.dto.user.UserDto;
import dev.n7meless.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    UserDto map(User user);

    @InheritInverseConfiguration
    User map(UserDto user);
}
