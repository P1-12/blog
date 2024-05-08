package com.example.blog.handler;

import com.example.blog.entity.User;
import com.example.blog.service.IUserService;
import com.example.blog.service.ex.NotLoginException;
import com.example.blog.service.ex.TokenErrorException;
import com.example.blog.util.UserThreadLocal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /** 判断是否请求接口路径是否为HandlerMethod（controller方法）不是就放行 */
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        String token = request.getHeader("token");
        /** 判断token是否为空，为空证明未登录 */
        if (token == null || token == ""){
            throw new NotLoginException();
        }

        /** 判断用户信息是否为空，为空证明未登录 */
        User user = userService.findUserByToken(token);
        if (user == null){
            throw new NotLoginException();
        }

        //登录验证成功，放行用户
        UserThreadLocal.put(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 如果不删除ThreadLocal中用完的信息，会有内存泄露风险
        UserThreadLocal.remove();
    }
}
