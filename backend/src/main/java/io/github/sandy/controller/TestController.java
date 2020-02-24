package io.github.sandy.controller;

import io.github.sandy.entity.User;
import io.github.sandy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/test")
    public List<User> getUsers(){
        return userRepository.findAll();
    }
}
