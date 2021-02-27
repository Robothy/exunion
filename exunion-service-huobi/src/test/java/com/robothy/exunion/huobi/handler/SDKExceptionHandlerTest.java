package com.robothy.exunion.huobi.handler;

import com.huobi.exception.SDKException;
import com.robothy.exunion.core.exception.InvalidOperationException;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.spi.ExchangeServiceExceptionHandler;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SDKExceptionHandlerTest {

    @Test
    void instanceExists() {
        ServiceLoader<ExchangeServiceExceptionHandler> load = ServiceLoader.load(ExchangeServiceExceptionHandler.class);
        Set<Class<? extends ExchangeServiceExceptionHandler>> handlers = new HashSet<>();
        for (ExchangeServiceExceptionHandler handler : load) {
            handlers.add(handler.getClass());
        }
        assertTrue(handlers.contains(SDKExceptionHandler.class));
    }

    @Test
    void handleBinanceApiException() {
        SDKExceptionHandler handler = new SDKExceptionHandler();
        SDKException sdkException = new SDKException("invalid-account", "Invalid Account");
        Result<?> result = handler.handle(sdkException);
        assertNotNull(result);
        assertFalse(result.ok());
        assertEquals("invalid-account", result.getCode());
        assertEquals("Invalid Account", result.getMessage());
    }

    @Test
    void handleOtherRuntimeException(){
        SDKExceptionHandler handler = new SDKExceptionHandler();
        InvalidOperationException invalidOperationException = new InvalidOperationException("Invalid Account.");
        Result<?> result = handler.handle(invalidOperationException);

        assertNotNull(result);
        assertFalse(result.ok());
        assertNull(result.getCode());
        assertEquals("Invalid Account.", result.getMessage());
    }

    @Test
    void testExceptions(){
        Set<Class<? extends RuntimeException>> exceptions = new SDKExceptionHandler().exceptions();
        assertNotNull(exceptions);
        assertTrue(exceptions.contains(SDKException.class));
    }

}