package com.internship.tmontica_admin.security;

import lombok.Getter;

import java.util.Random;

@Getter
public class AuthenticationKey {

    private static final int AUTHENTICATION_KEY_LENGTH = 10;
    private String authenticationKey;

    public AuthenticationKey(){
        this.createKey();
    }

    private void createKey(){

        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for(int i=0; i<AUTHENTICATION_KEY_LENGTH; i++){
            if(random.nextBoolean()){
                builder.append((char)(random.nextInt(26)+97));
                continue;
            }
            builder.append(random.nextInt(10));
        }

        authenticationKey = builder.toString();
    }

}
