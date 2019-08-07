package com.internship.tmontica_admin.statistic;

import com.internship.tmontica_admin.statistic.exception.StatisticException;
import com.internship.tmontica_admin.statistic.exception.StatisticExceptionType;
import com.internship.tmontica_admin.statistic.model.request.StatisticMenuByDateReqDTO;
import com.internship.tmontica_admin.statistic.model.request.StatisticReqDTO;
import com.internship.tmontica_admin.statistic.model.request.StatisticSalesByAgeReqDTO;
import com.internship.tmontica_admin.statistic.model.response.StatisticMenuByDateRespDTO;
import com.internship.tmontica_admin.statistic.model.response.StatisticSalesByAgeRespDTO;
import com.internship.tmontica_admin.statistic.vo.SalesWithAgeData;
import com.internship.tmontica_admin.statistic.vo.SalesWithMenuData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticDao statisticDao;

    public StatisticMenuByDateRespDTO getMenuByDateData(StatisticMenuByDateReqDTO statisticMenuByDateReqDTO) {

        setDefaultDate(statisticMenuByDateReqDTO);
        Map<Integer, Integer> menuData = statisticDao.getSalesMenuDataByDate(statisticMenuByDateReqDTO)
                .stream().filter(v -> statisticMenuByDateReqDTO.getMenuIds().contains(v.getMenuId()))
                .collect(Collectors.groupingBy(SalesWithMenuData::getMenuId, Collectors.summingInt(SalesWithMenuData::getTotalPrice)));
        List<SalesWithMenuData> resultList = new ArrayList<>();
        menuData.keySet().forEach(v->resultList.add(new SalesWithMenuData(v, menuData.get(v))));
        return new StatisticMenuByDateRespDTO(resultList);
    }

    public StatisticSalesByAgeRespDTO getSalesByAgeData(StatisticSalesByAgeReqDTO statisticSalesByAgeReqDTO){

        setDefaultDate(statisticSalesByAgeReqDTO);
        Map<String, Integer> ageData = statisticDao.getSalesAgeGroupDataByDate(statisticSalesByAgeReqDTO)
                .stream().filter(v->statisticSalesByAgeReqDTO.getAgeGroups().contains(v.getAgeGroup()))
                .collect(Collectors.groupingBy(SalesWithAgeData::getAgeGroup, Collectors.summingInt(SalesWithAgeData::getTotalPrice)));
        List<SalesWithAgeData> resultList = new ArrayList<>();
        ageData.keySet().forEach(v->resultList.add(new SalesWithAgeData(v, ageData.get(v))));
        return new StatisticSalesByAgeRespDTO(resultList);
    }

    private void setDefaultDate(StatisticReqDTO statisticReqDTO) {

        if (statisticReqDTO.getStartDate().equals("")) {
            if (!statisticReqDTO.getEndDate().equals("")) {
                throw new StatisticException(StatisticExceptionType.INVALID_PARAMETER_OPTION);
            }
            statisticReqDTO.setStartDate(LocalDate.of(2019, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        if (statisticReqDTO.getEndDate().equals("")) {
            statisticReqDTO.setEndDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }

}
