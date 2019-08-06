package com.internship.tmontica_admin.statistic;

import com.internship.tmontica_admin.order.Order;
import com.internship.tmontica_admin.order.OrderDao;
import com.internship.tmontica_admin.statistic.output.AgeGroup;
import com.internship.tmontica_admin.user.User;
import com.internship.tmontica_admin.user.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final UserDao userDao;
    private final OrderDao orderDao;

    public void makeData(){

        // 전체 주문 리스트
        List<Order> todayOrderList = orderDao.getTodayOrders(0,100);
        // 유저별 총 구매액
        Map<String, Integer> totalPriceByUser = todayOrderList.stream()
                .collect(Collectors.groupingBy(Order::getUserId, Collectors.summingInt(Order::getTotalPrice)));
        // 상품을 구매한 유저 리스트
        List<User> userList = userDao.getAllUser().stream().filter(v->totalPriceByUser.keySet().contains(v.getId())).collect(Collectors.toList());
        // 연령별 총구매액 맵
        Map<String, Integer> totalPriceByAgeGroup = userList.stream()
                .collect(Collectors.toMap(v->AgeGroup.getAgeGroup(v.getBirthDate()), v->totalPriceByUser.get(v.getId()),
                        Integer::sum));

        totalPriceByAgeGroup.keySet().forEach(System.out::println);
        totalPriceByAgeGroup.keySet().forEach(v-> System.out.println(totalPriceByAgeGroup.get(v)));
    }
}
