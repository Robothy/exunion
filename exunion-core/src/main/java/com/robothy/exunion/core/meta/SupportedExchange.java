package com.robothy.exunion.core.meta;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

public enum SupportedExchange {

    HUOBI("huobi.com", "https://api.huobi.pro", "https://huobiapi.github.io/docs/spot/v1/en"),
    ;

    SupportedExchange(String name, String apiServer, String apiRef) {
        this.name = name;
        this.defaultApiServer = apiServer;
        this.apiRef = apiRef;
    }

    private String name;

    private String defaultApiServer;

    private String apiRef;

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
