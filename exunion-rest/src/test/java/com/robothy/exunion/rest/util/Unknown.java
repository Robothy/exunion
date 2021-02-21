package com.robothy.exunion.rest.util;

import com.robothy.exunion.core.meta.Exchange;

public enum Unknown implements Exchange {

    SINGLETON;

    @Override
    public String id() {
        return "unknown";
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
