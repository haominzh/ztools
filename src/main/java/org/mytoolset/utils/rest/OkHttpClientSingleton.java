package org.mytoolset.utils.rest;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

/**
 * OkHttpClient singleton provider
 */
public class OkHttpClientSingleton {

    private static final int DEFAULT_CONNECTION_TIMEOUT_IN_SECONDS = 5;
    private static final int DEFAULT_READ_TIMEOUT_IN_SECONDS = 5;

    private OkHttpClientSingleton() {
    }

    private static class LazyHolder {

        static final OkHttpClient INSTANCE = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpClient getInstance() {
        return LazyHolder.INSTANCE;
    }
}

