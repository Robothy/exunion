package com.robothy.exunion.binance.market;

import com.robothy.exunion.binance.Binance;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.market.DepthService;
import com.robothy.exunion.rest.spi.ExchangeServiceProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BinanceDepthServiceTest {

    @Test
    void getDepth() throws IOException {
        DepthService depthService = ExchangeServiceProvider.newInstance(Binance.BINANCE, DepthService.class);
        assertNotNull(depthService);
        Result<Depth> result = depthService.getDepth(Symbol.of("BTC", "USDT"));
        assertTrue(result.ok());
        assertNotNull(result.get());
        assertNotNull(result.getOrigin());
    }
}