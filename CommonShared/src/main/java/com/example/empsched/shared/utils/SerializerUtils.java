package com.example.empsched.shared.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializerUtils {
    public static String serialize(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Deserialization failed", e);
        }
    }
}
