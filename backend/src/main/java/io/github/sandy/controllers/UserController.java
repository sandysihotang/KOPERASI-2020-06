package io.github.sandy.controllers;

import com.pusher.rest.Pusher;
import io.github.sandy.entities.User;
import io.github.sandy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping(value = "/api/getUser")
    public List<User> getAllUser(){
        Pusher pusher = new Pusher("899299", "b62a6641110833445445", "83aa5943d5de895e8ada");
        pusher.setCluster("ap1");
        pusher.setEncrypted(true);

        pusher.trigger("my-channel", "my-event", Collections.singletonMap("message", "hello world"));
        return userService.getAllUser();
    }

    @GetMapping(value = "/cr")
    public void cr(){
        userService.save();
    }
}