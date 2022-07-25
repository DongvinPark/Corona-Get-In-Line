package com.example.GetInLine.repository;


import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.dto.EventDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository {

    //default를 쓴 이유 : 원래는 config 패키지의 RepositoryConfig.java 클래스 내부에서 구현을 다 해줘야 하는데
    //defalut를 써서 미리 구현을 해 놓으면 RepositoryConfig.java 내부를 깔끔하게 유지할 수 있다.
    //결론적으로, repository layer 구현이 완료되면 삭제해야 함.

    default List<EventDTO> findEvents(
        Long placeId,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDatetime,
        LocalDateTime eventEndDatetime
    ){ return List.of(); }
    default Optional<EventDTO> findEvent(Long eventId){ return Optional.empty(); }
    default boolean insertEvent(EventDTO eventDTO) {return false;}
    default boolean updateEvent(Long eventId, EventDTO eventDTO) {return false;}
    default boolean deleteEvent(Long eventId) {return false;}

}//end of class
