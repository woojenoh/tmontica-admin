package com.internship.tmontica_admin.statistic;

import com.internship.tmontica_admin.statistic.exception.StatisticValidException;
import com.internship.tmontica_admin.statistic.model.request.StatisticMenuByDateReqDTO;
import com.internship.tmontica_admin.statistic.model.request.StatisticSalesByAgeReqDTO;
import com.internship.tmontica_admin.statistic.model.response.StatisticMenuByDateRespDTO;
import com.internship.tmontica_admin.statistic.model.response.StatisticSalesByAgeRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @PostMapping("/menu")
    public ResponseEntity<StatisticMenuByDateRespDTO> getMenuByDate(@RequestBody @Valid StatisticMenuByDateReqDTO statisticMenuByDateReqDTO,
                                                                    BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new StatisticValidException("기간별 메뉴 판매량 요청", "폼 데이터가 올바르지 않습니다.", bindingResult);
        }

        StatisticMenuByDateRespDTO statisticMenuByDateRespDTO = statisticService.getMenuByDateData(statisticMenuByDateReqDTO);
        return new ResponseEntity<>(statisticMenuByDateRespDTO, HttpStatus.OK);
    }

    @PostMapping("/age")
    public ResponseEntity<StatisticSalesByAgeRespDTO> getSalesByAgeGroup(@RequestBody @Valid StatisticSalesByAgeReqDTO statisticSalesByAgeReqDTO,
                                                                    BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new StatisticValidException("연령대별 판매량 요청", "폼 데이터가 올바르지 않습니다.", bindingResult);
        }

        StatisticSalesByAgeRespDTO statisticSalesByAgeRespDTO = statisticService.getSalesByAgeData(statisticSalesByAgeReqDTO);
        return new ResponseEntity<>(statisticSalesByAgeRespDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> test(@RequestParam("test")String test) {

        //statisticService.makeTotalSalesByAgeGroupData();
        //statisticScheduler.makeTotalSalesByMenu();
        return new ResponseEntity<>("test", HttpStatus.OK);
    }
}
