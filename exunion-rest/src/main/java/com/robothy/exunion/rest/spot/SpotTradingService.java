package com.robothy.exunion.rest.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.trade.spot.Order;
import com.robothy.exunion.rest.ExchangeService;

import java.io.IOException;

/**
 * Spot trading service.
 */
public interface SpotTradingService extends ExchangeService {

    Order buy(Order order) throws ExchangeException, IOException;

    Order sell(Order order) throws ExchangeException, IOException;

    Order cancel(Order order) throws ExchangeException, IOException;

    Order query(Order order) throws ExchangeException, IOException;

}