package com.internship.tmontica_admin.menu.model.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MenuIdName {
    private int id;
    private String nameKo;

    public boolean isThisId(int id){

        return this.id == id;
    }
}
