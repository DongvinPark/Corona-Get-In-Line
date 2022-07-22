package com.example.GetInLine.controller.api;


import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.dto.APIDataResponse;
import com.example.GetInLine.dto.APIErrorResponse;
import com.example.GetInLine.dto.EventRequest;
import com.example.GetInLine.dto.EventResponse;
import com.example.GetInLine.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api")
@RestController
public class APIEventController {

    @GetMapping("/events")
    public APIDataResponse<List<EventResponse>> getEvents(){
        return APIDataResponse.of(
                List.of(
                        EventResponse.of(
                                1L,
                                "오후 운동",
                                EventStatus.OPENED,
                                LocalDateTime.of(2021,1,1,13,0,0),
                                LocalDateTime.of(2021,1,1,16,0,0),
                                0,
                                24,
                                "마스크 꼭 착용하세요"
                        )
                )
        );
    }//func

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events")
    public APIDataResponse<Void> createEvent(@RequestBody EventRequest eventRequest){
        return APIDataResponse.empty();
    }//func

    @GetMapping("/events/{eventId}")
    public APIDataResponse<EventResponse> getEvent(@PathVariable Long eventId){
        if(eventId.equals(2L)){
            return APIDataResponse.empty();
        }

        return APIDataResponse.of(
                EventResponse.of(
                        1L,
                        "오후 운동",
                        EventStatus.OPENED,
                        LocalDateTime.of(2021,1,1,13,0,0),
                        LocalDateTime.of(2021,1,1,16,0,0),
                        0,
                        24,
                        "마스크 꼭 착용하세요"
                )
        );
    }//func

    @PutMapping("/events/{eventId}")
    public APIDataResponse<Void> modifyEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest eventREquest
    ){
        return APIDataResponse.empty();
    }//func

    @DeleteMapping("/events/{eventId}")
    public APIDataResponse<Void> removeEvent(
            @PathVariable Long eventId
    ){
        return APIDataResponse.empty();
    }//func


}//end of class





