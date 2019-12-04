package io.github.sandy.services;


import io.github.sandy.entities.User;
import io.github.sandy.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepositories userRepositories;

    public List<User> getAllUser(){
        return userRepositories.findAll();
    }

    public void save(){
        userRepositories.save(new User("sa",new Date(),new Date(),1,"sa","sa"));
    }
}
