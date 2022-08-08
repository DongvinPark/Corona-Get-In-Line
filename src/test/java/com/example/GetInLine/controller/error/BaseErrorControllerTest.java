package com.example.GetInLine.controller.error;

import com.example.GetInLine.controller.error.BaseErrorController;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 에러")
@WebMvcTest(
        value = BaseErrorController.class,

        /*
        * 아래의 두 인자가 추가된 이유는, 스프링 본 코드에서 시큐리티를 적용시킬 경우 이게 테스트코드에도
        * 영향을 주기 때문이다. 시큐리티 관련 클래스들을 단위 테스트에서 제외시켜주지 않을 경우, 시큐리티
        * 기능까지 한꺼번에 포함시켜서 테스트 메서드들을 작동시키기 때문에 이는 원하는 메서드와 그 메서드의 입력과 출력에만
        * 집중하고 싶어하는 단위테스트의 작동과정을 복잡하고 까다롭게 만든다.
        * 이러한 현상을 제거하고자 아래의 두 인자를 추가하여 시큐리티 기능이 단위테스트 과정에서 배제되도록 한다.
        * */
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
)
class BaseErrorControllerTest {

    private final MockMvc mvc;

    public BaseErrorControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 에러 페이지 - 페이지 없음")
    @Test
    void givenWrongURI_whenRequestingPage_thenReturns404Error() throws Exception {
        //Given


        //When & Then
        mvc.perform(get("/wrong-uri"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}//end of class