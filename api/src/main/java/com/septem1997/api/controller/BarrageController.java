package com.septem1997.api.controller;

import com.septem1997.api.entity.Barrage;
import com.septem1997.api.entity.User;
import com.septem1997.api.service.BarrageService;
import com.septem1997.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barrage")
public class BarrageController {


    @Autowired
    private BarrageService service;

    @GetMapping("/list")
    public List<Barrage> list(){
        return service.getLast10Records();
    }
}
