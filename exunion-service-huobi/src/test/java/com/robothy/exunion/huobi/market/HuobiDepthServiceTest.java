package com.robothy.exunion.huobi.market;

import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.Currency;
import com.robothy.exunion.huobi.meta.Huobi;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.market.DepthService;
import com.robothy.exunion.rest.spi.ExchangeServiceProvider;
import com.robothy.exunion.rest.spi.Options;
import com.robothy.exunion.rest.spi.OptionsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class HuobiDepthServiceTest {

    private final Options options = OptionsBuilder.create()
            .apiServer("https://api.huobi.be")
            .build();

    private final DepthService depthService = ExchangeServiceProvider.newInstance(Huobi.SINGLETON, DepthService.class, options);

    @Test
    void getDepth() throws IOException {
        Depth depth = depthService.getDepth(HuobiSymbol.of(Currency.ETH, Currency.USDT)).get();
        Assertions.assertNotNull(depth);
        Assertions.assertEquals(Currency.ETH, depth.getSymbol().getBase());
        Assertions.assertEquals(Currency.USDT, depth.getSymbol().getQuote());
        Assertions.assertNotNull(depth.getTimestamp());
        Assertions.assertNotNull(depth.getAsks());
        Assertions.assertNotNull(depth.getBids());
        Assertions.assertTrue(depth.getAsks().get(0).getPrice().compareTo(depth.getBids().get(0).getPrice()) > 0);
    }

    @Test
    void testGetDepth() throws IOException {
        Result<Depth> result = depthService.getDepth(HuobiSymbol.of(Currency.ETH, Currency.BTC), 10);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Result.Status.OK, result.getStatus());
        Assertions.assertNull(result.getCode());
        Assertions.assertNull(result.getMessage());
        Assertions.assertNotNull(result.getOrigin());
        Assertions.assertTrue(result.getOrigin() instanceof HuobiDepth);

        Depth depth = result.get();
        Assertions.assertNotNull(depth);
        Assertions.assertTrue(depth.getAsks().size() <= 10);
        Assertions.assertTrue(depth.getBids().size() <= 10);
    }

    @Test
    void getDepthWithInvalidSymbol() throws IOException {
        Result<Depth> result = depthService.getDepth(HuobiSymbol.of(Currency.BTC, Currency.BTC));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Result.Status.ERROR, result.getStatus());
        Assertions.assertNotNull(result.getOrigin());
        Assertions.assertTrue(result.getOrigin() instanceof HuobiDepth);
        Assertions.assertNotNull(result.getCode());
        Assertions.assertNotNull(result.getMessage());
    }
}