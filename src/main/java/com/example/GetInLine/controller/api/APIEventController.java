package com.example.GetInLine.controller.api;


import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.dto.APIDataResponse;
import com.example.GetInLine.dto.APIErrorResponse;
import com.example.GetInLine.dto.EventRequest;
import com.example.GetInLine.dto.EventResponse;
import com.example.GetInLine.exception.GeneralException;
import com.example.GetInLine.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class APIEventController {

    private final EventService eventService;

    @GetMapping("/events")
    public APIDataResponse<List<EventResponse>> getEvents(
            @Positive Long placeId,
            @Size(min=2) String eventName,
            EventStatus eventStatus,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStartDatetime,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventEndDatetime
    ){
        /*return APIDataResponse.of(
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
        );*/ // EventService eventService;를 사용하게 되면서 이 부분은 필요 없게 되었다.

        //eventService.getEvents(...)의 리턴 결과는 List<EventDTO>이므로 이를 List<EventResponse>로 타입변환을
        //해 줘야 한다. 이러한 타입 변환의 책임을 컨트롤러 계층에 전부 포함시키는 것은 컨트롤러가 비대해질 수 있는 위험이 있다.
        //EventDTO는 D.T.O.이기 때문에 자주 바뀌어서는 안 되므로, 필요로 하는 변환 로직은 EventResponse에서 담당하도록 한다.
        List<EventResponse> responses = eventService
                .getEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime)
                .stream().map(EventResponse::from).toList();

        return APIDataResponse.of(responses);
    }//func

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events")
    public APIDataResponse<Void> createEvent(@Valid @RequestBody EventRequest eventRequest){
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





