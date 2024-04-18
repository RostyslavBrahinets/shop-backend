package com.shop.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(UserController.USERS_URL)
public class UserController {
    public static final String USERS_URL = "/api/v1/users";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping("/")
    public User save(
        @RequestBody User user
    ) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    public User update(
        @PathVariable long id,
        @RequestBody UserDto user
    ) {
        return userService.update(
            id,
            User.of(
                user.firstName(),
                user.lastName()
            )
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        userService.delete(User.of(null, null).withId(id));
    }
}
