package com.robothy.exunion.rest.util;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.rest.AbstractExchangeService;
import com.robothy.exunion.rest.market.DepthService;

import java.io.IOException;

public class MockedDepthService extends AbstractExchangeService implements DepthService {

    @Override
    public Depth getDepth(Symbol symbol) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public Depth getDepth(Symbol symbol, int depth) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public Exchange exchange() {
        return Huobi.SINGLETON;
    }
}