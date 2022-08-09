package com.example.GetInLine.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("spring.thymeleaf3")
public class ForThymeleafProperties {

    private final boolean decoupledLogic;

}//end of class
