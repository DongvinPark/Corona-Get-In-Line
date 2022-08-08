package com.example.GetInLine.service;


import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.domain.Event;
import com.example.GetInLine.domain.Place;
import com.example.GetInLine.dto.EventDTO;
import com.example.GetInLine.dto.EventViewResponse;
import com.example.GetInLine.exception.GeneralException;
import com.example.GetInLine.repository.EventRepository;
import com.example.GetInLine.repository.PlaceRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Transactional
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;




    @Transactional(readOnly = true)
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





    @Transactional(readOnly = true)
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





    @Transactional(readOnly = true)
    public Optional<EventDTO> getEvent(Long eventId){
        try{
            return eventRepository.findById(eventId).map(EventDTO::of);
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func





    @Transactional(readOnly = true)
    public Page<EventViewResponse> getEvent(Long placeId, Pageable pageable){
        try{
            Place place = placeRepository.getById(placeId);
            Page<Event> eventPage = eventRepository.findByPlace(place, pageable);

            return new PageImpl<>(
                    eventPage.getContent()
                            .stream()
                            .map(event -> EventViewResponse.from(EventDTO.of(event)))
                            .toList(),
                    eventPage.getPageable(),
                    eventPage.getTotalElements()
            );
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR);
        }
    }//func





    public boolean upsertEvent(EventDTO eventDTO){
        try{
            if(eventDTO.id() != null) {
                return modifyEvent(eventDTO.id(), eventDTO);
            }
            else{
                return createEvent(eventDTO);
            }
        }
        catch (Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func






    public boolean createEvent(EventDTO eventDTO){
        try{
            if(eventDTO == null) { return false; }

            /*
            * 원래코드에서는 여기가
            * Place place = placeRepository.getById(eventDTO.placeDTO().id());
            * 라고 써 있었다.
            * 그러나, 실제로 스프링 부트 앱을 작동시켜본 결과, HibernateException이 발생했다.
            * 발생된 예외의 내용은 아래와 같았다.
            * com.example.GetInLine.exception.GeneralException: Data access error - Data access error - Generation of HibernateProxy instances at runtime is not allowed when the configured BytecodeProvider is 'none'; your model requires a more advanced BytecodeProvider to be enabled.;
            * 내용인 즉, BydecodeProvider가 node인 상태에서는 HibernateProxy를 런타임 중에 새로
            * 생성하는 것이 금지 돼 있어서 그렇다는 것이었다.
            *
            * 프록시는 DB 조회시 성능을 끌어올리기 위해 사용되는 기술이다.
            * getById()가 바로 그 프록시 객체을 통해서 데이터를 얻어오는 대표적인 메서드이다.
            * 프록시는 원본 자바 엔티티 객체의 참조를 담고 있으며, 지연 로딩(Lazy)을 위한 핵심기술이다.
            *
            * 비즈니스 로직에 따라서 특정 정보가 항상 즉시 필요한 것은 아닌 상황에 있는데, 이 때마다
            * 실제 엔티티 객체를 통해 DB에 쿼리를 날릴 경우 낭비가 발생한다.
            *
            * 이러한 낭비를 줄이고자 JPA에 도입된 기술이 Proxy와 Lazy로딩이다.
            *
            * 이것에 대한 자세한 공부는 따로 해야 할 것이지만, 어찌되었든 현재의 코드에서는
            * 프록시 객체를 런타임에 생성하는 것이 JPA와 Hibernate의 내부 로직에 의해서 금지된 상태였다.
            *
            * 따라서, 프록시 객체를 사용하는 getById()메서드를 사용하지 않고, 바로 실제 엔티티 객체를
            * 사용하여 DB에 쿼리를 날리는 findById()메서드를 사용하도록 코드를 변경하였다.
            * 그리고, palceRepository.findById()메서드는 Optional<Place>를 반환하기 때문에,
            * eventRepository.save(eventDTO.toEntity(place.get()));을 통해
            * Optional<Place> 객체에서 Place라는 엔티티 객체를 꺼내서 활용할 수 있게 했다.
            * 이렇게 해주자 비로소 이벤트를 추가하고 DB에 기록하는 것이 실제로 동작하게 되었다.
            * */
            Optional<Place> place = placeRepository.findById(eventDTO.placeDTO().id());

            if(place.isEmpty()){
                throw new GeneralException(ErrorCode.NOT_FOUND);
            }
            else{
                eventRepository.save(eventDTO.toEntity(place.get()));
                return true;
            }

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
