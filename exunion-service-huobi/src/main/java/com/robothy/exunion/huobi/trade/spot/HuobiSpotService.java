package com.robothy.exunion.huobi.trade.spot;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.json.JsonHttpContent;
import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;
import com.robothy.exunion.rest.spot.AbstractSpotTradingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("rawtypes")
public class HuobiSpotService extends AbstractSpotTradingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuobiSpotService.class);

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
            HuobiSpotOrder huobiSpotOrder = new HuobiSpotOrder(spotOrder);
            HttpContent content = new JsonHttpContent(super.getJsonFactory(), huobiSpotOrder);
            HttpResponse response = super.requestFactory.buildPostRequest(new GenericUrl(url), content).execute();
            if(response.getStatusCode() != HttpStatusCodes.STATUS_CODE_OK){
                LOGGER.error(getJsonFactory().fromInputStream(response.getContent(), String.class));
            }

            SpotOrderDetails spotOrderDetails = new SpotOrderDetails();
            spotOrderDetails.copyPropertiesFromSpotOrder(spotOrder);
            Map resp = response.parseAs(Map.class);
            spotOrderDetails.setOrderId(resp.get("data").toString());
            return spotOrderDetails;
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
