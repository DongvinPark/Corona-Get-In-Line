package com.example.GetInLine.service;


import com.example.GetInLine.constant.ErrorCode;
import com.example.GetInLine.dto.PlaceDTO;
import com.example.GetInLine.dto.PlaceResponse;
import com.example.GetInLine.exception.GeneralException;
import com.example.GetInLine.repository.PlaceRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;



    @Transactional(readOnly = true)
    public List<PlaceDTO> getPlaces(Predicate predicate){
        try{
            return StreamSupport.stream( placeRepository.findAll(predicate).spliterator(), false )
                    .map(PlaceDTO::of)
                    .toList();
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func






    @Transactional(readOnly = true)
    public Optional<PlaceDTO> getPlace(Long placeId){
        try{
            return placeRepository.findById(placeId).map(PlaceDTO::of);
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func






    public boolean upsertPlace(PlaceDTO placeDTO){
        try{
            if(placeDTO.id()!=null){
                log.info("플레이스서비스 업서트 - 수정파트 진입");
                return modifyPlace(placeDTO.id(), placeDTO);
            }
            else{
                log.info("플레이스서비스 업서트 - 생성파트 진입");
                return createPlace(placeDTO);
            }
        }
        catch (Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func








    public boolean createPlace(PlaceDTO placeDTO){
        try{
            if(placeDTO == null) { return false; }
            placeRepository.save(placeDTO.toEntity());
            return true;
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func








    public boolean modifyPlace(Long placeId, PlaceDTO placeDTO){
        try{
            if(placeId == null || placeDTO == null){ return false; }
            placeRepository.findById(placeId)
                    .ifPresent(place -> placeRepository.save(placeDTO.updateEntity(place)));
            return true;
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func








    public boolean removePlace(Long placeId){
        try{
            if(placeId == null) { return false; }
            placeRepository.deleteById(placeId);
            return true;
        }
        catch(Exception e){
            throw new GeneralException(ErrorCode.DATA_ACCESS_ERROR, e);
        }
    }//func

}//end of class



















