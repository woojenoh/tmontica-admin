package com.internship.tmontica_admin.menu.model.response;

import com.internship.tmontica_admin.menu.Menu;
import com.internship.tmontica_admin.paging.Pagination;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MenuByPageResp {
    private Pagination pagination;
    private List<Menu> menus;
}
