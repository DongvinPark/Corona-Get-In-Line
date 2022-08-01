package com.example.GetInLine.repository.querydsl;


import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.dto.EventViewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface EventRepositoryCustom {

    //아래의 파라미터들로 이벤트들을 검색할 수 있도록 쿼리 검색 메서드를 내가 만들어준다.
    //이렇게 할 수 있는게 JPA의 강점이다. 단, 복습은 필요하다.
    //또한, 이 메서드를 구현하는 구현체를 따로 만들어야 하는데,
    //그 구현체는 현재 클래스 이름에 Impl를 붙여줘야 한다. 그래야 querydsl에서 구현체로서 인식한다.
    Page<EventViewResponse> findEventViewPageBySearchParams(
            String placeName,
            String eventName,
            EventStatus EventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Pageable pageable
    );

}//end of class
