package com.internship.tmontica_admin.statistic;

import com.internship.tmontica_admin.statistic.model.request.StatisticReqDTO;
import com.internship.tmontica_admin.statistic.vo.SalesWithAgeData;
import com.internship.tmontica_admin.statistic.vo.SalesWithMenuData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatisticDao {

    @Insert("INSERT INTO sales_agegroup_data (age_group , total_price) " +
            "VALUES(#{ageGroup}, #{totalPrice})")
    int saveSalesAgeGroupData(SalesWithAgeData salesWithAgeData);
    @Insert("INSERT INTO sales_menu_data (menu_id , total_price) " +
            "VALUES(#{menuId}, #{totalPrice})")
    int saveSalesMenuData(SalesWithMenuData salesWithMenuData);

    // 위 인서트문은 statistic으로 이전.
    @Select("SELECT age_group, total_price FROM sales_agegroup_data " +
            "WHERE reg_date BETWEEN date(#{startDate}) AND date(#{endDate})+1")
    List<SalesWithAgeData> getSalesAgeGroupDataByDate(StatisticReqDTO statisticReqDTO);

    @Select("SELECT menu_id, total_price FROM sales_menu_data " +
            "WHERE reg_date BETWEEN date(#{startDate}) AND date(#{endDate})+1")
    List<SalesWithMenuData> getSalesMenuDataByDate(StatisticReqDTO statisticReqDTO);


}
