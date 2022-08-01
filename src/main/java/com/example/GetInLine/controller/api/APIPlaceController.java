package com.example.GetInLine.controller.api;

import com.example.GetInLine.constant.PlaceType;
import com.example.GetInLine.dto.APIDataResponse;
import com.example.GetInLine.dto.PlaceRequest;
import com.example.GetInLine.dto.PlaceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class APIPlaceController {

    @GetMapping("/places")
    public APIDataResponse<List<PlaceResponse>> getPlaces(){
        return APIDataResponse.of(
                List.of(
                        PlaceResponse.of(
                                1L,
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





    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/places")
    public APIDataResponse<Void> createPlace(@RequestBody PlaceRequest placeRequest){
        return APIDataResponse.empty();
    }//func






    @GetMapping("/places/{placeId}")
    public APIDataResponse<PlaceResponse> getPlace(@PathVariable Long placeId){
        if(placeId.equals(2L)){
            return APIDataResponse.empty();
        }

        return APIDataResponse.of(PlaceResponse.of(
                placeId,
                PlaceType.COMMON,
                "랄라배드민턴장",
                "서울시 강남구 강남대로 1234",
                "010-1234-5678",
                30,
                "신장개업"
            )
        );
    }//func






    @PutMapping("/places/{placeId}")
    public APIDataResponse<Void> modifyPlace(
            @PathVariable Long placeId,
            @RequestBody PlaceRequest placeRequest
    ){
        return APIDataResponse.empty();
    }





    @DeleteMapping("/places/{placeId}")
    public APIDataResponse<Void> removePlace(@PathVariable Long placeId){
        return APIDataResponse.empty();
    }

}//end of class






