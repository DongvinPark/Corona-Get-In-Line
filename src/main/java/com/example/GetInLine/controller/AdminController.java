package com.example.GetInLine.controller;

/*
* 이 컨트롤러는 뷰를 리턴한다.
* */

import com.example.GetInLine.constant.AdminOperationStatus;
import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.constant.PlaceType;
import com.example.GetInLine.domain.Event;
import com.example.GetInLine.domain.Place;
import com.example.GetInLine.dto.*;
import com.example.GetInLine.exception.GeneralException;
import com.example.GetInLine.service.EventService;
import com.example.GetInLine.service.PlaceService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final EventService eventService;
    private final PlaceService placeService;





    @GetMapping("/places")
    public ModelAndView adminPlaces(
            @QuerydslPredicate(root = Place.class) Predicate predicate
    ){
        List<PlaceResponse> places = placeService.getPlaces(predicate)
                .stream().map(PlaceResponse::from).toList();

        return new ModelAndView(
                "admin/places",
                Map.of(
                        "places", places,
                        "placeTypeOption", PlaceType.values()
                )
        );
    }//fund





    @GetMapping("/places/{placeId}")
    public ModelAndView adminPlaceDetail(
            @PathVariable Long placeId,
            @PageableDefault Pageable pageable
    ){
        PlaceResponse place = placeService.getPlace(placeId)
                .map(PlaceResponse::from)
                .orElseThrow( ()-> new GeneralException(ErrorCode.NOT_FOUND));
        Page<EventViewResponse> events = eventService.getEvent(placeId, pageable);

        return new ModelAndView(
                "admin/place-detail",
                Map.of(
                        "adminOperationStatus", AdminOperationStatus.MODIFY,
                        "place", place,
                        "events", events,
                        "placeTypeOption", PlaceType.values()
                )
        );
    }//func





    //Model 타입은 왜 존재하는 거지??
    @GetMapping("/places/new")
    public String newPlace(Model model){
        model.addAttribute("adminOperationStatus", AdminOperationStatus.CREATE);
        model.addAttribute("placeTypeOption", PlaceType.values());

        return "admin/place-detail";
    }//func




    //RedirectAttributes 는 왜 쓰는 거지??
    @ResponseStatus(HttpStatus.SEE_OTHER)
    @PostMapping("/places")
    public String upsertPlace(
            @Valid PlaceRequest placeRequest,
            RedirectAttributes redirectAttributes
    ){
        AdminOperationStatus status = placeRequest.id() != null ?
                AdminOperationStatus.MODIFY : AdminOperationStatus.CREATE;
        placeService.upsertPlace(placeRequest.toDTO());

        log.info("플레이스리퀘스트의 id : " + placeRequest.id());
        log.info("현재 AdminStatus : " + status.getMessage());

        log.info("어드민 컨트롤러 내부의 플레이스서비스 작업 완료.");

        redirectAttributes.addFlashAttribute("adminOperationStatus", status);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/places");

        log.info("어드민 컨트롤러 내부 리디렉트어트리뷰트 작업완료");

        return "redirect:/admin/confirm";
    }//func





    @ResponseStatus(HttpStatus.SEE_OTHER)
    @GetMapping("/places/{placeId}/delete")
    public String deletePlace(
            @PathVariable Long placeId,
            RedirectAttributes redirectAttributes
    ){
        placeService.removePlace(placeId);

        redirectAttributes.addFlashAttribute("adminOperationStatus", AdminOperationStatus.DELETE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/places");

        return "redirect:/admin/confirm";
    }//func




    @GetMapping("/places/{placeId}/newEvent")
    public String newEvent(@PathVariable Long placeId, Model model){
        EventResponse event = placeService.getPlace(placeId)
                .map(EventResponse::empty)
                .orElseThrow( ()-> new GeneralException(ErrorCode.NOT_FOUND) );

        model.addAttribute( "adminOperationStatus", AdminOperationStatus.CREATE);
        model.addAttribute( "eventStatusOption", EventStatus.values());
        model.addAttribute( "event", event);

        return "admin/event-detail";
    }//func




    @ResponseStatus(HttpStatus.SEE_OTHER)
    @PostMapping("/places/{placeId}/events")
    public String upsertEvent(
            @Valid EventRequest eventRequest,
            @PathVariable Long placeId,
            RedirectAttributes redirectAttributes
    ){
        AdminOperationStatus status = eventRequest.id() != null ?
                AdminOperationStatus.MODIFY : AdminOperationStatus.CREATE;
        eventService.upsertEvent( eventRequest.toDTO(PlaceDTO.idOnly(placeId)) );

        redirectAttributes.addFlashAttribute("adminOperationStatus", status);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/places/" + placeId);

        return "redirect:/admin/confirm";
    }//func




    @ResponseStatus(HttpStatus.SEE_OTHER)
    @GetMapping("/events/{eventId}/delete")
    public String deleteEvent(
            @PathVariable Long eventId,
            RedirectAttributes redirectAttributes
    ){
        eventService.removeEvent(eventId);

        redirectAttributes.addFlashAttribute("adminOperationStatus", AdminOperationStatus.DELETE);
        redirectAttributes.addFlashAttribute("redirectUrl", "/admin/events");

        return "redirect:/admin/confirm";
    }//func




    @GetMapping("/events")
    public ModelAndView adminEvents(
            @QuerydslPredicate(root = Event.class) Predicate predicate
            ){
        List<EventResponse> events = eventService.getEvents(predicate)
                .stream()
                .map(EventResponse::from)
                .toList();

        return new ModelAndView(
                "admin/events",
                Map.of(
                        "events", events,
                        "eventStatusOption", EventStatus.values()
                )
        );
    }//func




    @GetMapping("/events/{eventId}")
    public ModelAndView adminEventDetail(@PathVariable Long eventId){
        EventResponse event = eventService.getEvent(eventId)
                .map(EventResponse::from)
                .orElseThrow( ()-> new GeneralException(ErrorCode.NOT_FOUND) );

        return new ModelAndView(
                "admin/event-detail",
                Map.of(
                        "adminOperationStatus", AdminOperationStatus.MODIFY,
                        "event", event,
                        "eventStatusOption", EventStatus.values()
                )
        );
    }//func




    @GetMapping("/confirm")
    public String confirm(Model model){
        if(!model.containsAttribute("redirectUrl")){
            log.info("/confirm에서 redirectUrl이 없어서 BadRequest!!");
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
        return "admin/confirm";
    }//func

}//end of class




















