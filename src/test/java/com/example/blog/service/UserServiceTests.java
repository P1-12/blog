package com.example.blog.service;

import com.example.blog.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private IUserService userService;

    @Test
    public void reg(){
        User user = new User();
        user.setUsername("a");
        user.setPassword("123");
        userService.reg(user);
    }

    @Test
    public void login(){
        User user = new User();
        user.setUsername("a");
        user.setPassword("123");
        userService.login(user);
    }
}
