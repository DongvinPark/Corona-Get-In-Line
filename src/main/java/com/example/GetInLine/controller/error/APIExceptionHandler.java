package com.example.GetInLine.controller.error;


import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.dto.APIErrorResponse;
import com.example.GetInLine.exception.GeneralException;
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
@RestControllerAdvice(annotations = RestController.class)//이 부분을 통해서 JSON바디의 api를 다루는 클래스만을 범위로 작동하게끔 한정지을 수 있다.
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    //ResponseEntityExceptionHandler는 스프링 mvc를 쓸 때 발생할 수 있는 예외들을 미리 모아놔서 편리하게
    // 처리할 수 있게 해주는 클래스다.

    //@Validated 을 이용한 예외처리를 실시할 때 발생하는 ConstraintViolationException을 처리하기 위한 ExceptionHandler다.
    @ExceptionHandler
    public ResponseEntity<Object> forValidatedException(ConstraintViolationException e, WebRequest request){
        return getInternalResponseEntity(e, ErrorCode.VALIDATION_ERROR, HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, request);
    }//func





    @ExceptionHandler
    public ResponseEntity<Object> general(GeneralException e, WebRequest request){
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.isClientSideError() ?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return getInternalResponseEntity(e, errorCode, HttpHeaders.EMPTY, status, request);
        /*return ResponseEntity
                .status(status)
                .body(APIErrorResponse.of(
                        false, errorCode, errorCode.getMessage(e)
                ));*/
    }//func





    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request){
        return getInternalResponseEntity(e, ErrorCode.INTERNAL_ERROR, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, request);
        /*return ResponseEntity
                .status(status)
                .body(APIErrorResponse.of(
                        false, errorCode, errorCode.getMessage(e)
                ));*/
    }





    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorCode errorCode = status.is4xxClientError() ?
                ErrorCode.SPRING_BAD_REQUEST :
                ErrorCode.SPRING_INTERNAL_ERROR;

        return getInternalResponseEntity(ex, errorCode, headers, status, request);
    }


    //>>>>> Helper Method Area <<<<<
    private ResponseEntity<Object> getInternalResponseEntity(Exception e, ErrorCode errorCode, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(
                e,
                APIErrorResponse.of(false, errorCode.getCode(), errorCode.getMessage(e)),
                headers,
                status,
                request
        );
    }
}//end of class
