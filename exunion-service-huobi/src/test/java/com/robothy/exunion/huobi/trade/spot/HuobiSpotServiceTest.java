package com.robothy.exunion.huobi.trade.spot;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.meta.Currency;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.rest.spot.SpotTradingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

class HuobiSpotServiceTest {

    @Test
    void place() {
        SpotOrder spotOrder = new SpotOrder();
        spotOrder.setType(SpotOrder.Type.LIMIT);
        spotOrder.setSide(SpotOrder.Side.BUY);
        spotOrder.setSymbol(Symbol.of(Currency.BTC, Currency.USDT));
        spotOrder.setPrice(new BigDecimal(2000));
        spotOrder.setQuantity(new BigDecimal(100));

        Map<String, Object> extra = new HashMap<>();
        extra.put("account-id", "A001");
        extra.put("client-order-id", "T001");
        extra.put("source", "exunion-api");
        spotOrder.setExtraInfo(extra);

//        SpotTradingService spotTradingService = SpotTradingServiceBuilder.create()
//                .exchange(SupportedExchange.HUOBI)
//                .jsonFactory(new JacksonFactory())
//                .apiServer("https://api.huobi.be")
//                .apiKey("fake5drtmh-fakef8dc-fake225d-fakeb")
//                .apiSecret("155bfake-8a16fake-efaafake-bfake")
//                .build();
//        Assertions.assertThrows(ExchangeException.class, () -> spotTradingService.place(spotOrder));
    }

    @Test
    void testPlace() {
    }

    @Test
    void cancel() {
    }

    @Test
    void testCancel() {
    }

    @Test
    void testCancel1() {
    }

    @Test
    void query() {
    }

    @Test
    void testQuery() {
    }

    @Test
    void testQuery1() {
    }
}