package com.septem1997.api.service;

import com.septem1997.api.config.JwtConfig;
import com.septem1997.api.entity.User;
import com.septem1997.api.model.Result;
import com.septem1997.api.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.management.ServiceNotFoundException;
import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    @SneakyThrows
    public Result signup(User source){
        if (source.getUsername()==null||source.getPassword()==null){
            throw new ServiceUnavailableException("请输入账号或密码");
        }
        User userByUsername = repository.findUserByUsernameAndIsDeleteFalse(source.getUsername());
        if (userByUsername!=null){
            throw new ServiceUnavailableException("该名字已被占用");
        }
        source.setPassword(bCryptPasswordEncoder.encode(source.getPassword())); //对密码进行加密
        return Result.success(repository.save(source));
    }

    public User getCurrentUser(){
        ServletRequestAttributes ra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =  ra.getRequest();
        final String token = request.getHeader("token");
        final Claims tokenClaim = jwtConfig.getTokenClaim(token);
        return repository.findUserByUsernameAndIsDeleteFalse(tokenClaim.getSubject());
    }

    @SneakyThrows
    public User findOneById(Long id){
        final Optional<User> optional = repository.findById(id);
        if (optional.isPresent()){
            return optional.get();
        }else {
            throw new ServiceNotFoundException("找不到该记录");
        }
    }


    @SneakyThrows
    public HashMap<String,Object> login(User user) {
        if (user.getUsername()==null||user.getPassword()==null){
            throw new ServiceUnavailableException("请输入账号或密码");
        }
        User userByUsername = repository.findUserByUsernameAndIsDeleteFalse(user.getUsername());
        if (bCryptPasswordEncoder.matches(user.getPassword(),userByUsername.getPassword())){
            return new HashMap<>() {{
                put("token", jwtConfig.getToken(userByUsername.getUsername()));
            }};
        }else {
            throw new ServiceUnavailableException("账号或密码错误");
        }
    }
}
