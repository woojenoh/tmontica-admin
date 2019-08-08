package com.internship.tmontica_admin.statistic.datatype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserAgentType {
    MOBILE("모바일", "MOBILE"),
    TABLET("태블릿", "TABLET"),
    PC("데스크탑&랩", "PC");

    private String nameKo;
    private String nameEng;

    public static boolean checkValidType(String ageGroup){

        for(UserAgentType value : UserAgentType.values()){
            if(value.getNameEng().equals(ageGroup)){
                return true;
            }
        }

        return false;
    }
}
