package com.internship.tmontica_admin.statistic.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SalesWithAgeData {

    private String ageGroup;
    private int totalPrice;

    public SalesWithAgeData(String ageGroup, int totalPrice){
        this.ageGroup = ageGroup;
        this.totalPrice = totalPrice;
    }
}
