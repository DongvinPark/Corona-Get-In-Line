package com.example.GetInLine.service;


import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.domain.Place;
import com.example.GetInLine.dto.EventDTO;
import com.example.GetInLine.dto.EventViewResponse;
import com.example.GetInLine.exception.GeneralException;
import com.example.GetInLine.repository.EventRepository;
import com.example.GetInLine.repository.PlaceRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;




    public List<EventDTO> getEvents (
            Predicate predicate
    ){
        try{
            return StreamSupport.stream(eventRepository.findAll(predicate).spliterator(), false)
                    .map(EventDTO::of)
                    .toList();
        }
        catch (Exception e){
            //외부 Data 관련 모듈 등에서 발생한 에러를 처리할 때, 그 에러가 어떤 종류의 에러인지를
            // 좀 더 명확하게 표현하기 위해서 ErrorCode.java 클래스에 DATA_ACCESS_ERROR를 추가해 줌.
            // 그리고, RuntimeException을 우리가 미리 정의해 놓은 ErroCode 상수를 이용해서 GeneralException으로
            // 바꿔서 던져주는 작업을 함.
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func





    public Page<EventViewResponse> getEventViewResponse(
            String placeName,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Pageable pageable
    ){
        try{
            return eventRepository.findEventViewPageBySearchParams(
                    placeName,
                    eventName,
                    eventStatus,
                    eventStartDatetime,
                    eventEndDatetime,
                    pageable
            );
        }//try
        catch (Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }//catch
    }//func





    public Optional<EventDTO> getEvent(Long eventId){
        try{
            return eventRepository.findById(eventId).map(EventDTO::of);
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func






    public boolean createEvent(EventDTO eventDTO){
        try{
            if(eventDTO == null) { return false; }
            Place place = placeRepository.findById(eventDTO.placeDTO().id())
                    .orElseThrow(()-> new GeneralException(ErrorCode.NOT_FOUND));
            eventRepository.save(eventDTO.toEntity(place));
            return true;
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func







    public boolean modifyEvent(Long eventId ,EventDTO eventDTO){
        try{
            if(eventId == null || eventDTO == null){ return false; }
            eventRepository.findById(eventId)
                    .ifPresent(event -> eventRepository.save(eventDTO.updateEntity(event)));
            return true;
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }







    public boolean removeEvent(Long eventId){
        try{
            if(eventId == null) { return false; }
            eventRepository.deleteById(eventId);
            return true;
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

}//end of class
