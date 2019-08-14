package com.internship.tmontica_admin.option;

import lombok.Getter;

@Getter
public enum OptionType {

    TEMPERATURE("Temperature", "온도"),
    SHOT("Shot", "샷추가"),
    SYRUP("Syrup", "시럽추가"),
    SIZE("Size","사이즈업");

    private String type;
    private String name;

    OptionType(String type, String name) {
        this.type = type;
        this.name = name;
    }
}
