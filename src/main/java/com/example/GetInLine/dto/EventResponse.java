package com.example.GetInLine.dto;


import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.domain.Place;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        PlaceDTO placeDTO,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDatetime,
        LocalDateTime eventEndDatetime,
        Integer currentNumberOfPeople,
        Integer capacity,
        String memo
) {

    public static EventResponse of(
            Long id,
            PlaceDTO placeDTO,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Integer currentNumberOfPeople,
            Integer capacity,
            String memo
    ){
        return new EventResponse(id, placeDTO, eventName, eventStatus, eventStartDatetime, eventEndDatetime, currentNumberOfPeople, capacity, memo);
    }



    public static EventResponse from(EventDTO eventDTO){
        if(eventDTO == null) { return null; }
        return EventResponse.of(
                eventDTO.id(),
                eventDTO.placeDTO(),
                eventDTO.eventName(),
                eventDTO.eventStatus(),
                eventDTO.eventStartDatetime(),
                eventDTO.eventEndDatetime(),
                eventDTO.currentNumberOfPeople(),
                eventDTO.capacity(),
                eventDTO.memo()
        );
    }//func



    public static EventResponse empty(PlaceDTO placeDTO){
        return EventResponse.of(null, placeDTO, null, null, null, null, null, null, null);
    }

    public String getPlaceName(){
        return this.placeDTO().placeName();
    }

}//end of class















