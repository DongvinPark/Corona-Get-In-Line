package com.example.GetInLine.controller.api;


import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.dto.APIErrorResponse;
import com.example.GetInLine.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class APIEventController {

    @GetMapping("/events")
    public List<String> getEvents(){
        throw new GeneralException("테스트 메시지");
        //return List.of("event1", "event2");
    }

    @PostMapping("/events")
    public Boolean createEvent(){
        throw new RuntimeException("runtime 테스트 메시지");
        //return true;
    }

    @GetMapping("/events/{eventId}")
    public String getEvent(@PathVariable Integer eventId){
        return "event" + eventId;
    }

    @PutMapping("/events/{eventId}")
    public Boolean modifyEvent(@PathVariable Integer eventId){
        return true;
    }

    @DeleteMapping("/events/{eventId}")
    public Boolean removeEvent(@PathVariable Integer eventId){
        return true;
    }


}//end of class





