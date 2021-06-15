package com.septem1997.api.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtConfig {
    /*
     * 根据身份ID标识，生成Token
     */
    private static final String SECRET = "banana";
    private static final Integer EXPIRE = 7200;
    public static final String HEADER = "token";

    public String getToken (String identityId){
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + EXPIRE * 1000);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(identityId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
    /*
     * 获取 Token 中注册信息
     */
    public Claims getTokenClaim (String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /*
     * Token 是否过期验证
     */
    public boolean isTokenExpired (Date expirationTime) {
        return expirationTime.before(new Date());
    }
}