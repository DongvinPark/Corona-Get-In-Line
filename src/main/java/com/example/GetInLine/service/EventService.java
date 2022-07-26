package com.example.GetInLine.service;


import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.dto.EventDTO;
import com.example.GetInLine.exception.GeneralException;
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
        try{
            return eventRepository.findEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime);
        }
        catch (Exception e){
            //외부 Data 관련 모듈 등에서 발생한 에러를 처리할 때, 그 에러가 어떤 종류의 에러인지를
            // 좀 더 명확하게 표현하기 위해서 ErrorCode.java 클래스에 DATA_ACCESS_ERROR를 추가해 줌.
            // 그리고, RuntimeException을 우리가 미리 정의해 놓은 ErroCode 상수를 이용해서 GeneralException으로
            // 바꿔서 던져주는 작업을 함. 이 부분이 EventServiceTest에서 테스트로 구현돼 있음.
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func

    public Optional<EventDTO> getEvent(Long eventId){
        try{
            return eventRepository.findEvent(eventId);
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func

    public boolean createEvent(EventDTO eventDTO){
        try{
            return eventRepository.insertEvent(eventDTO);
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func

    public boolean modifyEvent(Long eventId ,EventDTO eventDTO){
        try{
            return eventRepository.updateEvent(eventId, eventDTO);
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

    public boolean removeEvent(Long eventId){
        try{
            return eventRepository.deleteEvent(eventId);
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }

}//end of class
