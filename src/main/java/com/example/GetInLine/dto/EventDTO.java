package com.example.GetInLine.dto;


import com.example.GetInLine.constant.EventStatus;

import java.time.LocalDateTime;

public record EventDTO(
        Long id,
        Long placeId,
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
            Long placeId,
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
        return new EventDTO(id, placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime, currentNumberOfPeople, capacity, memo, createdAt, modifiedAt);
    }
}//end of class













