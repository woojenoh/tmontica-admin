package com.internship.tmontica_admin.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  Menu {
    private int id;
    @NotEmpty
    private String nameEng;
    @Min(0)
    private int productPrice;
    @NotEmpty
    private String categoryKo;
    @NotEmpty
    private String categoryEng;
    private boolean monthlyMenu;
    private boolean usable;
    @NotEmpty
    private String imgUrl;
    private String description;
    @Min(0)
    private int sellPrice;
    @Min(0)
    private int discountRate;
    private Date createdDate;
    private Date updatedDate;
    private String creatorId;
    private String updaterId;

    @Min(0)
    private int stock;
    @NotEmpty
    private String nameKo;

    private Date startDate;
    private Date endDate;


}
