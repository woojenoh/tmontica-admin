package com.internship.tmontica_admin.menu;


import com.internship.tmontica_admin.option.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuDetail {
    private int id;
    @NotEmpty
    private String nameEng;
    @NotEmpty
    private String nameKo;
    private String description;
    @NotEmpty
    private String img;
    @Min(0)
    private int sellPrice;
    @Min(0)
    private int productPrice;
    @Min(0)
    private int discountRate;
    @NotEmpty
    private String categoryEng;
    @NotEmpty
    private String categoryKo;
    @Min(0)
    private int stock;
    private boolean monthlyMenu;

   // 메뉴의 옵션
    private List<Option> options;

    public void addOption(Option option){
        if(options == null)
            options = new ArrayList<>();
        options.add(option);
    }

}
