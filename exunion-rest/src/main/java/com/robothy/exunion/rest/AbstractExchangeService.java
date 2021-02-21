package com.robothy.exunion.rest;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.robothy.exunion.rest.spi.Options;

import java.util.Optional;

/**
 * Initialized common used options.
 */
public abstract class AbstractExchangeService implements ExchangeService {

    protected Options options;

    @Override
    public void init(Options options) {
        this.options = Optional.ofNullable(options).orElse(new Options());
        if(this.options.getApiServer() == null){
            this.options.setApiServer(exchange().defaultApiServer());
        }

        if(this.options.getJsonFactory() == null){
            this.options.setJsonFactory(new JacksonFactory());
        }

        if(this.options.getHttpRequestFactory() == null){
            this.options.setHttpRequestFactory(new NetHttpTransport().createRequestFactory(request -> request.setParser(new JsonObjectParser(new JacksonFactory()))));
        }
    }

    public Options getOptions() {
        return options;
    }

    /**
     * Update the options that will be effective immediately.
     * @param options new options.
     */
    public void setOptions(Options options) {
        this.options = options;
        this.init(options);
    }
}