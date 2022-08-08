package com.example.GetInLine.controller;


/*
* 이 컨트롤러는 뷰를 리턴한다.
* */

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }//func



    //@RequestHeader("referer")는 왜 사용된 것인가?
    @GetMapping("/logout")
    public String logout(
            @RequestHeader("referer") String referer, Model model
    ) {
        model.addAttribute("backUrl", referer);

        return "auth/logout";
    }//func



    @GetMapping("/sign-up")
    public String signUp() {
        return "auth/sign-up";
    }//func

}//End of class
