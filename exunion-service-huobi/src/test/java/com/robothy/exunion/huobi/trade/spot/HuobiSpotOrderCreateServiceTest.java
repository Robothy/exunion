package com.robothy.exunion.huobi.trade.spot;

import com.google.api.client.http.HttpMethods;
import com.robothy.exunion.core.meta.Currency;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import com.robothy.exunion.core.trade.spot.SpotOrderDetails;
import com.robothy.exunion.huobi.HuobiToken;
import com.robothy.exunion.huobi.meta.Huobi;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.spi.ExchangeServiceProvider;
import com.robothy.exunion.rest.spi.Options;
import com.robothy.exunion.rest.spi.OptionsBuilder;
import com.robothy.exunion.rest.spot.SpotOrderCreateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * set the preTestSuit = ture to make all of the tests shard the same MockServerClient instance,
 * which improves the test performance.
 */
@MockServerSettings(ports = 8080, perTestSuite = true)
@ExtendWith(MockServerExtension.class)
class HuobiSpotOrderCreateServiceTest {

    Options options = OptionsBuilder.create()
            .apiServer("http://localhost:8080")
            .token(HuobiToken.INSTANCE)
            .build();

    private final HttpRequest request = HttpRequest.request().withMethod(HttpMethods.POST);

    @Test
    void serviceExists(){
        assertNotNull(ExchangeServiceProvider.newInstance(Huobi.SINGLETON, SpotOrderCreateService.class, options));
    }

    @Test
    void create(MockServerClient mockServerClient) throws IOException {
        assertNotNull(mockServerClient);
        mockServerClient.when(request.withPath(HuobiSpotOrderCreateService.ORDER_PATH))
                .respond(HttpResponse.response().withBody("{\"data\":\"220086189003998\",\"status\":\"ok\"}"));

        SpotOrderCreateService createService = ExchangeServiceProvider.newInstance(Huobi.SINGLETON, SpotOrderCreateService.class, options);
        SpotOrder spotOrder = SpotOrder.Builder.create()
                .side(SpotOrder.Side.SELL)
                .type(SpotOrder.Type.LIMIT)
                .quantity(new BigDecimal(10000))
                .price(new BigDecimal("0.0001"))
                .symbol(Symbol.of(Currency.PNT, Currency.BTC))
                .extra("account-id", "3009896")
                .build();
        Result<SpotOrderDetails> result = createService.create(spotOrder);
        assertNotNull(result);
        assertTrue(result.ok());
        assertEquals(Result.Status.OK, result.getStatus());
    }

    @Test
    void batchCreate(MockServerClient mockServerClient) throws IOException {
        assertNotNull(mockServerClient);
        mockServerClient.when(
                request.withPath(HuobiSpotOrderCreateService.BATCH_ORDER_PATH)
        ).respond(
                HttpResponse.response()
                .withBody("{\"data\":[{\"order-id\":219645426002633,\"client-order-id\":\"\"},{\"client-order-id\":\"\",\"err-code\":\"validation-constraints-error\",\"err-msg\":\"Please enter a valid accountId.\"}],\"status\":\"ok\"}")
        );


        SpotOrderCreateService createService = ExchangeServiceProvider.newInstance(Huobi.SINGLETON, SpotOrderCreateService.class, options);
        SpotOrder sell = SpotOrder.Builder.create()
                .side(SpotOrder.Side.SELL)
                .type(SpotOrder.Type.LIMIT)
                .quantity(new BigDecimal(10000))
                .price(new BigDecimal("0.0001"))
                .extra("account-id", "3009896")
                .symbol(Symbol.of(Currency.PNT, Currency.BTC))
                .build();

        // no account id
        SpotOrder buy = SpotOrder.Builder.create()
                .side(SpotOrder.Side.BUY)
                .type(SpotOrder.Type.LIMIT)
                .quantity(new BigDecimal("1000"))
                .price(new BigDecimal("1"))
                .symbol(Symbol.of(Currency.BTC, Currency.USDT))
                .build();
        Result<List<Result<SpotOrderDetails>>> result = createService.create(Arrays.asList(sell, buy));
        assertNotNull(result);
        assertTrue(result.ok());

        List<Result<SpotOrderDetails>> resultList = result.get();
        assertNotNull(resultList);
        assertEquals(2, resultList.size());

        Result<SpotOrderDetails> first = resultList.get(0);
        assertNotNull(first);
        assertTrue(first.ok());
        assertNotNull(first.getData().getOrderId());

        Result<SpotOrderDetails> second = resultList.get(1);
        assertNotNull(second);
        assertFalse(second.ok());
        assertNotNull(second.getCode());
        assertNotNull(second.getMessage());
    }
}