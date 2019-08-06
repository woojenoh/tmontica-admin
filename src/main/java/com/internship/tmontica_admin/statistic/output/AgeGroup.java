package com.internship.tmontica_admin.statistic.output;

import com.internship.tmontica_admin.statistic.exception.StatisticException;
import com.internship.tmontica_admin.statistic.exception.StatisticExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Getter
@AllArgsConstructor
public enum AgeGroup {

    INFANTS(10, "영유아"),
    TEENAGER(20, "10대"),
    TWENTIES(30, "20대"),
    THIRTIES(40, "30대"),
    FORTIES(50, "40대"),
    FIFTIES(60, "50대"),
    OVER_SIXTIES(999, "60대 이상");

    private int ageGroupMax;
    private String name;

    public static String getAgeGroup(Date userBirthDate){

        int userBirthYear = LocalDateTime.ofInstant(userBirthDate.toInstant(), ZoneId.systemDefault()).getYear();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - userBirthYear + 1;
        for(AgeGroup ageGroup : AgeGroup.values()){
            if(ageGroup.getAgeGroupMax() > age){
                return ageGroup.getName();
            }
        }

        throw new StatisticException(StatisticExceptionType.INVALID_DATE_EXCEPTION);
    }
}
