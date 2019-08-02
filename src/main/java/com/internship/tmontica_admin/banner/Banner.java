package com.internship.tmontica_admin.banner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Banner {
    private int id;
    private String usePage;
    private boolean usable;
    private String imgUrl;
    private String link;
    private Date startDate;
    private Date endDate;
    private String creatorId;
    private int number;
}
