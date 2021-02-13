package com.robothy.exunion.core.trade;

import com.robothy.exunion.core.meta.SupportedExchange;

/**
 * Trading error message abstraction, the implementation must tell the
 * error code, error message, and which exchange.
 */
public interface ExchangeErrorMessage {

    String code();

    String message();

    SupportedExchange exchange();

    default String formattedMessage(){
        return String.format("[%s - %s] %s", exchange(), code(), message());
    }

}
