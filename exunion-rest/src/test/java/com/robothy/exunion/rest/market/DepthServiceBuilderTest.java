package com.robothy.exunion.rest.market;

import com.robothy.exunion.core.meta.SupportedExchange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DepthServiceBuilderTest {
    @Test
    void build() {
        DepthService depthService = DepthServiceBuilder.create()
                .exchange(SupportedExchange.HUOBI)
                .build();
        Assertions.assertNotNull(depthService);
    }
}