package com.robothy.exunion.api.service.trade.spot;

import com.robothy.exunion.api.trade.spot.Order;
import com.robothy.exunion.api.trade.spot.SpotTradingService;

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
    public Order buy(Order order) {
        return instance.buy(order);
    }

    @Override
    public Order sell(Order order) {
        return instance.sell(order);
    }

    @Override
    public Order cancel(Order order) {
        return instance.cancel(order);
    }

    @Override
    public Order query(Order order) {
        return instance.query(order);
    }
}
