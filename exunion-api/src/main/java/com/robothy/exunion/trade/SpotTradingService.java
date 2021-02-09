package com.robothy.exunion.trade;

/**
 * Spot trading service.
 */
interface SpotTradingService {

    Order buy(Order order);

    Order sell(Order order);

    Order cancel(Order order);

    Order query(Order order);

}