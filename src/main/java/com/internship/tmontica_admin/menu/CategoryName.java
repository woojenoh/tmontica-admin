package com.internship.tmontica_admin.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CategoryName {

    CATEGORY_COFFEE("coffee", "커피"),
    CATEGORY_ADE("ade", "에이드"),
    CATEGORY_BREAD("bread", "빵"),
    CATEGORY_MONTHLY("monthlymenu", "이달의 메뉴");

    private String categoryEng;
    private String categoryKo;

    public static String convertEngToKo(String categoryEng){

        for(CategoryName categoryName : CategoryName.values()){
            if(categoryName.getCategoryEng().equals(categoryEng)){
                return categoryName.getCategoryKo();
            }
        }

        return "non";
    }

}
