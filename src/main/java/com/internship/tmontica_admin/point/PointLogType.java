package com.internship.tmontica_admin.point;

import lombok.Getter;

@Getter
public enum PointLogType {

    USE_POINT("USE", "-"),
    GET_POINT("GET", "+"),
    DISSIPATE_POINT("DISSIPATE", "-");

    private String type;
    private String symbol;

    PointLogType(String type, String symbol){

        this.type = type;
        this.symbol = symbol;
    }

    // 타입과 금액을받아 적립이면 양수 차감이면 음수를 돌려주는 메소드.
    public static int getRealAmount(int amount, String type){

        for(PointLogType pointLogType : PointLogType.values()){
            if(type.equals(pointLogType.getType())) {
                return Integer.parseInt(pointLogType.getSymbol() + amount);
            }
        }

        return 0;
    }
}
