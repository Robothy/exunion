package com.robothy.exunion.rest.market;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.SupportedExchange;

import java.io.IOException;

public class DepthServiceProxy implements DepthService {

    private final DepthService instance;

    public DepthServiceProxy(DepthService instance){
        this.instance = instance;
    }

    @Override
    public Depth getDepth(String symbol) throws ExchangeException, IOException {
        return this.instance.getDepth(symbol);
    }

    @Override
    public Depth getDepth(String symbol, int depth) throws ExchangeException, IOException {
        return this.instance.getDepth(symbol, depth);
    }

    @Override
    public SupportedExchange exchange() {
        return this.instance.exchange();
    }
}
