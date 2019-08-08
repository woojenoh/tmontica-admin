package com.internship.tmontica_admin.menu.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuMainResponse {
    private String categoryKo;
    private String categoryEng;
    private List<MenuSimpleResponse> menus = new ArrayList<>();
}
