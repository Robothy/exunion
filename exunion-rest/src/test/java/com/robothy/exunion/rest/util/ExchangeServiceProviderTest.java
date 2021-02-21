package com.robothy.exunion.rest.util;

import com.robothy.exunion.core.exception.ServiceNotFoundException;
import com.robothy.exunion.rest.market.DepthService;
import com.robothy.exunion.rest.spi.ExchangeServiceProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExchangeServiceProviderTest {
    @Test
    void newInstance() {
        DepthService depthService = ExchangeServiceProvider.newInstance(Huobi.SINGLETON, DepthService.class);
        Assertions.assertNotNull(depthService);
        Assertions.assertThrows(ServiceNotFoundException.class, () -> ExchangeServiceProvider.newInstance(Unknown.SINGLETON, DepthService.class));
    }
}