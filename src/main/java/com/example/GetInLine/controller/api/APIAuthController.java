package com.example.GetInLine.controller.api;

import com.example.GetInLine.dto.APIDataResponse;
import com.example.GetInLine.dto.AdminRequest;
import com.example.GetInLine.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 뷰의 제어와 각종 CRUD 기능을 전부 다른 컨트롤러에서 담당하기 때문에
 * 필요가 없어진 컨트롤러임.
 * */

@Deprecated
//@RequestMapping("/api")
//@RestController
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
