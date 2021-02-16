package com.robothy.exunion.rest.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.trade.spot.Order;
import com.robothy.exunion.rest.ExchangeService;

import java.io.IOException;
import java.util.List;

/**
 * Spot trading service.
 */
public interface SpotTradingService extends ExchangeService {

    Order place(Order order) throws ExchangeException, IOException;

    List<Order> place(List<Order> orders) throws ExchangeException, IOException;

    Order cancel(Order order) throws ExchangeException, IOException;

    List<Order> cancel(List<Order> orders) throws ExchangeException, IOException;

    Order query(Order order) throws ExchangeException, IOException;

    List<Order> query(List<Order> orders) throws ExchangeException, IOException;

}