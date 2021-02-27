package com.robothy.exunion.rest.util;

import com.robothy.exunion.core.exception.InvalidOperationException;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.spi.ExchangeServiceExceptionHandler;

import java.util.HashSet;
import java.util.Set;

public class MockInvalidOperationHandler implements ExchangeServiceExceptionHandler {

    @Override
    public Result<?> handle(RuntimeException e) {
        return new Result<>("invalid-operation", e.getMessage());
    }

    @Override
    public Set<Class<? extends RuntimeException>> exceptions() {
        Set<Class<? extends RuntimeException>> handlers = new HashSet<>();
        handlers.add(InvalidOperationException.class);
        return handlers;
    }
}
