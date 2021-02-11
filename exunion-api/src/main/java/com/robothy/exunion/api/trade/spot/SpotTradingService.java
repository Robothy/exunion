package com.robothy.exunion.api.trade.spot;

/**
 * Spot trading service.
 */
public interface SpotTradingService {

    Order buy(Order order);

    Order sell(Order order);

    Order cancel(Order order);

    Order query(Order order);

}