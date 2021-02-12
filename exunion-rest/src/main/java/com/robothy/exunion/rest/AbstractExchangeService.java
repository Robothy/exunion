package com.robothy.exunion.rest;

import com.robothy.exunion.core.auth.Token;

import java.util.Map;

/**
 * Define shared properties in different trading services.
 */
public abstract class AbstractExchangeService implements ExchangeService {

    private Token token;

    private Map<String, Object> extraProperties;

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

}