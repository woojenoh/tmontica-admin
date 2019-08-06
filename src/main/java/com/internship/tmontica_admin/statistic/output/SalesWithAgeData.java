package com.internship.tmontica_admin.statistic.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@AllArgsConstructor
public class SalesWithAgeData {

    private AgeGroup ageGroup;
    private Date today;
    private int price;
}
