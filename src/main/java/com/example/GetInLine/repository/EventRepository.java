package com.example.GetInLine.repository;


import com.example.GetInLine.constant.EventStatus;
import com.example.GetInLine.domain.Event;
import com.example.GetInLine.domain.QEvent;
import com.example.GetInLine.dto.EventDTO;
import com.example.GetInLine.repository.querydsl.EventRepositoryCustom;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
* 여기는 전체적으로 어떻게 작동하고, 무엇을 위해 존재하는 거지?? 복습이 필요하다.
* */
public interface EventRepository extends
        JpaRepository<Event, Long>,
        EventRepositoryCustom,
        QuerydslPredicateExecutor<Event>,
        QuerydslBinderCustomizer<QEvent>
{
    @Override
    default void customize(QuerydslBindings bindings, QEvent root){
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.place.placeName, root.eventName, root.eventStatus, root.eventStartDatetime, root.eventEndDatetime);
        bindings.bind(root.place.placeName).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.eventName).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.eventStartDatetime).first(ComparableExpression::goe);
        bindings.bind(root.eventEndDatetime).first(ComparableExpression::loe);
    }


}//end of class












