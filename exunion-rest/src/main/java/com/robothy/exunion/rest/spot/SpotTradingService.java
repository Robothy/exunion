package com.robothy.exunion.rest.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;
import com.robothy.exunion.rest.ExchangeService;

import java.io.IOException;
import java.util.List;

/**
 * Spot trading service.
 */
public interface SpotTradingService extends ExchangeService {

    SpotOrderDetails place(SpotOrder spotOrder) throws ExchangeException, IOException;

    List<SpotOrderDetails> place(List<SpotOrder> spotOrders) throws ExchangeException, IOException;

    SpotOrderDetails cancel(SpotOrderDetails spotOrder) throws ExchangeException, IOException;

    SpotOrderDetails cancel(String orderId) throws ExchangeException, IOException;

    List<SpotOrderDetails> cancel(List<SpotOrderDetails> spotOrders) throws ExchangeException, IOException;

    SpotOrderDetails query(SpotOrderDetails spotOrder) throws ExchangeException, IOException;

    SpotOrderDetails query(String orderId) throws ExchangeException, IOException;

    List<SpotOrderDetails> query(List<SpotOrderDetails> spotOrders) throws ExchangeException, IOException;

}