package com.example.GetInLine.controller.error;


import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//컨트롤러 어드바이스의 등장이다. 뷰에 대한 예외 처리를 위한 것이다.
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler
    public ModelAndView general(GeneralException e){
        ErrorCode errorCode = e.getErrorCode();
        /*HttpStatus status = errorCode.isClientSideError() ?
                HttpStatus.BAD_REQUEST :
                HttpStatus.INTERNAL_SERVER_ERROR;*/

        return new ModelAndView(
                "error",
                Map.of(
                        "statusCode", errorCode.getHttpStatus().value(),
                        "errorCode", errorCode,
                        "message", errorCode.getMessage()
                ),
                errorCode.getHttpStatus()
        );
    }//func






    //general 예외에서조차도 잡지 못한 예외를 잡기 위한 파트
    @ExceptionHandler
    public ModelAndView exception(Exception e, HttpServletResponse response){
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        ErrorCode errorCode = httpStatus.is4xxClientError() ? ErrorCode.BAD_REQUEST : ErrorCode.INTERNAL_ERROR;

        if(httpStatus == HttpStatus.OK){
            httpStatus = HttpStatus.FORBIDDEN;
            errorCode = ErrorCode.BAD_REQUEST;
        }

        return new ModelAndView(
                "error",
                Map.of(
                        "statusCode", httpStatus.value(),
                        "errorCode", errorCode,
                        "message", errorCode.getMessage(e)
                ),
                errorCode.getHttpStatus()
        );
    }//func

}//end of class













