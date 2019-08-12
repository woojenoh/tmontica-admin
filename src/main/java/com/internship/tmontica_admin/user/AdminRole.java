package com.internship.tmontica_admin.user;

import lombok.Getter;

@Getter
public enum AdminRole {
    ADMIN("ADMIN"),
    SUPER_VISOR("SUPER"),
    CEO("CEO");

    private String role;

    AdminRole(String role){

        this.role = role;
    }
}


