package com.septem1997.api.controller;

import com.septem1997.api.entity.User;
import com.septem1997.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService service;


    @GetMapping("/info")
    public Object getUserInfo(){
        return service.getCurrentUser();
    }

    @PostMapping("/login")
    public Object login(
            @RequestBody User user
    ){
        return service.login(user);
    }


    @PostMapping("/signup")
    public Object signup(
            @RequestBody User user
    ){
        return service.signup(user);
    }
}
