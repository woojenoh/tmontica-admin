package com.internship.tmontica_admin.statistic.model.response;

import com.internship.tmontica_admin.statistic.vo.SalesWithAgeData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StatisticSalesByAgeRespDTO {

    private List<SalesWithAgeData> dataList;
}
