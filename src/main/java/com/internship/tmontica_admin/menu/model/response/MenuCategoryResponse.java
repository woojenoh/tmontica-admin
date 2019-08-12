package com.internship.tmontica_admin.menu.model.response;

import com.internship.tmontica_admin.menu.Menu;
import com.internship.tmontica_admin.paging.Pagination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuCategoryResponse {
    private String categoryKo;
    private String categoryEng;
    private Pagination pagination;
    private List<Menu> menus = new ArrayList<>();
}
