package com.robothy.exunion.huobi.meta;

import com.robothy.exunion.core.meta.Exchange;

public enum Huobi implements Exchange {

    SINGLETON;

    @Override
    public String id() {
        return "huobi";
    }

    @Override
    public String defaultApiServer() {
        return "https://api.huobi.be";
    }

    @Override
    public String apiRef() {
        return "https://huobiapi.github.io/docs/spot/v1/en";
    }
}
