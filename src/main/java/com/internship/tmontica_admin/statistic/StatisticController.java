package com.internship.tmontica_admin.statistic;

import com.internship.tmontica_admin.statistic.exception.StatisticValidException;
import com.internship.tmontica_admin.statistic.model.request.StatisticMenuByDateRequestDTO;
import com.internship.tmontica_admin.statistic.model.request.StatisticOrderByUserAgentRequestDTO;
import com.internship.tmontica_admin.statistic.model.request.StatisticSalesByAgeGroupRequestDTO;
import com.internship.tmontica_admin.statistic.model.response.StatisticMenuByDateResponseDTO;
import com.internship.tmontica_admin.statistic.model.response.StatisticOrderByUserAgentResponseDTO;
import com.internship.tmontica_admin.statistic.model.response.StatisticSalesByAgeGroupResponseDTO;
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
    public ResponseEntity<StatisticMenuByDateResponseDTO> getMenuByDate(@RequestBody @Valid StatisticMenuByDateRequestDTO statisticMenuByDateReqDTO,
                                                                        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new StatisticValidException("기간별 메뉴 판매량 요청", "폼 데이터가 올바르지 않습니다.", bindingResult);
        }

        StatisticMenuByDateResponseDTO statisticMenuByDateResponseDTO = statisticService.getMenuByDateData(statisticMenuByDateReqDTO);
        return new ResponseEntity<>(statisticMenuByDateResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/age")
    public ResponseEntity<StatisticSalesByAgeGroupResponseDTO> getSalesByAgeGroup(@RequestBody @Valid StatisticSalesByAgeGroupRequestDTO statisticSalesByAgeGroupRequestDTO,
                                                                                  BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new StatisticValidException("연령대별 판매량 요청", "폼 데이터가 올바르지 않습니다.", bindingResult);
        }

        StatisticSalesByAgeGroupResponseDTO statisticSalesByAgeGroupResponseDTO = statisticService.getSalesByAgeData(statisticSalesByAgeGroupRequestDTO);
        return new ResponseEntity<>(statisticSalesByAgeGroupResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/useragent")
    public ResponseEntity<StatisticOrderByUserAgentResponseDTO> getOrderByUserAgent(@RequestBody @Valid StatisticOrderByUserAgentRequestDTO statisticOrderByUserAgentRequestDTO,
                                                                                   BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new StatisticValidException("기기별 주문건수 요청", "폼 데이터가 올바르지 않습니다.", bindingResult);
        }

        StatisticOrderByUserAgentResponseDTO statisticOrderByUserAgentResponseDTO = statisticService.getOrderByUserAgent(statisticOrderByUserAgentRequestDTO);
        return new ResponseEntity<>(statisticOrderByUserAgentResponseDTO, HttpStatus.OK);
    }

}
