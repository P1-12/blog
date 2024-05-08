package com.example.blog.util;

import com.example.blog.entity.User;
import io.jsonwebtoken.*;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.util.Date;

public class JwtUtil {
    private static final long time = 60 * 60 * 60 * 24 * 30; //设置Token有效期为30天
    private static final String sign = "200447CPT12356@#$%^";
    public static String createJwt(User user){
        // 创建JwtBuilder对象
        JwtBuilder jwtBuilder = Jwts.builder();

        String jwtToken = jwtBuilder
                //设置头部header（固定信息）
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                //设置载荷Payload（不要存放敏感信息）
                .claim("uid",user.getUid())
                .claim("username",user.getUsername())
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + time)) //Token过期时间
                //设置签名signature
                .signWith(SignatureAlgorithm.HS256,sign) //设置加密算法和签名
                //将上述头部和载荷和签名连接成一个字符串
                .compact();
        return jwtToken;
    }

    public static Claims CheckToken(String token){
        try {
            JwtParser jwtParser = Jwts.parser();
            Jws<Claims> claimsJws = jwtParser.setSigningKey(sign).parseClaimsJws(token);
            return claimsJws.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
