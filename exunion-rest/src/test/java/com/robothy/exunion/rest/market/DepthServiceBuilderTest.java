package com.robothy.exunion.rest.market;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.robothy.exunion.core.meta.SupportedExchange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DepthServiceBuilderTest {
    @Test
    void build() {
        DepthService depthService = DepthServiceBuilder.create()
                .exchange(SupportedExchange.HUOBI)
                .jsonFactory(new JacksonFactory())
                .build();
        Assertions.assertNotNull(depthService);
    }
}