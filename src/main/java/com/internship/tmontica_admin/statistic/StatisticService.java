package com.internship.tmontica_admin.statistic;

import com.internship.tmontica_admin.menu.MenuDao;
import com.internship.tmontica_admin.menu.model.vo.MenuIdName;
import com.internship.tmontica_admin.statistic.datatype.AgeGroup;
import com.internship.tmontica_admin.statistic.datatype.UserAgentType;
import com.internship.tmontica_admin.statistic.exception.StatisticException;
import com.internship.tmontica_admin.statistic.exception.StatisticExceptionType;
import com.internship.tmontica_admin.statistic.model.request.StatisticMenuByDateRequestDTO;
import com.internship.tmontica_admin.statistic.model.request.StatisticOrderByUserAgentRequestDTO;
import com.internship.tmontica_admin.statistic.model.request.StatisticRequestDTO;
import com.internship.tmontica_admin.statistic.model.request.StatisticSalesByAgeGroupRequestDTO;
import com.internship.tmontica_admin.statistic.model.response.StatisticMenuByDateResponseDTO;
import com.internship.tmontica_admin.statistic.model.response.StatisticOrderByUserAgentResponseDTO;
import com.internship.tmontica_admin.statistic.model.response.StatisticSalesByAgeGroupResponseDTO;
import com.internship.tmontica_admin.statistic.vo.OrderWithUserAgentData;
import com.internship.tmontica_admin.statistic.vo.SalesWithAgeGroupData;
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

    //** 주어진 기간의 메뉴별 매출액 정보를 sales_menu_data 테이블에 집계된 데이터로 가공함 **//
    public StatisticMenuByDateResponseDTO getMenuByDateData(StatisticMenuByDateRequestDTO statisticMenuByDateReqDTO) {

        setDefaultDate(statisticMenuByDateReqDTO);

        // 기간에 따른 메뉴 매출에서 요청받은 메뉴 id를 필터링하고 menu Id별 판매정보를 매핑하는 맵
        Map<Integer, List<SalesWithMenuData>> menuFilterDataMap = statisticDao.getSalesMenuDataByDate(statisticMenuByDateReqDTO).stream()
                .filter(v -> statisticMenuByDateReqDTO.getMenuIdList().contains(v.getMenuId()))
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
        List<Integer> noDataMenuIdList = statisticMenuByDateReqDTO.getMenuIdList().stream()
                .filter(v->!menuFilterDataMap.keySet().contains(v))
                .filter(menuIdNameMap::containsKey)
                .collect(Collectors.toList());

        for(Integer menuId  : noDataMenuIdList){
            resultList.add(new SalesWithMenuData(menuId, 0, menuIdNameMap.get(menuId)));
        }

        return new StatisticMenuByDateResponseDTO(resultList);
    }

    //** 주어진 기간의 연령별 매출액 정보를 sales_agegroup_data 테이블에 집계된 데이터로 가공함 **//
    public StatisticSalesByAgeGroupResponseDTO getSalesByAgeData(StatisticSalesByAgeGroupRequestDTO statisticSalesByAgeGroupRequestDTO){

        setDefaultDate(statisticSalesByAgeGroupRequestDTO);

        // 연령대별 총매출액 맵
        Map<String, Integer> salesWithageGroupMap = statisticDao.getSalesAgeGroupDataByDate(statisticSalesByAgeGroupRequestDTO).stream()
                .filter(v-> statisticSalesByAgeGroupRequestDTO.getAgeGroupList().contains(v.getAgeGroup()))
                .collect(Collectors.groupingBy(SalesWithAgeGroupData::getAgeGroup, Collectors.summingInt(SalesWithAgeGroupData::getTotalPrice)));
        // 매핑 정보를 바탕으로 응답 객체생성
        List<SalesWithAgeGroupData> resultList = new ArrayList<>();
        salesWithageGroupMap.keySet().forEach(v->resultList.add(new SalesWithAgeGroupData(v, salesWithageGroupMap.get(v))));

        // 아래로 집계에 잡히지않은 연령대 요청시 매출액을 0으로 넘겨주는 로직
        List<String> noDataAgeGroupList = statisticSalesByAgeGroupRequestDTO.getAgeGroupList().stream()
                .filter(v->!salesWithageGroupMap.keySet().contains(v))
                .filter(AgeGroup::checkValidType)
                .collect(Collectors.toList());

        for(String noDataAgeGroup : noDataAgeGroupList){
            resultList.add(new SalesWithAgeGroupData(noDataAgeGroup, 0));
        }

        return new StatisticSalesByAgeGroupResponseDTO(resultList);
    }

    //** 주어진 기간의 기기별 주문건수 정보를 order_useragent_data 테이블에 집계된 데이터로 가공함 **//
    public StatisticOrderByUserAgentResponseDTO getOrderByUserAgent(StatisticOrderByUserAgentRequestDTO statisticOrderByUserAgentRequestDTO){

        setDefaultDate(statisticOrderByUserAgentRequestDTO);

        // 기기별 주문건수 맵
        Map<String, Integer> orderWithUserAgentMap = statisticDao.getOrderUserAgentDataByDate(statisticOrderByUserAgentRequestDTO).stream()
                .filter(v-> statisticOrderByUserAgentRequestDTO.getUserAgentList().contains(v.getUserAgent()))
                .collect(Collectors.groupingBy(OrderWithUserAgentData::getUserAgent, Collectors.summingInt(OrderWithUserAgentData::getCount)));
        // 매핑 정보를 바탕으로 응답 객체 생성
        List<OrderWithUserAgentData> resultList = new ArrayList<>();
        orderWithUserAgentMap.keySet().forEach(v->resultList.add(new OrderWithUserAgentData(v, orderWithUserAgentMap.get(v))));

        // 아래로 집계로 잡히지않은 기기 요청시 카운트를 0으로 넘겨주는 로직
        List<String> noDataUserAgentList = statisticOrderByUserAgentRequestDTO.getUserAgentList().stream()
                .filter(v->!orderWithUserAgentMap.keySet().contains(v))
                .filter(UserAgentType::checkValidType)
                .collect(Collectors.toList());

        for(String noDataUserAgent : noDataUserAgentList){
            resultList.add(new OrderWithUserAgentData(noDataUserAgent, 0));
        }

        return new StatisticOrderByUserAgentResponseDTO(resultList);
    }

    private void setDefaultDate(StatisticRequestDTO statisticRequestDTO) {

        if (statisticRequestDTO.getStartDate().equals("")) {
            if (!statisticRequestDTO.getEndDate().equals("")) {
                throw new StatisticException(StatisticExceptionType.INVALID_PARAMETER_OPTION);
            }
            statisticRequestDTO.setStartDate(LocalDate.of(2019, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        if (statisticRequestDTO.getEndDate().equals("")) {
            statisticRequestDTO.setEndDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }

}
