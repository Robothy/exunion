package com.robothy.exunion.binance.handler;

import com.binance.api.client.BinanceApiError;
import com.binance.api.client.exception.BinanceApiException;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.spi.ExchangeServiceExceptionHandler;

import java.util.Collections;
import java.util.Set;

/**
 * The {@link BinanceApiException} handler.
 */
public class BinanceApiExceptionHandler implements ExchangeServiceExceptionHandler {

    /**
     * Extract the error code and error message from a {@link BinanceApiException} instance
     * and set the code and message to an {@link Result} object, then return it.
     *
     * @param e a {@link BinanceApiException} object.
     * @return an {@link Result} with code and message.
     */
    @Override
    public Result<?> handle(RuntimeException e) {
        if (e instanceof BinanceApiException) {
            BinanceApiException ex = (BinanceApiException) e;
            BinanceApiError error = ex.getError();
            return new Result<>(error == null ? null : String.valueOf(error.getCode()), ex.getMessage());
        } else {
            return new Result<>(null, e.getMessage());
        }
    }

    @Override
    public Set<Class<? extends RuntimeException>> exceptions() {
        return Collections.singleton(BinanceApiException.class);
    }
}
