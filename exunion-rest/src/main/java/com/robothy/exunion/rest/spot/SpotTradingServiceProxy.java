package com.robothy.exunion.rest.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.trade.spot.Order;

import java.io.IOException;
import java.util.Objects;

/**
 * Proxy of SpotTradingService. Some common around logic could be written in this proxy class.
 */
public class SpotTradingServiceProxy implements SpotTradingService {

    private final SpotTradingService instance;

    public SpotTradingServiceProxy(SpotTradingService instance) {
        Objects.requireNonNull(instance, "The spot trading service instance shouldn't be null");
        this.instance = instance;
    }

    @Override
    public Order buy(Order order) throws ExchangeException, IOException {
        return this.instance.buy(order);
    }

    @Override
    public Order sell(Order order) throws ExchangeException, IOException {
        return this.instance.sell(order);
    }

    @Override
    public Order cancel(Order order) throws ExchangeException, IOException {
        return this.instance.cancel(order);
    }

    @Override
    public Order query(Order order) throws ExchangeException, IOException {
        return this.instance.query(order);
    }


    @Override
    public SupportedExchange exchange() {
        return this.instance.exchange();
    }
}
