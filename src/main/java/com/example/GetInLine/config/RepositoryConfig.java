package com.example.GetInLine.config;


import com.example.GetInLine.repository.EventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
* EventRepository가 인터페이스여서 바로 인스턴스를 만들 수 없으므로,
* 인스턴스를 만들 수 있게 해주는 Bean을 만들어준다.
* 즉, 구현체인 EventRepository를 Bean으로 등록할 수 있는 방법을 스프링에게 가르쳐 준 것.
* */

@Configuration
public class RepositoryConfig {

    @Bean
    public EventRepository eventRepository(){
        return new EventRepository(){};
    }

}//end of class
