package com.internship.tmontica_admin.statistic;

import com.internship.tmontica_admin.menu.MenuDao;
import com.internship.tmontica_admin.menu.model.vo.MenuIdName;
import com.internship.tmontica_admin.statistic.datatype.AgeGroup;
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
    private final MenuDao menuDao;

    //** 메뉴별 매출액 정보를 sales_menu_data 테이블에 집계된 내용을 바탕으로 반환함 **//
    public StatisticMenuByDateRespDTO getMenuByDateData(StatisticMenuByDateReqDTO statisticMenuByDateReqDTO) {

        setDefaultDate(statisticMenuByDateReqDTO);

        // 기간에 따른 메뉴 매출에서 요청받은 메뉴 id를 필터링하고 menu Id별 판매정보를 매핑하는 맵
        Map<Integer, List<SalesWithMenuData>> menuFilterDataMap = statisticDao.getSalesMenuDataByDate(statisticMenuByDateReqDTO).stream()
                .filter(v -> statisticMenuByDateReqDTO.getMenuIds().contains(v.getMenuId()))
                .collect(Collectors.groupingBy(SalesWithMenuData::getMenuId));

        // 메뉴별 총 판매액으로 재구성
        List<SalesWithMenuData> resultList = new ArrayList<>();

        for(Integer key : menuFilterDataMap.keySet()){
            int totalPrice = 0;
            String menuName = "";
            for(SalesWithMenuData salesWithMenuData : menuFilterDataMap.get(key)){
                totalPrice += salesWithMenuData.getTotalPrice();
                menuName = salesWithMenuData.getMenuName();
            }
            resultList.add(new SalesWithMenuData(key, totalPrice, menuName));
        }

        // 아래로 집계에 잡히지않은 메뉴 요청시 매출액을 0으로 넘겨주는 로직
        Map<Integer, String> menuIdNameMap = menuDao.getAllMenuIdAndName().stream()
                .collect(Collectors.toMap(MenuIdName::getId, MenuIdName::getNameKo));
        List<Integer> noDataMenuIdList = statisticMenuByDateReqDTO.getMenuIds().stream()
                .filter(v->!menuFilterDataMap.keySet().contains(v))
                .filter(menuIdNameMap::containsKey)
                .collect(Collectors.toList());

        for(Integer menuId  : noDataMenuIdList){
            resultList.add(new SalesWithMenuData(menuId, 0, menuIdNameMap.get(menuId)));
        }

        return new StatisticMenuByDateRespDTO(resultList);
    }

    public StatisticSalesByAgeRespDTO getSalesByAgeData(StatisticSalesByAgeReqDTO statisticSalesByAgeReqDTO){

        setDefaultDate(statisticSalesByAgeReqDTO);

        // 연령대별 총매출액 매핑한 맵
        Map<String, Integer> ageData = statisticDao.getSalesAgeGroupDataByDate(statisticSalesByAgeReqDTO).stream()
                .filter(v->statisticSalesByAgeReqDTO.getAgeGroups().contains(v.getAgeGroup()))
                .collect(Collectors.groupingBy(SalesWithAgeData::getAgeGroup, Collectors.summingInt(SalesWithAgeData::getTotalPrice)));
        // 매핑 정보를 바탕으로 응답 객체생성
        List<SalesWithAgeData> resultList = new ArrayList<>();
        ageData.keySet().forEach(v->resultList.add(new SalesWithAgeData(v, ageData.get(v))));

        // 아래로 집계에 잡히지않은 연령대 요청시 매출액을 0으로 넘겨주는 로직
        List<String> noDataAgeGroupList = statisticSalesByAgeReqDTO.getAgeGroups().stream()
                .filter(v->!ageData.keySet().contains(v))
                .filter(AgeGroup::checkValidType)
                .collect(Collectors.toList());

        for(String noDataAgeGroup : noDataAgeGroupList){
            resultList.add(new SalesWithAgeData(noDataAgeGroup, 0));
        }

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
