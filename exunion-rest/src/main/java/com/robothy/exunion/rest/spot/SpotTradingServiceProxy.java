package com.robothy.exunion.rest.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;

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
    public SupportedExchange exchange() {
        return this.instance.exchange();
    }

    @Override
    public SpotOrderDetails place(SpotOrder spotOrder) throws ExchangeException, IOException {
        return this.instance.place(spotOrder);
    }

    @Override
    public List<SpotOrderDetails> place(List<SpotOrder> spotOrders) throws ExchangeException, IOException {
        return this.instance.place(spotOrders);
    }

    @Override
    public SpotOrderDetails cancel(SpotOrderDetails spotOrder) throws ExchangeException, IOException {
        return this.instance.cancel(spotOrder);
    }

    @Override
    public SpotOrderDetails cancel(String orderId) throws ExchangeException, IOException {
        return this.instance.cancel(orderId);
    }

    @Override
    public List<SpotOrderDetails> cancel(List<SpotOrderDetails> spotOrders) throws ExchangeException, IOException {
        return this.instance.cancel(spotOrders);
    }

    @Override
    public SpotOrderDetails query(SpotOrderDetails spotOrder) throws ExchangeException, IOException {
        return this.instance.query(spotOrder);
    }

    @Override
    public SpotOrderDetails query(String orderId) throws ExchangeException, IOException {
        return this.instance.query(orderId);
    }

    @Override
    public List<SpotOrderDetails> query(List<SpotOrderDetails> spotOrders) throws ExchangeException, IOException {
        return this.instance.query(spotOrders);
    }
}
