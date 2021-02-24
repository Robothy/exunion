package com.robothy.exunion.huobi.market;

import com.google.api.client.http.HttpMethods;
import com.huobi.service.huobi.HuobiMarketService;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.IOException;

@MockServerSettings(ports = 8080, perTestSuite = true)
@ExtendWith(MockServerExtension.class)
class HuobiDepthServiceTest {

    private final Options options = OptionsBuilder.create()
            .apiServer("http://localhost")
            .build();

    private final HttpRequest request = HttpRequest.request()
            .withPath(HuobiMarketService.REST_MARKET_DEPTH_PATH)
            .withMethod(HttpMethods.GET);

    private final DepthService depthService = ExchangeServiceProvider.newInstance(Huobi.SINGLETON, DepthService.class, options);

    @Test
    void getDepth(MockServerClient serverClient) throws IOException {
        serverClient.when(request)
                .respond(
                        HttpResponse.response()
                        .withBody("{\n" +
                                "  \"ch\" : \"market.ethusdt.depth.step1\",\n" +
                                "  \"status\" : \"ok\",\n" +
                                "  \"tick\" : {\n" +
                                "    \"asks\" : [ [ 1654.3, 0.0302 ], [ 1654.4, 1.2769 ], [ 1654.5, 2.0005 ], [ 1654.6, 1.5423 ], [ 1654.7, 2.5162 ], [ 1654.8, 1.8136 ], [ 1654.9, 9.0634 ], [ 1655.0, 18.5147 ], [ 1655.1, 5.9794 ], [ 1655.2, 1.2778 ], [ 1655.3, 36.4727 ], [ 1655.4, 2.7923 ], [ 1655.5, 2.0952 ], [ 1655.6, 23.4293 ], [ 1655.7, 2.4426 ], [ 1655.8, 5.9477 ], [ 1655.9, 11.5553 ], [ 1656.0, 9.6293 ], [ 1656.1, 1.1938 ], [ 1656.2, 0.3121 ] ],\n" +
                                "    \"bids\" : [ [ 1653.9, 2.3807 ], [ 1653.7, 1.9567 ], [ 1653.6, 0.4389 ], [ 1653.5, 1.5971 ], [ 1653.3, 1.987 ], [ 1653.2, 0.7675 ], [ 1653.1, 29.9057 ], [ 1653.0, 3.699 ], [ 1652.9, 0.1698 ], [ 1652.8, 2.332 ], [ 1652.7, 4.6474 ], [ 1652.6, 5.3976 ], [ 1652.5, 1.8134 ], [ 1652.4, 1.2125 ], [ 1652.3, 5.3792 ], [ 1652.2, 26.7104 ], [ 1652.1, 25.1649 ], [ 1652.0, 2.7375 ], [ 1651.9, 8.205 ], [ 1651.8, 3.6483 ] ],\n" +
                                "    \"ts\" : 1614173004822,\n" +
                                "    \"version\" : 118976597431\n" +
                                "  },\n" +
                                "  \"ts\" : 1614173005733\n" +
                                "}")
                );

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
    void testGetDepth(MockServerClient serverClient) throws IOException {
        serverClient.when(request).respond(
                HttpResponse.response()
                .withBody("{\n" +
                        "  \"ch\" : \"market.ethbtc.depth.step1\",\n" +
                        "  \"status\" : \"ok\",\n" +
                        "  \"tick\" : {\n" +
                        "    \"asks\" : [ [ 0.03317, 62.4391 ], [ 0.03318, 6.2941 ], [ 0.03319, 7.8996 ], [ 0.0332, 16.848 ], [ 0.03321, 17.953 ], [ 0.03322, 21.3888 ], [ 0.03323, 4.5202 ], [ 0.03324, 83.8279 ], [ 0.03325, 0.8152 ], [ 0.03326, 1.7117 ] ],\n" +
                        "    \"bids\" : [ [ 0.03315, 0.8189 ], [ 0.03314, 15.3904 ], [ 0.03313, 23.1709 ], [ 0.03312, 13.2939 ], [ 0.03311, 15.8843 ], [ 0.0331, 15.0719 ], [ 0.03309, 12.212 ], [ 0.03308, 68.0363 ], [ 0.03307, 3.007 ], [ 0.03306, 3.3868 ] ],\n" +
                        "    \"ts\" : 1614173003984,\n" +
                        "    \"version\" : 110627886979\n" +
                        "  },\n" +
                        "  \"ts\" : 1614173004084\n" +
                        "}")
        );

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
    void getDepthWithInvalidSymbol(MockServerClient server) throws IOException {
        server.when(request).respond(
                HttpResponse.response()
                .withBody("{\"err-code\":\"invalid-parameter\",\"err-msg\":\"invalid symbol\",\"status\":\"error\",\"ts\":1614173004600}")
        );

        Result<Depth> result = depthService.getDepth(HuobiSymbol.of(Currency.BTC, Currency.BTC));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Result.Status.ERROR, result.getStatus());
        Assertions.assertNotNull(result.getOrigin());
        Assertions.assertTrue(result.getOrigin() instanceof HuobiDepth);
        Assertions.assertNotNull(result.getCode());
        Assertions.assertNotNull(result.getMessage());
    }
}