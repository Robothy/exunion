package com.robothy.exunion.rest.spi;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.json.JsonFactory;
import com.robothy.exunion.core.auth.Token;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class OptionsBuilder {

    private final Options options;

    public static OptionsBuilder create(){
        return new OptionsBuilder();
    }

    public OptionsBuilder(){
        this.options = new Options();
    }

    public OptionsBuilder token(Token token){
        requireNonNull();
        options.setToken(token);
        return this;
    }

    public OptionsBuilder putExtra(String key, Object value){
        requireNonNull();
        if(options.getExecutor() == null) options.setExtraProperties(new HashMap<>());
        options.getExtraProperties().put(key, value);
        return this;
    }

    public OptionsBuilder jsonFactory(JsonFactory jsonFactory){
        requireNonNull();
        options.setJsonFactory(jsonFactory);
        return this;
    }

    public OptionsBuilder httpRequestFactory (HttpRequestFactory requestFactory){
        requireNonNull();
        options.setHttpRequestFactory(requestFactory);
        return this;
    }

    public OptionsBuilder apiServer(String apiServer){
        requireNonNull();
        options.setApiServer(apiServer);
        return this;
    }

    public OptionsBuilder executor(ExecutorService executor){
        requireNonNull();
        options.setExecutor(executor);
        return this;
    }

    public Options build(){
        requireNonNull();
        return options;
    }

    private void requireNonNull(){
        Objects.requireNonNull(options, "Please call the static method create() to create an instance before set properties.");
    }

}
