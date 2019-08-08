package com.internship.tmontica_admin.menu.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuUpdateRequest {
    @Min(1)
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
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private Date endDate;
    private boolean usable = true;
    private List<Integer> optionIds = new ArrayList<>();
}
