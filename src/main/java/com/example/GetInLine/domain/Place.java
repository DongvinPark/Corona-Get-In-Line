package com.example.GetInLine.domain;


import com.example.GetInLine.constant.PlaceType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Place {

    private Long id;

    private PlaceType placeType;
    private String placeName;
    private String address;
    private String phoneNumber;
    private Integer capacity;
    private String memo;

    private LocalDateTime createdAd;
    private LocalDateTime modifiedAt;

}
