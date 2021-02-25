package com.robothy.exunion.huobi.common;

import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.trade.ExchangeErrorMessage;

public class HuobiExchangeErrorMessageUtil {

    public static ExchangeErrorMessage of(String code, String msg){
        return new ExchangeErrorMessage() {
            @Override
            public String code() {
                return code;
            }

            @Override
            public String message() {
                return msg;
            }

            @Override
            public SupportedExchange exchange() {
                return SupportedExchange.HUOBI;
            }
        };
    }

}
