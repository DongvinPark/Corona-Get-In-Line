package com.example.GetInLine.dto;


import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.domain.Event;
import com.example.GetInLine.domain.Place;

import java.time.LocalDateTime;
import java.util.Optional;

public record EventDTO(
        Long id,
        PlaceDTO placeDTO,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDatetime,
        LocalDateTime eventEndDatetime,
        Integer currentNumberOfPeople,
        Integer capacity,
        String memo,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static EventDTO of(
            Long id,
            PlaceDTO placeDTO,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Integer currentNumberOfPeople,
            Integer capacity,
            String memo,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ){
        return new EventDTO(id, placeDTO, eventName, eventStatus, eventStartDatetime, eventEndDatetime, currentNumberOfPeople, capacity, memo, createdAt, modifiedAt);
    }



    public static EventDTO of(Event event){
        return new EventDTO(
                event.getId(),
                PlaceDTO.of(event.getPlace()),
                event.getEventName(),
                event.getEventStatus(),
                event.getEventStartDatetime(),
                event.getEventEndDatetime(),
                event.getCurrentNumberOfPeople(),
                event.getCapacity(),
                event.getMemo(),
                event.getCreatedAt(),
                event.getModifiedAt()
        );
    }//func



    public Event toEntity(Place place){
        return Event.of(
                place,
                eventName,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                currentNumberOfPeople,
                capacity,
                memo
        );
    }//func



    public Event updateEntity(Event event){
        if(eventName != null){event.setEventName(eventName);}
        if(eventStatus != null){event.setEventStatus(eventStatus);}
        if(eventStartDatetime != null){event.setEventStartDatetime(eventStartDatetime);}
        if(eventEndDatetime != null){event.setEventEndDatetime(eventEndDatetime);}
        if(currentNumberOfPeople != null){event.setCurrentNumberOfPeople(currentNumberOfPeople);}
        if(capacity != null){event.setCapacity(capacity);}
        if(memo != null){event.setMemo(memo);}

        return event;
    }//func
}//end of class













