package com.example.GetInLine.controller;

import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.dto.EventDTO;
import com.example.GetInLine.service.EventService;
import com.example.GetInLine.service.PlaceService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@DisplayName("View 컨트롤러 - 이벤트")
@WebMvcTest(
        controllers = EventController.class,

        //아래의 두 줄의 인자가 왜 더 필요한지에 대해서는 BaseErrorControllerTest.java 파일에
        //설명해 두었다.
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
)
class EventControllerTest {

    private final MockMvc mvc;

    @MockBean
    private EventService eventService;

    public EventControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }




    @DisplayName("[view][GET] 이벤트 리스트 페이지")
    @Test
    void givenNothing_whenRequestingEventsPage_thenReturnsEventsPage() throws Exception {
        // Given
        given(eventService.getEvents(any())).willReturn(List.of());

        // When & Then
        mvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/index"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("events"));
        then(eventService).should().getEvents(any());
    }//func





    @DisplayName("[view][GET] 이벤트 리스트 페이지 - 커스텀 데이터")
    @Test
    void givenNothing_whenRequestingCustomEventsPage_thenReturnsEventsPage() throws Exception {
        //Given
        given(eventService.getEventViewResponse(any(), any(), any(), any(), any(), any())).willReturn(Page.empty());

        //When & Then
        mvc.perform(get("/events/custom"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/index"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("events"));
        then(eventService).should().getEventViewResponse(any(), any(), any(), any(), any(), any());
    }//func






    @DisplayName("[view][GET] 이벤트 리스트 페이지 - 커스텀 데이터 + 검색 파라미터")
    @Test
    void givenParams_whenRequestingCustomEventsPage_thenReturnsEventsPage() throws Exception {
        //Given
        String placeName = "배드민턴";
        String eventName = "오후";
        EventStatus eventStatus = EventStatus.OPENED;
        LocalDateTime eventStartDatetime = LocalDateTime.of(2021,1,1,0,0,0);
        LocalDateTime eventEndDatetime = LocalDateTime.of(2021,1,2,0,0,0);
        given(
                eventService.getEventViewResponse(
                        placeName, eventName, eventStatus, eventStartDatetime, eventEndDatetime, PageRequest.of(1,3)
                )
        ).willReturn(Page.empty());

        //When & Then
        mvc.perform(
                get("/events/custom")
                        .queryParam("placeName", placeName)
                        .queryParam("eventName", eventName)
                        .queryParam("eventStatus", eventStatus.name())
                        .queryParam("eventStartDatetime", eventStartDatetime.toString())
                        .queryParam("eventEndDatetime", eventEndDatetime.toString())
                        .queryParam("page", "1")
                        .queryParam("size", "3")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/index"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("events"));
        then(eventService).should().getEventViewResponse(
                placeName, eventName, eventStatus, eventStartDatetime, eventEndDatetime, PageRequest.of(1,3)
        );
    }//func





    @DisplayName("[view][GET] 이벤트 리스트 페이지 - 커스텀 데이터 + 검색 파라미터(장소명, 이벤트명 잘못된 입력)")
    @Test
    void givenWrongParams_whenRequestingCustomEventsPage_thenReturnsEventsPage() throws Exception {
        //Given
        String placeName = "배";
        String eventName = "오";
        EventStatus eventStatus = EventStatus.OPENED;
        LocalDateTime eventStartDatetime = LocalDateTime.of(2021,1,1,0,0,0);
        LocalDateTime eventEndDatetime = LocalDateTime.of(2021,1,2,0,0,0);
        /*given(
                eventService.getEventViewResponse(
                        placeName, eventName, eventStatus, eventStartDatetime, eventEndDatetime, PageRequest.of(1,3)
                )
        ).willReturn(Page.empty());*/

        //When & Then
        mvc.perform(
                        get("/events/custom")
                                .queryParam("placeName", placeName)
                                .queryParam("eventName", eventName)
                                .queryParam("eventStatus", eventStatus.name())
                                .queryParam("eventStartDatetime", eventStartDatetime.toString())
                                .queryParam("eventEndDatetime", eventEndDatetime.toString())
                                .queryParam("page", "1")
                                .queryParam("size", "3")
                )
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("error"))
                .andExpect(model().attributeDoesNotExist("events"));
        then(eventService).shouldHaveNoInteractions();
    }//func






    @DisplayName("[view][GET] 이벤트 세부 정보 페이지")
    @Test
    void givenEventId_whenRequestingEventDetailPage_thenReturnsEventDetailPage() throws Exception {
        // Given
        long eventId = 1L;
        given(eventService.getEvent(eventId)).willReturn(Optional.of(
           EventDTO.of(eventId, null, null, null, null, null, null, null, null, null, null)
        ));

        // When & Then
        mvc.perform(get("/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/detail"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("event"));
        then(eventService).should().getEvent(eventId);
    }//func





    @DisplayName("[view][GET] 이벤트 세부 정보 페이지 - 데이터 없음")
    @Test
    void givenNonexistentEventId_whenRequestingEventDetailPage_thenReturnsErrorPage() throws Exception {
        //Given
        long eventId = 0L;
        given(eventService.getEvent(eventId)).willReturn(Optional.empty());

        //When & Then
        mvc.perform(get("/events/"+eventId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("error"));
        then(eventService).should().getEvent(eventId);
    }//func

}//end of class





















