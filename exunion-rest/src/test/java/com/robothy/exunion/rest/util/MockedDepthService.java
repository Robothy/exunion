package com.robothy.exunion.rest.util;

import com.robothy.exunion.core.exception.InvalidOperationException;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.rest.AbstractExchangeService;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.market.DepthService;

import java.io.IOException;

public class MockedDepthService extends AbstractExchangeService implements DepthService {

    @Override
    public Result<Depth> getDepth(Symbol symbol) throws IOException {
        throw new InvalidOperationException("Invalid Operation.");
    }

    @Override
    public Result<Depth> getDepth(Symbol symbol, int depth) throws IOException {
        return null;
    }

    @Override
    public Exchange exchange() {
        return Huobi.SINGLETON;
    }
}