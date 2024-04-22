package com.shop.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtility {
    public static String getJsonBody(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}
