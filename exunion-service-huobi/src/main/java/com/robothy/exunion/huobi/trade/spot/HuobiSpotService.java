package com.robothy.exunion.huobi.trade.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.trade.spot.Order;
import com.robothy.exunion.rest.spot.AbstractSpotTradingService;

import java.io.IOException;
import java.util.List;

public class HuobiSpotService extends AbstractSpotTradingService {

    @Override
    public void init() {
        if(super.getApiServer()==null){
            super.setApiServer(exchange().getDefaultApiServer());
        }
    }

    @Override
    public SupportedExchange exchange() {
        return SupportedExchange.HUOBI;
    }

    @Override
    public Order place(Order order) throws ExchangeException, IOException {
        String.format("%s/v1/order/orders/place", SupportedExchange.HUOBI.getDefaultApiServer());
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
