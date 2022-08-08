package com.example.GetInLine.constant;

import com.example.GetInLine.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

//enum 클래스는 인스턴스를 만들어서 테스트하지는 않는다.

@DisplayName("에러 처리 - 에러 코드")
class ErrorCodeTest {

    //이 테스트는 아무래도, ErrorCode의 OK 상수에 대한 테스라고 봐야 한다.
    //그렇다면, OK외의 다른 상수 5개에 대해서도 아래의 테스트 코드를 반복해야 하는가?
    // 이러한 불편을 해소하기 위해서 'Parameterized Test'라는 기법을 대부분의 모던한 테스트 프레임워크에서 지원하고 있다.
    //물론, JUnit5에서도 지원한다.
    /*@DisplayName("예외를 받으면, 예외 메시지가 포함된 메시지 출력")
    @Test
    void givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage(){
        //Given
        Exception e = new Exception("This is test message.");

        //When
        String result = ErrorCode.OK.getMessage(e);

        //Then
        assertThat(result).isEqualTo("OK - This is test message.");
    }//func*/
    @DisplayName("예외를 받으면, 예외 메시지가 포함된 메시지 출력")
    @ParameterizedTest(name = "[{index}] {0} ===> {1}")
    @MethodSource//ParameterizedTest에서 입력 소스를 무엇으로 할 것인지를 명시해주는 어노테이션이다.
    void givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage(ErrorCode sut, String expected){
        //Given
        Exception e = new Exception("This is test message.");

        //When
        String actual = sut.getMessage(e);

        //Then
        assertThat(actual).isEqualTo(expected);
    }//func
    static Stream<Arguments> givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage(){
        return Stream.of(
                arguments(ErrorCode.OK , "OK - This is test message."),
                arguments(ErrorCode.BAD_REQUEST , "Bad request - This is test message."),
                arguments(ErrorCode.SPRING_BAD_REQUEST , "Spring-detected bad request - This is test message."),
                arguments(ErrorCode.VALIDATION_ERROR , "Validation error - This is test message."),
                arguments(ErrorCode.NOT_FOUND, "Requested resource is not found - This is test message."),
                arguments(ErrorCode.INTERNAL_ERROR , "Internal error - This is test message."),
                arguments(ErrorCode.SPRING_INTERNAL_ERROR , "Spring-detected internal error - This is test message."),
                arguments(ErrorCode.DATA_ACCESS_ERROR , "Data access error - This is test message.")
        );
    }//func







    @DisplayName("에러 메시지를 받으면, 해당 에러 메시지로 출력")
    @ParameterizedTest(name = "[{index}] \"{0}\" ===> \"{1}\"")
    @MethodSource//ParameterizedTest에서 입력 소스를 무엇으로 할 것인지를 명시해주는 어노테이션이다.
    void givenMessage_whenGettingMessage_thenReturnsMessage(String input, String expected){
        //Given

        //When
        String actual = ErrorCode.INTERNAL_ERROR.getMessage(input);

        //Then
        assertThat(actual).isEqualTo(expected);
    }//func
    static Stream<Arguments> givenMessage_whenGettingMessage_thenReturnsMessage(){
        return Stream.of(
                arguments(null, ErrorCode.INTERNAL_ERROR.getMessage()),
                arguments("", ErrorCode.INTERNAL_ERROR.getMessage()),
                arguments("    ", ErrorCode.INTERNAL_ERROR.getMessage()),
                arguments("This is error test message", "This is error test message")
        );
    }//func






    @DisplayName("toString() 호출 포맷 테스트")
    @Test
    void givenErrorCode_whenToString_thenReturnsSimplifiedToString(){
        //Given


        //When
        String result = ErrorCode.INTERNAL_ERROR.toString();

        //Then
        assertThat(result).isEqualTo("INTERNAL_ERROR (20000)");
    }//func






    @DisplayName("HttpStatus 에 대응하는 ErrorCode 찾기 - 정상")
    @ParameterizedTest(name = "[{index}] {0} ===> {1}")
    @MethodSource
    void givenHttpStatus_whenGettingErrorCode_thenReturnsErrorCode(
            HttpStatus httpStatus, ErrorCode expected
    ){
        //Given

        //When
        ErrorCode actual = ErrorCode.valueOf(httpStatus);

        //Then
        assertThat(actual).isEqualTo(expected);
    }//func
    static Stream<Arguments> givenHttpStatus_whenGettingErrorCode_thenReturnsErrorCode(){
        return Stream.of(
                //정의된 값들
                arguments(HttpStatus.OK ,ErrorCode.OK),
                arguments(HttpStatus.BAD_REQUEST ,ErrorCode.BAD_REQUEST),
                arguments(HttpStatus.INTERNAL_SERVER_ERROR ,ErrorCode.INTERNAL_ERROR),

                //정의되지 않은 값들
                arguments(HttpStatus.CONTINUE ,ErrorCode.OK),
                arguments(HttpStatus.ACCEPTED ,ErrorCode.OK),
                arguments(HttpStatus.MULTI_STATUS ,ErrorCode.OK),
                arguments(HttpStatus.MOVED_PERMANENTLY ,ErrorCode.OK),
                arguments(HttpStatus.CONFLICT ,ErrorCode.BAD_REQUEST),
                arguments(HttpStatus.EXPECTATION_FAILED ,ErrorCode.BAD_REQUEST),
                arguments(HttpStatus.BAD_GATEWAY ,ErrorCode.INTERNAL_ERROR),
                arguments(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED ,ErrorCode.INTERNAL_ERROR)
        );
    }





    @DisplayName("HttpStatus 에 대응하는 ErrorCode 찾기 - Null 처리")
    @Test
    void givenUnknownHttpStatus_whenGEttingErrorCode_thenReturnsErrorCode(){
        //Given
        HttpStatus nullStatus = null;

        //When
        Throwable t = catchThrowable(()-> ErrorCode.valueOf(nullStatus));

        //Then
        assertThat(t)
                .isInstanceOf(GeneralException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INTERNAL_ERROR)
                .hasMessage("HttpStatus is null.");
    }//func


}//end of class











