package com.robothy.exunion.binance.handler;

import com.binance.api.client.BinanceApiError;
import com.binance.api.client.exception.BinanceApiException;
import com.robothy.exunion.core.exception.InvalidOperationException;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.spi.ExchangeServiceExceptionHandler;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BinanceApiExceptionHandlerTest {

    @Test
    void instanceExists() {
        ServiceLoader<ExchangeServiceExceptionHandler> load = ServiceLoader.load(ExchangeServiceExceptionHandler.class);
        Set<Class<? extends ExchangeServiceExceptionHandler>> handlers = new HashSet<>();
        for (ExchangeServiceExceptionHandler handler : load) {
            handlers.add(handler.getClass());
        }
        assertTrue(handlers.contains(BinanceApiExceptionHandler.class));
    }

    @Test
    void handleBinanceApiException() {
        BinanceApiExceptionHandler handler = new BinanceApiExceptionHandler();
        BinanceApiError binanceApiError = new BinanceApiError();
        binanceApiError.setCode(-1);
        binanceApiError.setMsg("Invalid Account.");
        BinanceApiException binanceApiException = new BinanceApiException(binanceApiError);

        Result<?> result = handler.handle(binanceApiException);
        assertNotNull(result);
        assertFalse(result.ok());
        assertEquals("-1", result.getCode());
        assertEquals("Invalid Account.", result.getMessage());
    }

    @Test
    void handleOtherRuntimeException() {
        BinanceApiExceptionHandler handler = new BinanceApiExceptionHandler();
        InvalidOperationException invalidOperationException = new InvalidOperationException("Invalid Account.");
        Result<?> result = handler.handle(invalidOperationException);

        assertNotNull(result);
        assertFalse(result.ok());
        assertNull(result.getCode());
        assertEquals("Invalid Account.", result.getMessage());
    }

    @Test
    void testExceptions() {
        Set<Class<? extends RuntimeException>> exceptions = new BinanceApiExceptionHandler().exceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.contains(BinanceApiException.class));
    }

}