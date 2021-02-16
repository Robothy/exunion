package com.robothy.exunion.huobi.trade.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.trade.spot.Order;
import com.robothy.exunion.rest.spot.SpotTradingService;

import java.io.IOException;
import java.util.List;

public class HuobiSpotService implements SpotTradingService {

    @Override
    public SupportedExchange exchange() {
        return null;
    }

    @Override
    public Order place(Order order) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public List<Order> place(List<Order> orders) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public Order cancel(Order order) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public List<Order> cancel(List<Order> orders) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public Order query(Order order) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public List<Order> query(List<Order> orders) throws ExchangeException, IOException {
        return null;
    }
}
