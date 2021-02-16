package com.robothy.exunion.rest.util;

import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.rest.market.DepthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExchangeServiceProviderTest {
    @Test
    void newInstance() {
        DepthService depthService = ExchangeServiceProvider.newInstance(SupportedExchange.HUOBI, DepthService.class);
        Assertions.assertNotNull(depthService);
    }
}