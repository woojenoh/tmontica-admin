package com.internship.tmontica_admin.banner.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class BannerUpdateReq {

    @NotEmpty(message = "usePage는 필수입니다.")
    private String usePage;

    private boolean usable = true;

    @NotNull
    private MultipartFile imgFile;

    @NotEmpty(message = "link는 필수입니다.")
    private String link;

    @NotNull(message = "시작일은 필수입니다.")
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private Date startDate;

    @NotNull(message = "종료일은 필수입니다.")
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private Date endDate;

    @NotNull(message = "순서는 필수입니다.")
    @Min(1) @Max(10)
    private int number;
}
