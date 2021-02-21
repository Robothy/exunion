package com.robothy.exunion.huobi.trade.spot;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.json.JsonHttpContent;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;
import com.robothy.exunion.huobi.AbstractHuobiAuthorizedExchangeService;
import com.robothy.exunion.huobi.common.HuobiResponse;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.spot.SpotOrderCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HuobiSpotOrderCreateService extends AbstractHuobiAuthorizedExchangeService implements SpotOrderCreateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuobiSpotOrderCreateService.class);

    @Override
    public Result<SpotOrderDetails> create(SpotOrder spotOrder) throws IOException {
        Objects.requireNonNull(spotOrder, "Spot order cannot be null.");
        String url = signedUrl(HttpMethods.POST, "/v1/order/orders/place", null);
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

        Result<SpotOrderDetails> result;
        if(HuobiResponse.Status.ERROR.equals(huobiResponse.getStatus())){
            result = new Result<>(huobiResponse.getErrCode(), huobiResponse.getErrMsg());
        }else{
            SpotOrderDetails spotOrderDetails = new SpotOrderDetails();
            spotOrderDetails.copyPropertiesFromSpotOrder(spotOrder);
            spotOrderDetails.setOrderId(huobiResponse.getData().toString());
            result = new Result<>(spotOrderDetails);
        }
        return result;
    }

    @Override
    public List<Result<SpotOrderDetails>> create(List<SpotOrder> spotOrders) throws IOException {
        if(spotOrders == null || spotOrders.isEmpty()) throw new NullPointerException("The spotOrders cannot be empty.");
        String url = signedUrl(HttpMethods.POST, "/v1/order/batch-orders", null);
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

        List<Result<SpotOrderDetails>> results = new ArrayList<>(details.length);
        for (HuobiSpotOrderDetail huobiSpotOrderDetail : details){
            Result<SpotOrderDetails> result = new Result<>(huobiSpotOrderDetail.getErrCode(),  huobiSpotOrderDetail.getErrMsg());
            result.set(huobiSpotOrderDetail.toSpotOrderDetail());
            result.setOrigin(huobiSpotOrderDetail);
            results.add(result);
        }
        return results;
    }

}
