package com.robothy.exunion.rest.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.trade.spot.SpotOrder;

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
    public SpotOrder place(SpotOrder spotOrder) throws ExchangeException, IOException {
        return this.instance.place(spotOrder);
    }

    @Override
    public List<SpotOrder> place(List<SpotOrder> spotOrders) throws ExchangeException, IOException {
        return this.instance.place(spotOrders);
    }

    @Override
    public SpotOrder cancel(SpotOrder spotOrder) throws ExchangeException, IOException {
        return this.instance.cancel(spotOrder);
    }

    @Override
    public List<SpotOrder> cancel(List<SpotOrder> spotOrders) throws ExchangeException, IOException {
        return this.instance.cancel(spotOrders);
    }

    @Override
    public SpotOrder query(SpotOrder spotOrder) throws ExchangeException, IOException {
        return this.instance.query(spotOrder);
    }

    @Override
    public List<SpotOrder> query(List<SpotOrder> spotOrders) throws ExchangeException, IOException {
        return this.instance.query(spotOrders);
    }

    @Override
    public SupportedExchange exchange() {
        return this.instance.exchange();
    }
}
