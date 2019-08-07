package com.internship.tmontica_admin.statistic;

import com.internship.tmontica_admin.order.Order;
import com.internship.tmontica_admin.order.OrderDao;
import com.internship.tmontica_admin.order.OrderDetail;
import com.internship.tmontica_admin.statistic.datatype.AgeGroup;
import com.internship.tmontica_admin.statistic.vo.SalesWithAgeData;
import com.internship.tmontica_admin.statistic.vo.SalesWithMenuData;
import com.internship.tmontica_admin.user.User;
import com.internship.tmontica_admin.user.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatisticScheduler {

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final StatisticDao statisticDao;

    // 좀더 자주도는것으로, 뺴기, 널체크
//    @Scheduled(cron = "0 0 19 * * *")
//    public void makeTotalSalesByAgeGroupData(){
//
//        // 유저별 총 구매액
//        Map<String, Integer> totalPriceByUser = orderDao.getTodayAllOrders().stream().filter(Order::isRealSales)
//                .collect(Collectors.groupingBy(Order::getUserId, Collectors.summingInt(Order::getTotalPrice)));
//
//        // 연령별 총구매액 맵
//        Map<String, Integer> totalSalesByAgeGroup =  userDao.getAllUser().stream()
//                .filter(v->totalPriceByUser.keySet().contains(v.getId()))
//                .collect(Collectors.toMap(v->AgeGroup.getAgeGroup(v.getBirthDate()), v->totalPriceByUser.get(v.getId()),
//                        Integer::sum));
//
//        for(String ageGroup : totalSalesByAgeGroup.keySet()){
//            statisticDao.saveSalesAgeGroupData(new SalesWithAgeData(ageGroup, totalSalesByAgeGroup.get(ageGroup)));
//        }
//    }
//
//    @Scheduled(cron = "0 0 19 * * *")
//    public void makeTotalSalesByMenu(){
//
//        // 전체 유효 상세 주문 리스트
//        List<OrderDetail> todayOrderDetailList = orderDao.getTodayOrderDetails();
//
//        // 메뉴 id별 매출액 맵
//        Map<Integer, Integer> totalSalesByMenuId = todayOrderDetailList.stream()
//                .collect(Collectors.toMap(OrderDetail::getMenuId, OrderDetail::getTotalPrice, Integer::sum));
//
//        for(Integer menuId : totalSalesByMenuId.keySet()){
//            statisticDao.saveSalesMenuData(new SalesWithMenuData(menuId, totalSalesByMenuId.get(menuId)));
//        }
//
////        totalSalesByMenuId.keySet().forEach(System.out::println);
////        totalSalesByMenuId.keySet().forEach(v->System.out.println(totalSalesByMenuId.get(v)));
//    }
//

}
