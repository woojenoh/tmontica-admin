package com.internship.tmontica_admin.statistic.model.response;

import com.internship.tmontica_admin.statistic.vo.SalesWithAgeGroupData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StatisticSalesByAgeGroupResponseDTO {

    private List<SalesWithAgeGroupData> dataList;
}
