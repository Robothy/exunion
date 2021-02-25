package com.robothy.exunion.huobi.trade.spot;

import com.huobi.client.TradeClient;
import com.huobi.client.req.trade.CreateOrderRequest;
import com.huobi.constant.HuobiOptions;
import com.huobi.constant.enums.OrderTypeEnum;
import com.robothy.exunion.core.annotation.Version;
import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;
import com.robothy.exunion.huobi.AbstractHuobiAuthorizedExchangeService;
import com.robothy.exunion.huobi.meta.Huobi;
import com.robothy.exunion.rest.spi.Options;
import com.robothy.exunion.rest.spot.SpotTradingService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Version("1.2")
public class HuobiSpotTradingAdapter extends AbstractHuobiAuthorizedExchangeService implements SpotTradingService {

    private TradeClient tradeClient;

    @Override
    public void init(Options options) {
        super.init(options);
        HuobiOptions huobiOptions = HuobiOptions.builder()
                .apiKey(options.getToken().getApiKey())
                .secretKey(options.getToken().getApiSecret())
                .restHost(options.getApiServer())
                .build();
        this.tradeClient = TradeClient.create(huobiOptions);
    }

    @Override
    public Exchange exchange() {
        return Huobi.SINGLETON;
    }

    @Override
    public SpotOrderDetails place(SpotOrder spotOrder) throws ExchangeException, IOException {
        Objects.requireNonNull(spotOrder, "The spot order shouldn't be null.");
        Objects.requireNonNull(spotOrder.getExtraInfo(), "Extra Info shouldn't be null, the 'account-id' should be passed through extraInfo.");
        CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
                .accountId(Long.valueOf(spotOrder.getExtraInfo().get("account-id").toString()))
                .type(OrderTypeEnum.find(HuobiOrderTypeUtil.of(spotOrder.getSide(), spotOrder.getType())))
                .build();

        Long orderId = this.tradeClient.createOrder(createOrderRequest);
        if(null == orderId) throw new ExchangeException("Failed to place order.");
        SpotOrderDetails spotOrderDetails = new SpotOrderDetails();
        spotOrderDetails.copyPropertiesFromSpotOrder(spotOrder);
        spotOrderDetails.setOrderId(orderId.toString());
        return spotOrderDetails;
    }

    @Override
    public List<SpotOrderDetails> place(List<SpotOrder> spotOrders) throws ExchangeException, IOException {
        throw new UnsupportedOperationException("Unsupported creating batch orders.");
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
}
