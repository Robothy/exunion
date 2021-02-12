package com.robothy.exunion.core.market;

import com.robothy.exunion.api.exception.ExchangeException;

import java.io.IOException;

public interface DepthService {

    Depth getDepth(String symbol) throws ExchangeException, IOException;

    Depth getDepth(String symbol, int depth) throws ExchangeException, IOException;

}
