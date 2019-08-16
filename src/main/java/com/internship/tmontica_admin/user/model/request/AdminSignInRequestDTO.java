package com.internship.tmontica_admin.user.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AdminSignInRequestDTO {

    @NotNull
    private String id;
    @NotNull
    private String password;
}
