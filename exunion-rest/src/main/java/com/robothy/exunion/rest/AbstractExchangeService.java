package com.robothy.exunion.rest;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.robothy.exunion.core.auth.Token;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Define shared properties in different trading services.
 */
public abstract class AbstractExchangeService implements ExchangeService {

    private Token token;

    private Map<String, Object> extraProperties;

    private HttpTransport httpTransport;

    private JsonFactory jsonFactory;

    protected HttpRequestFactory requestFactory;

    private String apiServer;

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

    public HttpTransport getHttpTransport() {
        return httpTransport;
    }

    public void setHttpTransport(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    public JsonFactory getJsonFactory() {
        return jsonFactory;
    }

    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    public HttpRequestFactory getRequestFactory() {
        return requestFactory;
    }

    public void setRequestFactory(HttpRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
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
}