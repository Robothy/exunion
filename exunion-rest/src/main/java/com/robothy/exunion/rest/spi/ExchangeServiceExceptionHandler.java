package com.robothy.exunion.rest.spi;

import com.robothy.exunion.rest.Result;

import java.util.Set;

/**
 * Handler for the exchange service operations' RuntimeException. The operation must returns
 * {@link Result}. If a RuntimeException has multiple handlers, all handler will be executed,
 * but the first non null result will be returned.
 */
public interface ExchangeServiceExceptionHandler {

    Result<?> handle(RuntimeException e);

    Set<Class<? extends RuntimeException>> exceptions();

}
