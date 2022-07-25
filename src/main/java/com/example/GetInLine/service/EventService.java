package com.example.GetInLine.service;


import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.dto.EventDTO;
import com.example.GetInLine.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    public List<EventDTO> getEvents (
            Long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime
    ){
        return eventRepository.findEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime);
    }//func

    public Optional<EventDTO> getEvent(Long eventId){
        return eventRepository.findEvent(eventId);
    }//func

    public boolean createEvent(EventDTO eventDTO){
        return eventRepository.insertEvent(eventDTO);
    }//func

    public boolean modifyEvent(Long eventId ,EventDTO eventDTO){
        return eventRepository.updateEvent(eventId, eventDTO);
    }

    public boolean removeEvent(Long eventId){
        return eventRepository.deleteEvent(eventId);
    }

}//end of class
