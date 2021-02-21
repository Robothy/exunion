package com.robothy.exunion.huobi.trade.spot;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.json.JsonHttpContent;
import com.robothy.exunion.core.annotation.Version;
import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;
import com.robothy.exunion.huobi.AbstractHuobiAuthorizedExchangeService;
import com.robothy.exunion.huobi.meta.Huobi;
import com.robothy.exunion.huobi.common.HuobiExchangeError;
import com.robothy.exunion.huobi.common.HuobiResponse;
import com.robothy.exunion.huobi.util.HuobiSignUtil;
import com.robothy.exunion.rest.spi.Options;
import com.robothy.exunion.rest.spot.SpotTradingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Version("1.1")
@SuppressWarnings("rawtypes")
public class HuobiSpotService extends AbstractHuobiAuthorizedExchangeService implements SpotTradingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuobiSpotService.class);

    @Override
    public void init(Options options) {
        super.init(options);
    }

    @Override
    public Exchange exchange() {
        return Huobi.SINGLETON;
    }

    @Override
    public SpotOrderDetails place(SpotOrder spotOrder) throws ExchangeException, IOException {
        Objects.requireNonNull(spotOrder, "Spot order cannot be null.");
        return this.execute(()->{
            String url = sign(HttpMethods.POST, "/v1/order/orders/place", null);
            HuobiSpotOrder huobiSpotOrder = new HuobiSpotOrder(spotOrder);
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("Request URL: \n" + url);
                LOGGER.debug("Request Data: \n" + options.getJsonFactory().toPrettyString(huobiSpotOrder));
            }

            HttpContent content = new JsonHttpContent(options.getJsonFactory(), huobiSpotOrder);
            HttpResponse response = options.getHttpRequestFactory().buildPostRequest(new GenericUrl(url), content).execute();
            HuobiResponse huobiResponse = response.parseAs(HuobiResponse.class);

            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("Parsed Response: \n" + options.getJsonFactory().toPrettyString(huobiResponse));
            }

            if(HuobiResponse.Status.ERROR.equals(huobiResponse.getStatus())){
                throw new ExchangeException(HuobiExchangeError.of(huobiResponse.getErrCode(), huobiResponse.getErrMsg()));
            }

            SpotOrderDetails spotOrderDetails = new SpotOrderDetails();
            spotOrderDetails.copyPropertiesFromSpotOrder(spotOrder);
            spotOrderDetails.setOrderId(huobiResponse.getData().toString());
            return spotOrderDetails;
        });
    }

    /**
     *
     * @param spotOrders the spot orders to be placed
     * @return the results of each order. <b>Notice: </b> some orders may successfully placed while others are not.
     * The developer should check the result for each order by calling {@link HuobiResponse#getErrCode()}. It returns <code>null</code>
     * means the order was placed successfully; otherwise, you may need to extract the error message through invoking
     * ${@link HuobiResponse#getErrMsg()} and handle the error.
     *
     * @throws ExchangeException never threw, the developer needn't to handle it.
     * @throws IOException when network occurs an error.
     */
    @Override
    public List<SpotOrderDetails> place(List<SpotOrder> spotOrders) throws ExchangeException, IOException {
        if(spotOrders == null || spotOrders.isEmpty()) throw new NullPointerException("The spotOrders cannot be empty.");
        return this.execute(()->{
            String url = sign(HttpMethods.POST, "/v1/order/batch-orders", null);
            List<HuobiSpotOrder> huobiSpotOrders = spotOrders.stream().map(HuobiSpotOrder::new).collect(Collectors.toList());
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("Request URL: \n" + url);
                LOGGER.debug("Request Data: \n" + options.getJsonFactory().toPrettyString(huobiSpotOrders));
            }

            JsonHttpContent content = new JsonHttpContent(options.getJsonFactory(), huobiSpotOrders);
            HuobiSpotOrderDetail[] details = options.getHttpRequestFactory().buildPostRequest(new GenericUrl(url), content)
                    .execute()
                    .parseAs(HuobiSpotOrderDetail[].class);

            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("Parsed Data: \n" + options.getJsonFactory().toPrettyString(details));
            }

            List<SpotOrderDetails> result = new ArrayList<>(details.length);
            for (HuobiSpotOrderDetail huobiSpotOrderDetail : details){
                result.add(huobiSpotOrderDetail.toSpotOrderDetail());
            }
            return result;
        });
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
            return options.getExecutor().submit(task).get();
        } catch (InterruptedException e) {
            throw new ExchangeException("HUOBI spot trading operation is interrupted. message: " + e.getMessage());
        } catch (ExecutionException e) {
            if(e.getCause() instanceof ExchangeException) throw (ExchangeException) e.getCause();
            if (e.getCause() instanceof IOException) throw (IOException) e.getCause();
            throw new RuntimeException("Unknown exception occurred when execute HUOBI trading service.", e);
        }
    }

    private String sign(String method, String path, Map<String, Object> params){
        return HuobiSignUtil.sign(method, options.getApiServer(), path, options.getToken().getApiKey(), options.getToken().getApiSecret(), params);
    }

}
