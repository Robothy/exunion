package com.robothy.exunion.rest.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.rest.ExchangeService;

import java.io.IOException;
import java.util.List;

/**
 * Spot trading service.
 */
public interface SpotTradingService extends ExchangeService {

    SpotOrder place(SpotOrder spotOrder) throws ExchangeException, IOException;

    List<SpotOrder> place(List<SpotOrder> spotOrders) throws ExchangeException, IOException;

    SpotOrder cancel(SpotOrder spotOrder) throws ExchangeException, IOException;

    List<SpotOrder> cancel(List<SpotOrder> spotOrders) throws ExchangeException, IOException;

    SpotOrder query(SpotOrder spotOrder) throws ExchangeException, IOException;

    List<SpotOrder> query(List<SpotOrder> spotOrders) throws ExchangeException, IOException;

}