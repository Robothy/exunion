package com.robothy.exunion.huobi.trade.spot;

import com.robothy.exunion.core.auth.Token;
import com.robothy.exunion.core.meta.Currency;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;
import com.robothy.exunion.huobi.meta.Huobi;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.spi.ExchangeServiceProvider;
import com.robothy.exunion.rest.spi.Options;
import com.robothy.exunion.rest.spi.OptionsBuilder;
import com.robothy.exunion.rest.spot.SpotOrderCreateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class HuobiSpotOrderCreateServiceTest {

    Options options = OptionsBuilder.create()
            .apiServer("https://api.huobi.be")
            .token(new Token("fake-key", "fake-secret"))
            .build();

    @Test
    void serviceExists(){
        Assertions.assertNotNull(ExchangeServiceProvider.newInstance(Huobi.SINGLETON, SpotOrderCreateService.class, null));
    }

    @Test
    void create() throws IOException {
        SpotOrderCreateService createService = ExchangeServiceProvider.newInstance(Huobi.SINGLETON, SpotOrderCreateService.class, options);
        SpotOrder spotOrder = SpotOrder.Builder.create()
                .side(SpotOrder.Side.SELL)
                .type(SpotOrder.Type.LIMIT)
                .quantity(new BigDecimal(10000))
                .price(new BigDecimal("0.0001"))
                .symbol(Symbol.of(Currency.PNT, Currency.BTC))
                .build();
        Result<SpotOrderDetails> result = createService.create(spotOrder);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.ok());
        Assertions.assertEquals(Result.Status.ERROR, result.getStatus());
    }

    @Test
    void batchCreate() throws IOException {
        SpotOrderCreateService createService = ExchangeServiceProvider.newInstance(Huobi.SINGLETON, SpotOrderCreateService.class, options);
        SpotOrder sell = SpotOrder.Builder.create()
                .side(SpotOrder.Side.SELL)
                .type(SpotOrder.Type.LIMIT)
                .quantity(new BigDecimal(10000))
                .price(new BigDecimal("0.0001"))
                .symbol(Symbol.of(Currency.PNT, Currency.BTC))
                .build();

        SpotOrder buy = SpotOrder.Builder.create()
                .side(SpotOrder.Side.BUY)
                .type(SpotOrder.Type.LIMIT)
                .quantity(new BigDecimal(10))
                .price(new BigDecimal("1"))
                .symbol(Symbol.of(Currency.BTC, Currency.USDT))
                .build();
        Result<List<Result<SpotOrderDetails>>> result = createService.create(Arrays.asList(sell, buy));
        Assertions.assertFalse(result.ok());
        Assertions.assertNotNull(result.getCode());
        Assertions.assertNotNull(result.getMessage());
    }
}