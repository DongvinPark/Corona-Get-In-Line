package com.example.GetInLine.dto;


import com.example.GetInLine.constant.EventStatus;

import java.time.LocalDateTime;

public record EventViewResponse(
        Long id,
        String placeName,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDatetime,
        LocalDateTime eventEndDatetime,
        Integer currentNumberOfPeople,
        Integer capacity,
        String memo
) {

    public EventViewResponse(Long id, String placeName, String eventName, EventStatus eventStatus, LocalDateTime eventStartDatetime, LocalDateTime eventEndDatetime, Integer currentNumberOfPeople, Integer capacity, String memo) {
        this.id = id;
        this.placeName = placeName;
        this.eventName = eventName;
        this.eventStatus = eventStatus;
        this.eventStartDatetime = eventStartDatetime;
        this.eventEndDatetime = eventEndDatetime;
        this.currentNumberOfPeople = currentNumberOfPeople;
        this.capacity = capacity;
        this.memo = memo;
    }



    public static EventViewResponse of(Long id, String placeName, String eventName, EventStatus eventStatus, LocalDateTime eventStartDatetime, LocalDateTime eventEndDatetime, Integer currentNumberOfPeople, Integer capacity, String memo) {
        return new EventViewResponse(
                id,
                placeName,
                eventName,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                currentNumberOfPeople,
                capacity,
                memo
        );
    }//func



    public static EventViewResponse from(EventDTO eventDTO){
        if(eventDTO == null){ return null; }
        return EventViewResponse.of(
                eventDTO.id(),
                eventDTO.placeDTO().placeName(),
                eventDTO.eventName(),
                eventDTO.eventStatus(),
                eventDTO.eventStartDatetime(),
                eventDTO.eventEndDatetime(),
                eventDTO.currentNumberOfPeople(),
                eventDTO.capacity(),
                eventDTO.memo()
        );
    }//func

}//end of class

















