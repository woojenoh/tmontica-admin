package com.internship.tmontica_admin.statistic.datatype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgeGroup {

    INFANTS(9, "영유아"),
    TEENAGER(19, "10대"),
    TWENTIES(29, "20대"),
    THIRTIES(39, "30대"),
    FORTIES(49, "40대"),
    FIFTIES(59, "50대"),
    OVER_SIXTIES(999, "60대 이상");

    private int ageGroupMax;
    private String name;

    public static boolean checkValidType(String ageGroup){

        for(AgeGroup value : AgeGroup.values()){
            if(value.getName().equals(ageGroup)){
                return true;
            }
        }

        return false;
    }
}
