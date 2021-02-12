package com.robothy.exunion.api.trade.spot;

import com.robothy.exunion.api.exception.TradingException;
import java.io.IOException;

/**
 * Spot trading service.
 */
public interface SpotTradingService {

    Order buy(Order order) throws TradingException, IOException;

    Order sell(Order order) throws TradingException, IOException;

    Order cancel(Order order) throws TradingException, IOException;

    Order query(Order order) throws TradingException, IOException;

}