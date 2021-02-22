package com.robothy.exunion.binance;

public enum Binance implements com.robothy.exunion.core.meta.Exchange {

    BINANCE;

    @Override
    public String id() {
        return "binance";
    }

    @Override
    public String defaultApiServer() {
        return "https://api1.binance.com";
    }

    @Override
    public String apiRef() {
        return "https://binance-docs.github.io/apidocs/spot/en";
    }
}
