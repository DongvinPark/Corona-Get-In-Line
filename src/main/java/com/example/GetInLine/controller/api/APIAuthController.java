package com.example.GetInLine.controller.api;

import com.example.GetInLine.dto.APIDataResponse;
import com.example.GetInLine.dto.AdminRequest;
import com.example.GetInLine.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class APIAuthController {

    @PostMapping("/sign-up")
    public APIDataResponse<String> signUp(@RequestBody AdminRequest adminRequest){
        return APIDataResponse.empty();
    }

    @PostMapping("/login")
    public APIDataResponse<String> login(@RequestBody LoginRequest loginRequest){
        return APIDataResponse.empty();
    }

}//end of class
