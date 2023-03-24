package com.shop.user;

import com.shop.adminnumber.AdminNumberService;
import com.shop.adminnumber.AdminNumberValidator;
import com.shop.interfaces.ServiceInterface;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements ServiceInterface<User> {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final AdminNumberValidator adminNumberValidator;
    private final AdminNumberService adminNumberService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        UserValidator userValidator,
        AdminNumberValidator adminNumberValidator,
        AdminNumberService adminNumberService,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.adminNumberValidator = adminNumberValidator;
        this.adminNumberService = adminNumberService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
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

    @Override
    public User save(User user) {
        userValidator.validate(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone(),
            user.getPassword(),
            userRepository.findAll()
        );

        user.setPassword(passwordEncoder.encode(String.copyValueOf(user.getPassword())).toCharArray());

        adminNumberValidator.validateAdminNumberForSignUp(
            user.getAdminNumber(),
            adminNumberService.findAll()
        );

        userRepository.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        userValidator.validate(user.getId(), userRepository.findAll());
        userValidator.validateFullName(user.getFirstName(), user.getLastName());
        userRepository.update(user);
        return user;
    }

    @Override
    public void delete(User user) {
        userValidator.validate(user.getId(), userRepository.findAll());
        userRepository.delete(user);
    }
}
