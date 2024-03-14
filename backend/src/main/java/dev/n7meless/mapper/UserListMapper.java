package dev.n7meless.mapper;

import dev.n7meless.dto.user.UserDto;
import dev.n7meless.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserListMapper {
    List<UserDto> entityToDtoList(List<User> users);

    List<User> dtoToEntityList(List<UserDto> users);
}
