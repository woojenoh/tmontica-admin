package com.internship.tmontica_admin.menu.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuCategoryResp {
    private String categoryKo;
    private String categoryEng;
    private int size;
    private int page;
    private List<MenuSimpleResp> menus = new ArrayList<>();
}
