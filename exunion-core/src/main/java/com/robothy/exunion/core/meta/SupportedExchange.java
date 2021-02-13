package com.robothy.exunion.core.meta;

public enum SupportedExchange {

    HUOBI("huobi.com", "https://api.huobi.pro"),
    ;

    SupportedExchange(String name, String apiServer) {
        this.name = name;
        this.defaultApiServer = apiServer;
    }

    private String name;

    private String defaultApiServer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultApiServer() {
        return defaultApiServer;
    }

    public void setDefaultApiServer(String defaultApiServer) {
        this.defaultApiServer = defaultApiServer;
    }
}
