package com.internship.tmontica_admin.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Pattern(regexp="^[a-z0-9]{6,19}$")
    private String id;
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
    @NotNull
    private Date birthDate;
    @NotNull
    @Pattern(regexp="^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,19}")
    private String password;
    private String passwordCheck;
    private String role;
    private Date createdDate;
    @Min(0)
    private int point;
    private boolean isActive;
    private String activateCode;


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
