package com.robothy.exunion.huobi.trade.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.rest.spot.AbstractSpotTradingService;

import java.io.IOException;
import java.util.List;

public class HuobiSpotService extends AbstractSpotTradingService {

    @Override
    public void init() {

    }

    @Override
    public SupportedExchange exchange() {
        return SupportedExchange.HUOBI;
    }

    @Override
    public SpotOrder place(SpotOrder spotOrder) throws ExchangeException, IOException {
        String.format("%s/v1/order/spotOrders/place", SupportedExchange.HUOBI.getDefaultApiServer());
        return null;
    }

    @Override
    public List<SpotOrder> place(List<SpotOrder> spotOrders) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public SpotOrder cancel(SpotOrder spotOrder) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public List<SpotOrder> cancel(List<SpotOrder> spotOrders) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public SpotOrder query(SpotOrder spotOrder) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public List<SpotOrder> query(List<SpotOrder> spotOrders) throws ExchangeException, IOException {
        return null;
    }
}
