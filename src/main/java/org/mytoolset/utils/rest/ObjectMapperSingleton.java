package org.mytoolset.utils.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Jackson ObjectMapper singleton provider
 */
public class ObjectMapperSingleton {

    private ObjectMapperSingleton() {
    }

    private static class LazyHolder {
        static final ObjectMapper INSTANCE = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new JavaTimeModule());
    }

    public static ObjectMapper getInstance() {
        return LazyHolder.INSTANCE;
    }
}

