package com.internship.tmontica_admin.menu.model.request;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MenuMonthlyUpdateRequest {
    private List<Integer> menuIds = new ArrayList<>();
}
