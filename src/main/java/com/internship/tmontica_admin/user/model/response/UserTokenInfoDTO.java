package com.internship.tmontica_admin.user.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserTokenInfoDTO {

    private String id;
    private String name;
    private String email;
    private Date birthDate;
    private String role;
    private int point;

    public String toJson(){

        StringBuilder stringBuilder = new StringBuilder();
        makeJsonObjectStart("id", id, stringBuilder, false);
        makeJsonElement("name", name, stringBuilder, false, false);
        makeJsonElement("email", email, stringBuilder, false, false);
        makeJsonElement("birthDate", birthDate.toString(), stringBuilder, false, false);
        makeJsonElement("role", role, stringBuilder, false, false);
        makeJsonElement("point",Integer.toString(point), stringBuilder, true, true);

        return stringBuilder.toString();
    }

    private void makeJsonObjectStart(String type, String value, StringBuilder stringBuilder, boolean isInt){
        stringBuilder.append("{");
        makeJsonElement(type, value, stringBuilder, false, isInt);
    }

    private void makeJsonElement(String type, String value, StringBuilder stringBuilder, boolean isLast, boolean isInt){

        stringBuilder.append("\"");
        stringBuilder.append(type);
        if(!isInt) {
            stringBuilder.append("\":\"");
        } else{
           stringBuilder.append("\":");
        }
        stringBuilder.append(value);
        if(!isLast && !isInt) {
            stringBuilder.append("\",");
            return;
        } else if(!isLast && isInt){
            stringBuilder.append(",");
            return;
        }

        if(isInt){
            stringBuilder.append("}");
            return;
        }
        stringBuilder.append("\"}");
    }
}
