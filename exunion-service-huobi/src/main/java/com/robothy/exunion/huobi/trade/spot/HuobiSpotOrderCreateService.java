package com.robothy.exunion.huobi.trade.spot;

import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;
import com.robothy.exunion.huobi.AbstractHuobiAuthorizedExchangeService;
import com.robothy.exunion.huobi.common.HuobiResponse;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.spot.SpotOrderCreateService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HuobiSpotOrderCreateService extends AbstractHuobiAuthorizedExchangeService implements SpotOrderCreateService {

    public static final String ORDER_PATH = "/v1/order/orders/place";

    public static final String BATCH_ORDER_PATH = "/v1/order/batch-orders";

    @Override
    public Result<SpotOrderDetails> create(SpotOrder spotOrder) throws IOException {
        Objects.requireNonNull(spotOrder, "Spot order cannot be null.");
        HuobiSpotOrder huobiSpotOrder = new HuobiSpotOrder(spotOrder);
        HuobiResponse huobiResponse = super.postWithSign(ORDER_PATH, huobiSpotOrder, HuobiResponse.class);
        Result<SpotOrderDetails> result;
        if (HuobiResponse.Status.ERROR.equals(huobiResponse.getStatus())) {
            result = new Result<>(huobiResponse.getErrCode(), huobiResponse.getErrMsg());
        } else {
            SpotOrderDetails spotOrderDetails = new SpotOrderDetails();
            spotOrderDetails.copyPropertiesFromSpotOrder(spotOrder);
            spotOrderDetails.setOrderId(huobiResponse.getData().toString());
            result = new Result<>(spotOrderDetails);
        }
        result.setOrigin(huobiResponse);
        return result;
    }

    @Override
    public Result<List<Result<SpotOrderDetails>>> create(List<SpotOrder> spotOrders) throws IOException {
        Objects.requireNonNull(spotOrders, "The spotOrders cannot be empty.");
        if (spotOrders.isEmpty()) {
            return new Result<>(new ArrayList<>());
        }

        List<HuobiSpotOrder> huobiSpotOrders = spotOrders.stream().map(HuobiSpotOrder::new).collect(Collectors.toList());
        HuobiResponse huobiResponse = super.postWithSign(BATCH_ORDER_PATH, huobiSpotOrders, HuobiResponse.class);
        if (huobiResponse.getStatus().equals(HuobiResponse.Status.ERROR)) {
            return new Result<>(huobiResponse.getErrCode(), huobiResponse.getErrMsg());
        } else {
            String dataStr = options.getJsonFactory().toString(huobiResponse.getData());
            HuobiSpotOrderDetail[] details = options.getJsonFactory().fromString(dataStr, HuobiSpotOrderDetail[].class);

            return new Result<>(Stream.of(details).map(detail -> {
                Result<SpotOrderDetails> result = new Result<>();
                if (null != detail.getErrCode()) {
                    result.setStatus(Result.Status.ERROR);
                    result.setCode(detail.getErrCode());
                    result.setMessage(detail.getErrMsg());
                } else {
                    SpotOrderDetails dtl = new SpotOrderDetails();
                    dtl.copyPropertiesFromSpotOrder(detail.toSpotOrder());
                    dtl.setOrderId(String.valueOf(detail.getOrderId()));
                    result.setData(dtl);
                    result.setStatus(Result.Status.OK);
                }
                result.set(detail.toSpotOrderDetail());
                result.setOrigin(detail);
                return result;
            }).collect(Collectors.toList()), huobiResponse);
        }
    }
}
