package com.internship.tmontica_admin.point;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

      private final PointService pointService;

      @PostMapping
      public ResponseEntity<String> test(@RequestBody Point point){
          pointService.updateUserPoint(point);
          return new ResponseEntity<>("dd", HttpStatus.OK);
      }

    //    @GetMapping("/{type}")
//    public ResponseEntity<PointTypeRespDTO> getPointByType(@PathVariable String type){
//        return new ResponseEntity<>(new PointTypeRespDTO(), HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<String> postPointLog(@RequestBody @Valid PointLogReqDTO pointLogReqDTO){
//        if(pointService.postPointLog(pointLogReqDTO)){
//            return new ResponseEntity<>(PointResponseMessage.POST_POINT_LOG_SUCCESS.getMessage(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(PointResponseMessage.POST_POINT_LOG_FAIL.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<String> deletePointLogByUserId(@PathVariable String userId){
//        if(pointService.withdrawPointLogByUserId(userId)){
//            return new ResponseEntity<>(PointResponseMessage.DELETE_POINT_LOG_SUCCESS.getMessage(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(PointResponseMessage.DELETE_POINT_LOG_FAIL.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
