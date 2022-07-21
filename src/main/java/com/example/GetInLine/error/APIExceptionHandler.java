package com.example.GetInLine.error;


import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.dto.APIErrorResponse;
import com.example.GetInLine.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//뷰가 아닌, API에 대한 예외 처리를 위한 것이다.
@RestControllerAdvice(annotations = RestController.class)//이 부분을 통해서 JSON바디의 api를 다루는 클래스만을 범위로 작동하게끔 한정지을 수 있다.
public class APIExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<APIErrorResponse> general(GeneralException e){
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.isClientSideError() ?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status)
                .body(APIErrorResponse.of(
                        false, errorCode, errorCode.getMessage(e)
                ));
    }//func



    @ExceptionHandler
    public ResponseEntity<APIErrorResponse> exception(Exception e){
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status)
                .body(APIErrorResponse.of(
                        false, errorCode, errorCode.getMessage(e)
                ));
    }

}//end of class
