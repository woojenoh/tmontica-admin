package com.internship.tmontica_admin.statistic;

import com.internship.tmontica_admin.statistic.model.request.StatisticRequestDTO;
import com.internship.tmontica_admin.statistic.vo.OrderWithUserAgentData;
import com.internship.tmontica_admin.statistic.vo.SalesWithAgeGroupData;
import com.internship.tmontica_admin.statistic.vo.SalesWithMenuData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatisticDao {

    @Select("SELECT age_group, total_price FROM sales_agegroup_data " +
            "WHERE reg_date BETWEEN date(#{startDate}) AND DATE_ADD(date(#{endDate}), INTERVAL 1DAY)")
    List<SalesWithAgeGroupData> getSalesAgeGroupDataByDate(StatisticRequestDTO statisticRequestDTO);

    @Select("SELECT menu_id, total_price, menu_name FROM sales_menu_data " +
            "WHERE reg_date BETWEEN date(#{startDate}) AND DATE_ADD(date(#{endDate}), INTERVAL 1DAY)")
    List<SalesWithMenuData> getSalesMenuDataByDate(StatisticRequestDTO statisticRequestDTO);

    @Select("SELECT user_agent, count FROM order_useragent_data "+
    "WHERE reg_date BETWEEN date(#{startDate}) AND DATE_ADD(date(#{endDate}), INTERVAL 1DAY)")
    List<OrderWithUserAgentData> getOrderUserAgentDataByDate(StatisticRequestDTO statisticRequestDTO);
}
