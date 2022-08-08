package com.example.GetInLine.controller;

import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("View 컨트롤러 - 인증")
@WebMvcTest(
        controllers = AuthController.class,

        //아래의 두 줄의 인자가 왜 더 필요한지에 대해서는 BaseErrorControllerTest.java 파일에
        //설명해 두었다.
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
)
class AuthControllerTest {

    private final MockMvc mvc;

    public AuthControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }






    @DisplayName("[view][GET] 로그인 페이지")
    @Test
    void givenNothing_whenRequestingLoginPage_thenReturnsLoginPage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("auth/login"));
    }//func






    @DisplayName("[view][GET] 어드민 회원 가입 페이지")
    @Test
    void givenNothing_whenRequestingSignUpPage_thenReturnsSignUpPage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("This is sign-up page.")))
                .andExpect(view().name("auth/sign-up"));
    }

}//end of class