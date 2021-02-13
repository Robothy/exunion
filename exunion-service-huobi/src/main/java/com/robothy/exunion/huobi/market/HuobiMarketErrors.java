package com.robothy.exunion.huobi.market;

import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.trade.ExchangeErrorMessage;

public enum HuobiMarketErrors implements ExchangeErrorMessage {

    INVALID_SYMBOL("M001", "invalid symbol"),

    INVALIE_PEROID("M002", "invalid peroid"),

    INVALID_DEPTH("M003", "invalid depth"),

    INVALID_SIZE("M004", "invalid")

    ;

    HuobiMarketErrors(String code, String message){
        this.code = code;
        this.message = message;
    }

    private String code;

    private String message;

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public SupportedExchange exchange() {
        return SupportedExchange.HUOBI;
    }
}
