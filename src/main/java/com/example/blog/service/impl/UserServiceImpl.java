package com.example.blog.service.impl;

import com.example.blog.entity.User;
import com.example.blog.mapper.UserMapper;
import com.example.blog.service.IUserService;
import com.example.blog.service.ex.*;
import com.example.blog.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public void reg(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if(userMapper.findByUsername(username) != null){
            //数据库查到用户名，抛出用户名已存在异常
            throw new UsernameDuplicatedException();
        }

        //获取盐值（随机生成一个盐值）
        String salt = UUID.randomUUID().toString().toUpperCase();
        //补全数据:盐值的记录
        user.setSalt(salt);
        //将密码和盐值作为一个整体进行加密处理,忽略原有密码强度,提升了数据的安全性
        String md5Password = getMD5Password(password,salt);
        //将加密之后的密码重新补全设置到user对象中
        user.setPassword(md5Password);

        Integer rows = userMapper.reg(user);
        if(rows != 1){
            throw new RegException();
        }
    }

    @Override
    public User login(User user) {
        String username = user.getUsername();
        String password = user.getPassword().trim();
        User result = userMapper.findByUsername(username);
        if(result == null){
            throw new UserNotFoundException();
        }

        //获取数据库盐值
        String salt = result.getSalt();
        String md5Password = getMD5Password(password,salt).trim();

        if(!result.getPassword().equals(md5Password)){
            throw new PasswordErrorException();
        }

        // 登录成功下发token
        String token = JwtUtil.createJwt(result);
        result.setToken(token);
        result.setPassword(null);
        result.setSalt(null);
        return result;
    }

    @Override
    public User findUserByToken(String token) {
        if(token == null || token == ""){
            throw new TokenErrorException("token不合法");
        }

        Claims claims = JwtUtil.CheckToken(token);
        if(claims == null){
            throw new TokenErrorException("token过期");
        }

        Integer uid = (Integer)claims.get("uid");
        String username = (String) claims.get("username");
        User user = new User();
        user.setUid(uid);
        user.setUsername(username);

        return user;
    }

    private String getMD5Password(String password, String salt){
        //md5加密算法方法的调用(进行三次)
        for(int i=0;i<3;i++){
            password = DigestUtils.md5DigestAsHex((salt+password+salt).getBytes()).toUpperCase();
        }
        //返回加密之后的密码
        return password;
    }
}
