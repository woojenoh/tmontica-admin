package com.internship.tmontica_admin.banner;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BannerUsePage {

    BANNER_MAIN_TOP("메인-상단", "main-top"),
    BANNER_MAIN_BOTTOM("메인-하단", "main-bottom");

    private String usePageKo;
    private String usePageEng;

    public static String convertKoToEng(String location) {

        for (BannerUsePage bannerUsePage : BannerUsePage.values()) {
            if(bannerUsePage.getUsePageKo().equals(location)){
                return bannerUsePage.getUsePageEng();
            }
        }
        return "non";
    }

}
