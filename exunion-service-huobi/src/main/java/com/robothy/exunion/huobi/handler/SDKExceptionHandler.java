package com.robothy.exunion.huobi.handler;

import com.huobi.exception.SDKException;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.spi.ExchangeServiceExceptionHandler;

import java.util.Collections;
import java.util.Set;

public class SDKExceptionHandler implements ExchangeServiceExceptionHandler {

    @Override
    public Result<?> handle(RuntimeException e) {
        if(e instanceof SDKException){
            SDKException ex = (SDKException) e;
            return new Result<>(ex.getErrCode(), ex.getMessage());
        }else {
            return new Result<>(null, e.getMessage());
        }
    }

    @Override
    public Set<Class<? extends RuntimeException>> exceptions() {
        return Collections.singleton(SDKException.class);
    }
}
