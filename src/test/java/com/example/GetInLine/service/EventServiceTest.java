package com.example.GetInLine.service;

import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.dto.EventDTO;
import com.example.GetInLine.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)//repository 패키지 내부의 EventRepository를 테스트에 활용하기 위한 조치.
class EventServiceTest {

    /*@BeforeEach//@SpringBootTest를 쓸 경우, 스프링 컨테이너를 run하는데 시간이 많이 걸려서 대신 이걸 씀.
    void setUp(){
        sut = new EventService();
    }*///Mockito 기능을 쓰게 되면서 여기는 필요 없게 됨.

    //@Mock 붙어 있는 eventRepository를, @InjectMocks가 붙어 있는 EventService sut에다가 주입해 줌.
    @InjectMocks private EventService sut;
    @Mock private EventRepository eventRepository;

    @DisplayName("검색 조건 없이 이벤트 검색하면, 전체 결과를 출력하여 보여준다.")
    @Test
    void givenNothing_whenSearchingEvents_thenReturnsEntireEventList(){
        //Given : 아직 리포지토리 계층을 완전히 구현한 것이 아님에도,
        // eventRepository라는 Mock을 이용해서 테스트를 진행할 수 있게 해 준다.
        given(eventRepository.findEvents(null, null, null, null, null))
                .willReturn(List.of(
                        createEventDTO(1L, "오전 운동", true),
                        createEventDTO(1L, "오후 운동", false)
                ));

        //When
        List<EventDTO> list = sut.getEvents(null, null, null, null, null);

        //Then
        assertThat(list).hasSize(2);
        then(eventRepository).should().findEvents(null, null, null, null, null);
    }//func

    @DisplayName("검색 조건과 함께 이벤트 검색하면, 검색된 결과를 출력하여 보여준다.")
    @Test
    void givenSearchParams_whenSearchingEvents_thenReturnsEventList(){
        //Given
        Long placeId = 1L;
        String eventName = "오전 운동";
        EventStatus eventStatus = EventStatus.OPENED;
        LocalDateTime eventStartDatetime = LocalDateTime.of(2021, 1, 1, 0,0,0);
        LocalDateTime eventEndDatetime = LocalDateTime.of(2021, 1, 2, 0,0,0);
        given(eventRepository.findEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime))
                .willReturn(List.of(
                   createEventDTO(1L, "오전 운동", eventStatus, eventStartDatetime, eventEndDatetime)
                ));

