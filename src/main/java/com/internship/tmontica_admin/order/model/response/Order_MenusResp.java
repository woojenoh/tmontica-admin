package com.internship.tmontica_admin.order.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order_MenusResp {
    private int menuId;
    private String nameEng;
    private String nameKo;
    private String option;
    private String imgUrl;
    private int quantity;
    private int price;
}


