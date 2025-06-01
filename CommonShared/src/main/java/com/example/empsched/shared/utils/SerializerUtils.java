package com.example.empsched.shared.utils;

import com.example.empsched.shared.exception.MessageParsingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializerUtils {
    private SerializerUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String serialize(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new MessageParsingException(e);
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new MessageParsingException(e);
        }
    }
}
