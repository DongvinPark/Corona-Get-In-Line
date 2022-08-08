package com.example.GetInLine.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
* 이 컨트롤러는 뷰를 리턴한다.
* */

@Controller
public class BaseController {

    @GetMapping("/")
    public String root() {
        return "redirect:/events";
    }//func


}//end of class
