package com.rationsentinel.controller;

import com.rationsentinel.entity.User;
import com.rationsentinel.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CREATE USER
    @PostMapping
    public User createUser(@RequestBody User user,
                           @RequestParam String role) {
        return userService.createUser(user, role);
    }

    // GET USERS BY ROLE
    @GetMapping("/role/{roleName}")
    public List<User> getUsersByRole(@PathVariable String roleName) {
        return userService.getUsersByRole(roleName);
    }

    // GET USER BY EMAIL
    @GetMapping("/email")
    public User getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }
}
