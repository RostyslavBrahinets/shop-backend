package com.shop.services;

import com.shop.models.User;
import com.shop.repositories.UserRepository;
import com.shop.validators.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        UserValidator userValidator,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(long id) {
        userValidator.validate(id, userRepository.findAll());
        Optional<User> user = userRepository.findById(id);
        return user.orElseGet(User::new);
    }

    public User findByEmail(String email) {
        userValidator.validateEmail(email);
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseGet(User::new);
    }

    public User save(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        long adminNumberId
    ) {
        userValidator.validate(
            firstName,
            lastName,
            email,
            phone,
            password,
            userRepository.findAll()
        );

        return userRepository.save(
            firstName,
            lastName,
            email,
            phone,
            passwordEncoder.encode(password),
            adminNumberId
        );
    }

    public User update(
        long id,
        String firstName,
        String lastName
    ) {
        userValidator.validate(id, userRepository.findAll());
        userValidator.validateFullName(firstName, lastName);
        return userRepository.update(id, firstName, lastName);
    }

    public void delete(long id) {
        userValidator.validate(id, userRepository.findAll());
        userRepository.delete(id);
    }
}
