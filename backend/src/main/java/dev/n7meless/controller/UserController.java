package dev.n7meless.controller;

import dev.n7meless.dto.user.UserDto;
import dev.n7meless.entity.User;
import dev.n7meless.exception.UserNotFoundError;
import dev.n7meless.mapper.UserListMapper;
import dev.n7meless.mapper.UserMapper;
import dev.n7meless.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;
    private final UserListMapper listMapper;

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public List<UserDto> getUsers() {
        List<User> users = userService.getAllUsers();
        return listMapper.entityToDtoList(users);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.remove(id);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id).map(mapper::map)
                .orElseThrow(UserNotFoundError::new);
    }

}
