package com.robothy.exunion.rest.util;

import com.robothy.exunion.core.meta.Exchange;

public enum Huobi implements Exchange {

    SINGLETON;

    @Override
    public String id() {
        return "huobi";
    }

    @Override
    public String defaultApiServer() {
        return null;
    }

    @Override
    public String apiRef() {
        return null;
    }
}
