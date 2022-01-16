package org.mytoolset.utils.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;

public class RestApiCaller {

    public static final String AUTH_HEADER = "Authorization";

    private OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    private String url;
    private int connectionTimeoutInMillis = 0;
    private int readTimeoutInMillis = 0;
    private final Map<String, String> queryParams = new LinkedHashMap<>();
    private final Map<String, String> headers = new LinkedHashMap<>();
    private Object requestBody;
    private Class<?> responseClass;
    private String credentials;


    private RestApiCaller() {
        this(OkHttpClientSingleton.getInstance(), ObjectMapperSingleton.getInstance());
    }

    private RestApiCaller(@Nonnull OkHttpClient okHttpClient, @Nonnull ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.okHttpClient = okHttpClient;
    }

    public static RestApiCaller newInstance() {
        return new RestApiCaller();
    }

    @Nullable
    public <T> T call() throws ApiException {
        Objects.requireNonNull(url);

        this.adjustOkHttpClient();

        Request.Builder requestBuilder = new Request.Builder();
        this.attachUrlAndQueryParams(requestBuilder);
        this.attachHeaders(requestBuilder);
        this.attachRequestBody(requestBuilder);

        Request request = requestBuilder.build();

        Response response;
        try {
            response = this.okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new ApiException("Unexpected code " + response);
            }
        } catch (IOException ioException) {
            throw new ApiException("REST API call failed!", ioException);
        }

        if (responseClass != null) {
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new ApiException("Response body is null. " + response);
            }

            try {
                String jsonStr = responseBody.string();
                @SuppressWarnings("unchecked")
                T result = (T) this.objectMapper.readValue(jsonStr, this.responseClass);
                return result;
            } catch (JsonProcessingException je) {
                throw new ApiException("Failed to deserialize response body.", je);
            } catch (IOException ie) {
                throw new ApiException("Failed to get response body.", ie);
            }

        }
        return null;
    }

    private void adjustOkHttpClient() {
        if (this.connectionTimeoutInMillis > 0 || this.readTimeoutInMillis > 0) {
            OkHttpClient.Builder builder = this.okHttpClient.newBuilder();
            if (this.connectionTimeoutInMillis > 0) {
                builder.connectTimeout(this.connectionTimeoutInMillis, TimeUnit.MILLISECONDS);
            }
            if (this.readTimeoutInMillis > 0) {
                builder.readTimeout(this.readTimeoutInMillis, TimeUnit.MILLISECONDS);
            }

            this.okHttpClient = builder.build();
        }
    }

    private void attachUrlAndQueryParams(@Nonnull Request.Builder requestBuilder) throws ApiException {
        if (queryParams.isEmpty()) {
            requestBuilder.url(this.url);
        } else {
            HttpUrl httpUrl = HttpUrl.parse(url);
            if (httpUrl == null) {
                throw new ApiException("Failed to parse url " + url);
            }
            HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
            this.queryParams.forEach(urlBuilder::addQueryParameter);

            String url = urlBuilder.build().toString();
            requestBuilder.url(url);
        }
    }

    private void attachHeaders(@Nonnull Request.Builder requestBuilder) {
        this.headers.forEach(requestBuilder::addHeader);
        if (StringUtils.isNotBlank(this.credentials)) {
            requestBuilder.addHeader(AUTH_HEADER, this.credentials);
        }
    }

    private void attachRequestBody(@Nonnull Request.Builder requestBuilder) throws ApiException {
        if (requestBody != null) {
            try {
                String jsonBody = objectMapper.writeValueAsString(requestBody);
                RequestBody body = RequestBody.create(jsonBody,
                        MediaType.parse("application/json; charset=utf-8"));
                requestBuilder.post(body);
            } catch (JsonProcessingException e) {
                throw new ApiException("Cannot serialize requestBody " + requestBody.getClass(), e);
            }
        }
    }

    public RestApiCaller url(@Nonnull String url) {
        this.url = url;
        return this;
    }

    public RestApiCaller connectionTimeoutInMillis(int connectionTimeoutInMillis) {
        this.connectionTimeoutInMillis = connectionTimeoutInMillis;
        return this;
    }

    public RestApiCaller readTimeoutInMillis(int readTimeoutInMillis) {
        this.readTimeoutInMillis = readTimeoutInMillis;
        return this;
    }

    public RestApiCaller headers(@Nonnull Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public RestApiCaller addHeader(@Nonnull String key, @Nonnull String value) {
        this.headers.put(key, value);
        return this;
    }

    public RestApiCaller queryParams(@Nonnull Map<String, String> queryParams) {
        this.queryParams.putAll(queryParams);
        return this;
    }

    public RestApiCaller addQueryParam(@Nonnull String key, @Nonnull String value) {
        this.queryParams.put(key, value);
        return this;
    }

    public RestApiCaller requestBody(@Nonnull Object requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public RestApiCaller responseClass(@Nonnull Class<?> responseClass) {
        this.responseClass = responseClass;
        return this;
    }

    public RestApiCaller basicAuthentication(@Nonnull String username, @Nonnull String password) {
        this.credentials = Credentials.basic(username, password);
        return this;
    }
}

