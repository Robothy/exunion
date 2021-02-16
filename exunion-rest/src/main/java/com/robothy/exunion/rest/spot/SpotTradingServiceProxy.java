package com.robothy.exunion.rest.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.trade.spot.Order;

import java.io.IOException;
import java.util.List;
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
    public Order place(Order order) throws ExchangeException, IOException {
        return this.instance.place(order);
    }

    @Override
    public List<Order> place(List<Order> orders) throws ExchangeException, IOException {
        return this.instance.place(orders);
    }

    @Override
    public Order cancel(Order order) throws ExchangeException, IOException {
        return this.instance.cancel(order);
    }

    @Override
    public List<Order> cancel(List<Order> orders) throws ExchangeException, IOException {
        return this.instance.cancel(orders);
    }

    @Override
    public Order query(Order order) throws ExchangeException, IOException {
        return this.instance.query(order);
    }

    @Override
    public List<Order> query(List<Order> orders) throws ExchangeException, IOException {
        return this.instance.query(orders);
    }

    @Override
    public SupportedExchange exchange() {
        return this.instance.exchange();
    }
}
