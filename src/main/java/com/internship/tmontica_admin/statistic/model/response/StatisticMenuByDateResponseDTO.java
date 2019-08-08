package com.internship.tmontica_admin.statistic.model.response;

import com.internship.tmontica_admin.statistic.vo.SalesWithMenuData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StatisticMenuByDateResponseDTO {

    private List<SalesWithMenuData> dataList;
}
