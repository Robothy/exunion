package com.robothy.exunion.huobi.market;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.Currency;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.rest.market.DepthService;
import com.robothy.exunion.rest.market.DepthServiceBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class HuobiDepthServiceTest {

    private DepthService depthService = DepthServiceBuilder
            .create()
            .exchange(SupportedExchange.HUOBI)
            .httpTransport(new NetHttpTransport())
            .jsonFactory(new JacksonFactory())
            .build();


    @Test
    void getDepth() throws IOException, ExchangeException {
        Depth depth = depthService.getDepth(HuobiSymbol.of(Currency.ETH, Currency.USDT));
        Assertions.assertNotNull(depth);
        Assertions.assertEquals(Currency.ETH, depth.getSymbol().getBase());
        Assertions.assertEquals(Currency.USDT, depth.getSymbol().getQuote());
        Assertions.assertNotNull(depth.getTimestamp());
        Assertions.assertNotNull(depth.getAsks());
        Assertions.assertNotNull(depth.getBids());
        Assertions.assertTrue(depth.getAsks().get(0).getPrice().compareTo(depth.getBids().get(0).getPrice()) > 0);
    }

    @Test
    void testGetDepth() throws IOException, ExchangeException {
        Depth depth = depthService.getDepth(HuobiSymbol.of(Currency.ETH, Currency.BTC), 10);
        Assertions.assertTrue(depth.getAsks().size() <= 10);
        Assertions.assertTrue(depth.getBids().size() <= 10);
    }

    @Test
    void getDepthWithInvalidSymbol() {
        Assertions.assertThrows(ExchangeException.class, () -> depthService.getDepth(HuobiSymbol.of(Currency.BTC, Currency.BTC)));
    }
}