package dev.n7meless.mapper;

import dev.n7meless.dto.user.UserDto;
import dev.n7meless.entity.User;
import dev.n7meless.model.Status;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMapperTest {
    private final UserMapper mapper = new UserMapperImpl();
    private final UserListMapper mapperList = new UserListMapperImpl(mapper);

    @Test
    void givenUserToDto_whenMaps_thenCorrect(){
        //given
        User user = new User();
        user.setId(1);
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setStatus(Status.ACTIVE);

        //when
        UserDto userDto = mapper.map(user);

        //then
        assertEquals(user.getFirstName(),userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
    }

    @Test
    void shouldProperlyMapListDtosToListModels(){
        //given
        User user = new User();
        user.setId(1);
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setStatus(Status.ACTIVE);

        List<User> users = Collections.singletonList(user);

        //when
        List<UserDto> dtoList = mapperList.entityToDtoList(users);

        //then
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
        assertEquals(user.getFirstName(), dtoList.get(0).getFirstName());
        assertEquals(user.getId(), dtoList.get(0).getId());
    }

}
