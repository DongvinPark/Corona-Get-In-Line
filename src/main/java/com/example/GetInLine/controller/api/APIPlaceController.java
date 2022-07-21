package com.example.GetInLine.controller.api;

import com.example.GetInLine.constant.PlaceType;
import com.example.GetInLine.dto.APIDataResponse;
import com.example.GetInLine.dto.PlaceDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class APIPlaceController {

    @GetMapping("/places")
    public APIDataResponse<List<PlaceDTO>> getPlaces(){
        return APIDataResponse.of(
                List.of(
                        PlaceDTO.of(
                                PlaceType.COMMON,
                                "랄라배드민턴장",
                                "서울시 강남구 강남대로 1234",
                                "010-1234-5678",
                                30,
                                "신장개업"
                        )
                )//list.of
        );//return
    }//func

    @PostMapping("/places")
    public Boolean createPlace(){
        return true;
    }

    @GetMapping("/places/{placeId}")
    public APIDataResponse<PlaceDTO> getPlace(@PathVariable Integer placeId){
        if(placeId.equals(2)){
            return APIDataResponse.of(null);
        }

        return APIDataResponse.of(
                PlaceDTO.of(
                        PlaceType.COMMON,
                        "랄라배드민턴장",
                        "서울시 강남구 강남대로 1234",
                        "010-1234-5678",
                        30,
                        "신장개업"
                )//placeDTO.of
        );//apiDataResponse.of
    }//func

    @PutMapping("/places/{placeId}")
    public Boolean modifyPlace(@PathVariable Integer placeId){
        return true;
    }

    @DeleteMapping("/places/{placeId}")
    public Boolean removePlace(@PathVariable Integer placeId){
        return true;
    }

}//end of class





