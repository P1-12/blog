package com.example.blog.controller;

import com.example.blog.aop.LogAnnotation;
import com.example.blog.entity.User;
import com.example.blog.service.IUserService;
import com.example.blog.util.JsonResult;
import com.example.blog.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController extends BaseController{
    @Autowired
    private IUserService userService;

    @RequestMapping("reg")
    //加上此注解 代表要对此接口记录日志
    @LogAnnotation(module="用户",operator="注册")
    public JsonResult<Void> reg(User user){
        userService.reg(user);
        return JsonResult.getResponseResult("注册成功");
    }

    @RequestMapping("login")
    @LogAnnotation(module="用户",operator="登录")
    public JsonResult<User> login(User user){
        User result = userService.login(user);
        return JsonResult.getResponseResult(result);
    }

    @RequestMapping("userinfo")
    @LogAnnotation(module="用户",operator="获取用户信息")
    public JsonResult<User> getUserInfo(@RequestHeader(value = "token") String token){
        //User user = userService.findUserByToken(token);
        //从ThreadLocal获取信息
        User user = UserThreadLocal.get();
        return JsonResult.getResponseResult(user);
    }
}
