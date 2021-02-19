package com.robothy.exunion.huobi.trade.spot;

import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;
import com.robothy.exunion.rest.spot.AbstractSpotTradingService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class HuobiSpotService extends AbstractSpotTradingService {

    @Override
    public void init() {

    }

    @Override
    public SupportedExchange exchange() {
        return SupportedExchange.HUOBI;
    }

    @Override
    public SpotOrderDetails place(SpotOrder spotOrder) throws ExchangeException, IOException {
        return this.execute(()->{
            String url = String.format("%s/v1/order/spotOrders/place", SupportedExchange.HUOBI.getDefaultApiServer());

            return (SpotOrderDetails) null;
        });
    }

    @Override
    public List<SpotOrderDetails> place(List<SpotOrder> spotOrders) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public SpotOrderDetails cancel(SpotOrderDetails spotOrder) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public SpotOrderDetails cancel(String orderId) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public List<SpotOrderDetails> cancel(List<SpotOrderDetails> spotOrders) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public SpotOrderDetails query(SpotOrderDetails spotOrder) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public SpotOrderDetails query(String orderId) throws ExchangeException, IOException {
        return null;
    }

    @Override
    public List<SpotOrderDetails> query(List<SpotOrderDetails> spotOrders) throws ExchangeException, IOException {
        return null;
    }



    private <V> V execute(Callable<V> task) throws ExchangeException, IOException {
        try {
            return getExecutor().submit(task).get();
        } catch (InterruptedException e) {
            throw new ExchangeException("HUOBI spot trading operation is interrupted. message: " + e.getMessage());
        } catch (ExecutionException e) {
            if(e.getCause() instanceof ExchangeException) throw (ExchangeException) e.getCause();
            if (e.getCause() instanceof IOException) throw (IOException) e.getCause();
            throw new RuntimeException("Unknown exception occurred when execute HUOBI trading service.");
        }
    }
}
