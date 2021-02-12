package com.robothy.exunion.core.market;

import com.robothy.exunion.api.exception.ExchangeException;

import java.io.IOException;

public class DepthServiceProxy implements DepthService {

    private final DepthService instance;

    public DepthServiceProxy(DepthService instance){
        this.instance = instance;
    }

    @Override
    public Depth getDepth(String symbol) throws ExchangeException, IOException {
        return instance.getDepth(symbol);
    }

    @Override
    public Depth getDepth(String symbol, int depth) throws ExchangeException, IOException {
        return instance.getDepth(symbol, depth);
    }
}
