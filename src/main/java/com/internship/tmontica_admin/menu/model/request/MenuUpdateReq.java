package com.internship.tmontica_admin.menu.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class MenuUpdateReq {
    //TODO : validation..
    @NotNull
    private int menuId;
    @NotEmpty
    private String nameEng;
    @NotEmpty
    private String nameKo;
    private String description;
    private MultipartFile imgFile;
    @Min(0)
    private int productPrice;
    @Min(0)
    private int sellPrice;
    @Min(0)
    private int discountRate;
    @NotEmpty
    private String categoryEng;
    @NotEmpty
    private String categoryKo;
    @Min(0)
    private int stock;
    @NotNull
    private boolean monthlyMenu;
    @NotEmpty
    private String creator;
    private List<Integer> optionIds = new ArrayList<>();
}
