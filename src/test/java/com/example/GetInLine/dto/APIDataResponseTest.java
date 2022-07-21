package com.example.GetInLine.dto;

import com.example.GetInLine.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class APIDataResponseTest {

    @DisplayName("문자열 데이터가 주어지면, 표준 성공응답을 생성한다.")
    @Test
    void givenStringData_whenCreatingResponse_thenReturnSuccessfulResponse(){
        //Given
        String data = "test data";

        //When
        APIDataResponse<String> response = APIDataResponse.of(data);

        //Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("success", true)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.OK.getCode())
                .hasFieldOrPropertyWithValue("message", ErrorCode.OK.getMessage())
                .hasFieldOrPropertyWithValue("data", data);
    }//func


    @DisplayName("데이터가 없을 때, 비어있는 표준 성공 응답을 생성한다.")
    @Test
    void givenNothing_whenCreatingResponse_thenReturnsEmptySuccessfulResponse(){
        //Given


        //When
        APIDataResponse<String> response = APIDataResponse.empty();

        //Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("success", true)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.OK.getCode())
                .hasFieldOrPropertyWithValue("message", ErrorCode.OK.getMessage())
                .hasFieldOrPropertyWithValue("data", null);
    }//func

}//end of class