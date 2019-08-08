package com.internship.tmontica_admin.statistic.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SalesWithMenuData {

    private int menuId;
    private int totalPrice;
    private String menuName;

    public SalesWithMenuData(int menuId, int totalPrice, String menuName){
        this.menuId = menuId;
        this.totalPrice = totalPrice;
        this.menuName = menuName;
    }
}