        //When
        List<EventDTO> list = sut.getEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime);

        //Then
        assertThat(list)
                .hasSize(1)
                .allSatisfy(event -> {
                    assertThat(event)
                            .hasFieldOrPropertyWithValue("placeId", placeId)
                            .hasFieldOrPropertyWithValue("eventName", eventName)
                            .hasFieldOrPropertyWithValue("eventStatus", eventStatus);
                    assertThat(event.eventStartDatetime()).isAfterOrEqualTo(eventStartDatetime);
                    assertThat(event.eventStartDatetime()).isBeforeOrEqualTo(eventStartDatetime);
                });
        then(eventRepository).should().findEvents(placeId, eventName, eventStatus, eventStartDatetime, eventEndDatetime);
    }//func



    @DisplayName("이벤트 ID로 존재하는 이벤트를 조회하면, 해당 이벤트 정보를 출력하여 보여준다.")
    @Test
    void givenEventId_whenSearchingExistingEvent_thenReturnsEvent(){
        //Given
        long eventId = 1L;
        EventDTO eventDTO = createEventDTO(1L, "오전 운동", true);
        given(eventRepository.findEvent(eventId)).willReturn(Optional.of(eventDTO));

        //When
        Optional<EventDTO> result = sut.getEvent(eventId);

        //Then
        assertThat(result).hasValue(eventDTO);
        then(eventRepository).should().findEvent(eventId);
    }//func




    @DisplayName("이벤트 ID로 존재하지 않는 이벤트를 조회하면, 비어 있는 이벤트 정보를 출력하여 보여준다.")
    @Test
    void givenEventId_whenSearchingNonexistentEvent_thenReturnsEmptyOptional(){
        //Given
        long eventId = 2L;
        given(eventRepository.findEvent(eventId)).willReturn(Optional.empty());

        //When
        Optional<EventDTO> result = sut.getEvent(eventId);

        //Then
        assertThat(result).isEmpty();
        then(eventRepository).should().findEvent(eventId);
    }//func



    @DisplayName("이벤트 정보를 주면, 이벤트를 생성하고 결과를 true로 보여준다.")
    @Test
    void givenEvent_whenCreating_thenCreatesEventAndReturnsTure(){
        //Given
        EventDTO dto = createEventDTO(1L, "오후 운동", false);
        given(eventRepository.insertEvent(dto)).willReturn(true);

        //When
        boolean result = sut.createEvent(dto);

        //Then
        assertThat(result).isTrue();
        then(eventRepository).should().insertEvent(dto);
    }//func





    @DisplayName("이벤트 정보를 주지 않으면, 생성 중단하고 결과를 false로 보여준다.")
    @Test
    void givenNothing_whenCreating_thenAbortCreatingAndReturnsFalse(){
        //Given
        given(eventRepository.insertEvent(null)).willReturn(false);

        //When
        boolean result = sut.createEvent(null);

        //Then
        assertThat(result).isFalse();
        then(eventRepository).should().insertEvent(null);
    }//func




    @DisplayName("이벤트 ID와 정보를 주면, 이벤트 정보를 변경하고 결과를 true로 보여준다.")
    @Test
    void givenEventIdAndItsInfo_whenModifying_thenModifiesEventAndReturnsTrue(){
        //Given
        long eventId = 1L;
        EventDTO dto = createEventDTO(1L, "오후 운동", false);
        given(eventRepository.updateEvent(eventId, dto)).willReturn(true);

        //When
        boolean result = sut.modifyEvent(eventId, dto);

        //Then
        assertThat(result).isTrue();
        then(eventRepository).should().updateEvent(eventId, dto);
    }//func





    @DisplayName("이벤트 ID를 주지 않으면, 이벤트 정보를 변경을 중단하고 결과를 false로 보여준다.")
    @Test
    void givenNoEventId_whenModifying_thenAbortsModifyingAndReturnsFalse(){
        //Given
        EventDTO dto = createEventDTO(1L, "오후 운동", false);
        given(eventRepository.updateEvent(null, dto)).willReturn(false);

        //When
        boolean result = sut.modifyEvent(null, dto);

        //Then
        assertThat(result).isFalse();
        then(eventRepository).should().updateEvent(null, dto);
    }//func





    @DisplayName("이벤트 ID만 주고 변경할 정보를 주지 않으면, 이벤트 정보를 변경을 중단하고 결과를 false로 보여준다.")
    @Test
    void givenEventIdOnly_whenModifying_thenAbortsModifyingAndReturnsFalse(){
        //Given
        long eventId = 1L;
        given(eventRepository.updateEvent(eventId, null)).willReturn(false);

        //When
        boolean result = sut.modifyEvent(eventId, null);

        //Then
        assertThat(result).isFalse();
        then(eventRepository).should().updateEvent(eventId, null);
    }//func




    @DisplayName("이벤트 ID를 주면, 이벤트 정보를 삭제하고 결과를 true로 보여준다.")
    @Test
    void givenEventId_whenDeleting_thenDeletesEventAndReturnsTrue(){
        //Given
        long eventId = 1L;
        given(eventRepository.deleteEvent(eventId)).willReturn(true);

        //When
        boolean result = sut.removeEvent(eventId);

        //Then
        assertThat(result).isTrue();//이 부분은 실제 결과가 내가 원하는 결과와 일치하는지를 검증하기 위한 것이다.
        then(eventRepository).should().deleteEvent(eventId);//이 부분은 파라미터와 호출이 정확하게 된 것인지를 검증하기 위한 것이다.
    }//func



    @DisplayName("이벤트 ID를 주지 않으면, 삭제를 중단하고 결과를 false로 보여준다.")
    @Test
    void givenNothing_whenDeleting_thenAbortsDeletingAndReturnsFalse(){
        //Given
        given(eventRepository.deleteEvent(null)).willReturn(false);

        //When
        boolean result = sut.removeEvent(null);

        //Then
        assertThat(result).isFalse();
        then(eventRepository).should().deleteEvent(null);
    }//func



    //>>>>> Helper Methods <<<<<

    private EventDTO createEventDTO(long placeId, String eventName, boolean isMorning){
        String hourStart = isMorning ? "09" : "13";
        String hourEnd = isMorning ? "12" : "16";

        return createEventDTO(
          placeId,
          eventName,
          EventStatus.OPENED,
          LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourStart)),
          LocalDateTime.parse("2021-01-01T%s:00:00".formatted(hourEnd))
        );
    }//func





    private EventDTO createEventDTO(
           long placeId,
           String eventName,
           EventStatus eventStatus,
           LocalDateTime eventStartDateTime,
           LocalDateTime eventEndDateTime
    ){
        return EventDTO.of(
                placeId,
                eventName,
                eventStatus,
                eventStartDateTime,
                eventEndDateTime,
                0,
                24,
                "마스크 꼭 착용하세요",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }//func

}//end of class













