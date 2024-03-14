package dev.n7meless.service.impl;

import dev.n7meless.entity.User;
import dev.n7meless.entity.Role;
import dev.n7meless.repository.RoleRepository;
import dev.n7meless.repository.UserRepository;
import dev.n7meless.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@CacheConfig(cacheNames = {"user", "users"})
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Cacheable(key = "#id", value = "user")
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "users", allEntries = true),
            @CacheEvict(key = "#id", value = "user")})
    public void remove(Long id) {
        userRepository.findById(id);
    }

    @Override
    @Cacheable(value = "users")
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @CachePut(key = "#user.id", value = "user")
    public User save(User user) {
//        Set<Role> roles = user.getRoles();
//        for (Role role : roles) {
//
//        }
        return userRepository.save(user);
    }

    public Set<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

}

