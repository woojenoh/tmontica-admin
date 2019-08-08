package com.internship.tmontica_admin.statistic.model.response;

import com.internship.tmontica_admin.statistic.vo.SalesWithMenuData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StatisticMenuByDateRespDTO {

    private List<SalesWithMenuData> dataList;
}
