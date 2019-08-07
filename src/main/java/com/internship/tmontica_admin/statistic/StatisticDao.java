package com.internship.tmontica_admin.statistic;

import com.internship.tmontica_admin.statistic.model.request.StatisticReqDTO;
import com.internship.tmontica_admin.statistic.vo.SalesWithAgeData;
import com.internship.tmontica_admin.statistic.vo.SalesWithMenuData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatisticDao {

    @Select("SELECT age_group, total_price FROM sales_agegroup_data " +
            "WHERE reg_date BETWEEN date(#{startDate}) AND date(#{endDate})+1")
    List<SalesWithAgeData> getSalesAgeGroupDataByDate(StatisticReqDTO statisticReqDTO);

    @Select("SELECT menu_id, total_price, menu_name FROM sales_menu_data " +
            "WHERE reg_date BETWEEN date(#{startDate}) AND date(#{endDate})+1")
    List<SalesWithMenuData> getSalesMenuDataByDate(StatisticReqDTO statisticReqDTO);


}
