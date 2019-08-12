package com.internship.tmontica_admin.menu.model.response;

import lombok.Data;

@Data
public class MenuOptionResponse {
    private int id;
    private String name;
    private int price;
    private String type;
    private int quantity = 0;

}
