package com.example.GetInLine.controller;


/*
* 이 컨트롤러는 뷰를 리턴한다.
* */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/sign-up")
    public String signup(){
        return "auth/sign-up";
    }

}//End of class
