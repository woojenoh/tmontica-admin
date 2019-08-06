package com.internship.tmontica_admin.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping
    public ResponseEntity<String> signIn(@RequestParam("test")String test) {

        statisticService.makeTotalSalesByAgeGroupData();
        return new ResponseEntity<>("test", HttpStatus.OK);
    }
}
