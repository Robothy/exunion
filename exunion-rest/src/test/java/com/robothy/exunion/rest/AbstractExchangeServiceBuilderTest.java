package com.robothy.exunion.rest;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.testing.http.MockHttpTransport;
import com.robothy.exunion.core.auth.Token;
import com.robothy.exunion.core.exception.InvalidOperationException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.rest.market.DepthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class AbstractExchangeServiceBuilderTest {

    private MockedDepthServiceBuilder builder;

    @BeforeEach
    void setUp() {
        this.builder = new MockedDepthServiceBuilder();
    }

    @Test
    void exchange() {
        Assertions.assertNull(builder.abstractExchangeService);
        builder.exchange(SupportedExchange.HUOBI, DepthService.class);
        Assertions.assertNotNull(builder.abstractExchangeService);
        Assertions.assertEquals(SupportedExchange.HUOBI, builder.abstractExchangeService.exchange());
    }

    @Test
    void apiKey() {
        builder.exchange(SupportedExchange.HUOBI, DepthService.class)
                .apiKey("API-KEY");
        Assertions.assertEquals("API-KEY", builder.abstractExchangeService.getToken().getApiKey());
    }

    @Test
    void apiSecret() {
        builder.exchange(SupportedExchange.HUOBI, DepthService.class)
                .apiSecret("API-SECRET");
        Assertions.assertEquals("API-SECRET", builder.abstractExchangeService.getToken().getApiSecret());
    }

    @Test
    void token() {
        Token token = new Token("key", "secret");
        builder.exchange(SupportedExchange.HUOBI, DepthService.class)
                .token(token);
        Assertions.assertEquals(token, builder.abstractExchangeService.getToken());
    }

    @Test
    void httpTransport() {
        builder.exchange(SupportedExchange.HUOBI, DepthService.class)
                .jsonFactory(new JacksonFactory())
                .build();
        Assertions.assertSame(NetHttpTransport.class, builder.abstractExchangeService.getHttpTransport().getClass());

        builder.httpTransport(new MockHttpTransport())
                .build();
        Assertions.assertSame(MockHttpTransport.class, builder.abstractExchangeService.getHttpTransport().getClass());
    }

    @Test
    void jsonFactory() {
        builder.exchange(SupportedExchange.HUOBI, DepthService.class)
                .jsonFactory(new JacksonFactory());
        Assertions.assertSame(JacksonFactory.class, builder.abstractExchangeService.getJsonFactory().getClass());
    }

    @Test
    void extraProperties() {
        Map<String, Object> extra = new HashMap<>();
        builder.exchange(SupportedExchange.HUOBI, DepthService.class)
                .extraProperties(extra);
        Assertions.assertSame(extra, builder.abstractExchangeService.getExtraProperties());
    }

    @Test
    void apiServer() {
        builder.exchange(SupportedExchange.HUOBI, DepthService.class)
                .jsonFactory(new JacksonFactory())
                .build();
        Assertions.assertEquals(SupportedExchange.HUOBI.getDefaultApiServer(), builder.abstractExchangeService.getApiServer());

        String server = "https://api.example.com";
        builder.exchange(SupportedExchange.HUOBI, DepthService.class)
                .apiServer(server);
        Assertions.assertSame(server, builder.abstractExchangeService.getApiServer());
    }

    @Test
    void build() {
        Assertions.assertThrows(InvalidOperationException.class, () -> builder.build());

        DepthService instance = builder.exchange(SupportedExchange.HUOBI, DepthService.class)
                .jsonFactory(new JacksonFactory())
                .build();
        Assertions.assertNotNull(instance);
        Assertions.assertNotNull(builder.abstractExchangeService.getRequestFactory());
    }
}

class MockedDepthServiceBuilder extends AbstractExchangeServiceBuilder<MockedDepthServiceBuilder> {

    @Override
    protected DepthService build() {
        super.build();
        return (DepthService) super.exchangeService;
    }
}