package com.example.blog.mapper;

import com.example.blog.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void reg(){
        User user = new User();
        user.setUsername("123");
        user.setPassword("123");
        user.setSalt("123");
        userMapper.reg(user);
    }
}

