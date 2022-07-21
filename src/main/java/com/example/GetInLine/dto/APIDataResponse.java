package com.example.GetInLine.dto;

import com.example.GetInLine.constant.ErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class APIDataResponse<T> extends APIErrorResponse {

    private final /*Object*/ T data;

    private APIDataResponse(/*boolean success, Integer errorCode, String message, Object data*/ T data){
        //super(success, errorCode, message);
        super(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }


    public static <T> APIDataResponse<T> of(T data){
        return new APIDataResponse<>(data);
    }

    /*public static APIDataResponse of(boolean success, Integer errorCode, String message, Object data){
        return new APIDataResponse(success, errorCode, message, data);
    }*/

}//end of class
