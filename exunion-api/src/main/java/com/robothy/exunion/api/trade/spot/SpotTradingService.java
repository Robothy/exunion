package com.robothy.exunion.api.trade.spot;

import com.robothy.exunion.api.exception.ExchangeException;

import java.io.IOException;

/**
 * Spot trading service.
 */
public interface SpotTradingService {

    Order buy(Order order) throws ExchangeException, IOException;

    Order sell(Order order) throws ExchangeException, IOException;

    Order cancel(Order order) throws ExchangeException, IOException;

    Order query(Order order) throws ExchangeException, IOException;

}