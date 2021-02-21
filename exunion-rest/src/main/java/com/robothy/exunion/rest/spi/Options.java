package com.robothy.exunion.rest.spi;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.json.JsonFactory;
import com.robothy.exunion.core.auth.Token;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Options {

    private Token token;

    private Map<String, Object> extraProperties;

    private String apiServer;

    private JsonFactory jsonFactory;

    private HttpRequestFactory httpRequestFactory;

    private ExecutorService executor;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Map<String, Object> getExtraProperties() {
        return extraProperties;
    }

    public void setExtraProperties(Map<String, Object> extraProperties) {
        this.extraProperties = extraProperties;
    }

    public String getApiServer() {
        return apiServer;
    }

    public void setApiServer(String apiServer) {
        this.apiServer = apiServer;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public HttpRequestFactory getHttpRequestFactory() {
        return httpRequestFactory;
    }

    public void setHttpRequestFactory(HttpRequestFactory httpRequestFactory) {
        this.httpRequestFactory = httpRequestFactory;
    }

    public JsonFactory getJsonFactory() {
        return jsonFactory;
    }

    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }
}
