package com.example.GetInLine.controller.error;


import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.dto.APIErrorResponse;
import com.example.GetInLine.exception.GeneralException;

import org.springframework.data.rest.webmvc.RepositoryRestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

//뷰가 아닌, API에 대한 예외 처리를 위한 것이다.
@RestControllerAdvice(annotations = {RestController.class,RepositoryRestController.class })//이 부분을 통해서 JSON바디의 api를 다루는 클래스만을 범위로 작동하게끔 한정지을 수 있다.
//원래 김은호 쌤의 깃허브에서는 RepositoryRestController.class 가 annotations = {...} 안에 추가로 들어 있었는데,
//이것은 build.gradle의 dependency{}내부에서
// implementation 'org.springframework.data:spring-data-rest-hal-explorer'
//이 부분을 추가했을 때 추가 되는 클래스다.
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    //ResponseEntityExceptionHandler는 스프링 mvc를 쓸 때 발생할 수 있는 예외들을 미리 모아놔서 편리하게
    // 처리할 수 있게 해주는 클래스다.

    //@Validated 을 이용한 예외처리를 실시할 때 발생하는 ConstraintViolationException을 처리하기 위한 ExceptionHandler다.
    @ExceptionHandler
    public ResponseEntity<Object> forValidatedException(ConstraintViolationException e, WebRequest request){
        return handleExceptionInternal(e, ErrorCode.VALIDATION_ERROR, request);
    }//func





    @ExceptionHandler
    public ResponseEntity<Object> general(GeneralException e, WebRequest request){
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }//func





    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request){
        return handleExceptionInternal(e, ErrorCode.INTERNAL_ERROR, request);
    }





    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, ErrorCode.valueOf(status), headers, status, request);
    }


    //>>>>> Helper Method Area <<<<<



    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorCode errorCode, WebRequest request){
        return handleExceptionInternal(e, errorCode, HttpHeaders.EMPTY, errorCode.getHttpStatus(), request);
    }



    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorCode errorCode, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(
                e,
                APIErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)),
                headers,
                status,
                request
        );
    }
}//end of class
