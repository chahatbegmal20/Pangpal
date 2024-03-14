package dev.n7meless.service;

import dev.n7meless.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    User save(User user);

    void remove(Long id);
}
