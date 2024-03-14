package dev.n7meless.controller;


import dev.n7meless.annotation.UnsecuredWebMvcTest;
import dev.n7meless.entity.User;
import dev.n7meless.mapper.UserListMapper;
import dev.n7meless.mapper.UserListMapperImpl;
import dev.n7meless.mapper.UserMapper;
import dev.n7meless.mapper.UserMapperImpl;
import dev.n7meless.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UnsecuredWebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    private static final String URI = "/api/v1/users";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @SpyBean(classes = UserMapperImpl.class)
    private UserMapper userMapper;

    @SpyBean(classes = UserListMapperImpl.class)
    private UserListMapper userListMapper;

    @Test
    public void testGetUserById() throws Exception {

        // given
        User user =
                new User();
        user.setId(1L);
        user.setEmail("user@gmail.com");
        user.setFirstName("user");
        user.setLastName("user");

        given(userService.getUserById(user.getId())).willReturn(Optional.of(user));

        // when + then
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI + "/{user_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

    }

    @Test
    public void testGetAllUsers() throws Exception {

        // given
        User user =
                new User();
        user.setId(1L);
        user.setEmail("user@gmail.com");
        user.setFirstName("user");
        user.setLastName("user");

        given(userService.getAllUsers()).willReturn(Collections.singletonList(user));

        // when + then
        this.mockMvc
                .perform(MockMvcRequestBuilders.get(URI + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());

    }

    @Test
    public void testDeleteUserById() throws Exception {

        doNothing().when(userService).remove(1L);

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete(URI + "/{user_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk());

    }

}
