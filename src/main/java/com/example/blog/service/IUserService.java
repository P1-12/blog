package com.example.blog.service;

import com.example.blog.entity.User;

public interface IUserService {
    void reg(User user);

    User login(User user);

    User findUserByToken(String token);
}
