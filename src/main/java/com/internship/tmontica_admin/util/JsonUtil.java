package com.internship.tmontica_admin.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();
    public static String getJsonElementValue(String json, String key) {

        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(json);
        } catch (IOException e){
            e.printStackTrace();
            return "fail to parse json";
        }

        JsonNode keyNode = jsonNode.get(key);
        return keyNode.textValue();
    }

//    public static void main(String[] args){
//
//        System.out.println(getJsonElementValue("{\"id\":\"useridid\"}", "id"));
//    }
}
