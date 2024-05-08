package com.example.blog.mapper;

import com.example.blog.entity.User;

public interface UserMapper {
    Integer reg(User user);

    User findByUsername(String username);

    User findByUid(Integer uid);
}
