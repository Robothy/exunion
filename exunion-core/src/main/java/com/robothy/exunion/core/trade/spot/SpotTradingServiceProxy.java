package com.robothy.exunion.core.trade.spot;

import com.robothy.exunion.api.exception.ExchangeException;
import com.robothy.exunion.api.trade.spot.Order;
import com.robothy.exunion.api.trade.spot.SpotTradingService;

import java.io.IOException;
import java.util.Objects;

/**
 * Proxy of SpotTradingService. Some common around logic could be written in this proxy class.
 */
public class SpotTradingServiceProxy implements SpotTradingService {

    private SpotTradingService instance;

    public SpotTradingServiceProxy(SpotTradingService instance) {
        Objects.requireNonNull(instance, "The spot trading service instance shouldn't be null");
        this.instance = instance;
    }

    @Override
    public Order buy(Order order) throws IOException, ExchangeException {
        return instance.buy(order);
    }

    @Override
    public Order sell(Order order) throws IOException, ExchangeException {
        return instance.sell(order);
    }

    @Override
    public Order cancel(Order order) throws IOException, ExchangeException {
        return instance.cancel(order);
    }

    @Override
    public Order query(Order order) throws IOException, ExchangeException {
        return instance.query(order);
    }
}
