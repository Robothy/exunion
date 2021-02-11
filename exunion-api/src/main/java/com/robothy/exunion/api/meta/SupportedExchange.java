package com.robothy.exunion.api.meta;

public enum SupportedExchange {

    HUOBI("huobi.com", "https://huobi.be"),
    ;

    SupportedExchange(String name, String[] apiServer) {
        this.name = name;
        this.apiServer = apiServer;
    }

    SupportedExchange(String name, String apiServer) {
        this.name = name;
        this.apiServer = new String[]{apiServer};
    }

    private String name;

    private String[] apiServer;

}
