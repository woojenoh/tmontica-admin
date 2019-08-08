package com.internship.tmontica_admin.statistic.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderWithUserAgentData {

    private String userAgent;
    private int count;

    public OrderWithUserAgentData(String userAgent, int count){
        this.userAgent = userAgent;
        this.count = count;
    }
}
