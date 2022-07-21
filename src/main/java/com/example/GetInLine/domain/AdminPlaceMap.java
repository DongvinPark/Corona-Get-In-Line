package com.example.GetInLine.domain;


import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
public class AdminPlaceMap {

    private Long id;

    private Long adminId;
    private Long placeId;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
