package com.shop.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UserController.USERS_URL)
public class UserController {
    public static final String USERS_URL = "/web-api/users";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping
    public User save(
        @RequestBody User user
    ) {
        return userService.save(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone(),
            user.getPassword(),
            user.getAdminNumberId()
        );
    }

    @PostMapping("/{id}")
    public User update(
        @PathVariable long id,
        @RequestBody UserDto user
    ) {
        User updatedUser = new User();
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        return userService.update(id, updatedUser.getFirstName(), updatedUser.getLastName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }
}
