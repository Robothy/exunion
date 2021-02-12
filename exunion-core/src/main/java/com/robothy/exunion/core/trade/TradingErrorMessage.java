package com.robothy.exunion.core.trade;

/**
 * Trading error message abstraction, the implementation must tell the
 * error code, error message, and which exchange.
 */
public interface TradingErrorMessage {

    String code();

    String message();

    String exchange();

    default String formattedMessage(){
        return String.format("[%s - %s] %s", exchange(), code(), message());
    }

}
