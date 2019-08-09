package com.internship.tmontica_admin.menu.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDetailResponse {
    private int id;
    private String nameEng;
    private String nameKo;
    private String description;
    private String imgUrl;
    private int sellPrice;
    private int productPrice;
    private int discountRate;
    private String categoryEng;
    private String categoryKo;
    private int stock;
    private boolean monthlyMenu;
    private Date startDate;
    private Date endDate;
    private boolean usable;
    private List<MenuOptionResponse> option;

    public void setImgUrl(String imgUrl){
        this.imgUrl = "/images/".concat(imgUrl);
    }
}