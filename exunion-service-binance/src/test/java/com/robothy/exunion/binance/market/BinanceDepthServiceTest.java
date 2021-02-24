package com.robothy.exunion.binance.market;

import com.binance.api.client.config.BinanceApiConfig;
import com.robothy.exunion.binance.Binance;
import com.robothy.exunion.binance.BinanceUnitTestExtension;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.market.DepthService;
import com.robothy.exunion.rest.spi.ExchangeServiceProvider;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.Format;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MockServerSettings(ports = 18080)
@ExtendWith(MockServerExtension.class)
class BinanceDepthServiceTest {

    @Test
    void getDepth(MockServerClient server) throws IOException, InterruptedException {

        HttpRequest httpRequest = HttpRequest.request().withMethod("GET").withPath("/api/v3/depth");
        HttpResponse httpResponse = HttpResponse.response()
                .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                .withBody("{\"lastUpdateId\":2569961549,\"bids\":[[\"0.03351900\",\"4.61900000\"],[\"0.03351800\",\"0.07200000\"],[\"0.03351600\",\"0.00800000\"],[\"0.03351500\",\"0.89500000\"],[\"0.03351300\",\"0.00900000\"]],\"asks\":[[\"0.03352000\",\"1.90000000\"],[\"0.03352100\",\"0.17700000\"],[\"0.03352500\",\"0.00300000\"],[\"0.03352600\",\"0.50000000\"],[\"0.03352700\",\"0.21800000\"]]}");

        server.when(httpRequest).respond(httpResponse);

        while (!server.hasStarted()) Thread.sleep(100);

        System.out.println("Server started.");

        Mockito.mockStatic(BinanceApiConfig.class).when(BinanceApiConfig::getApiBaseUrl).thenReturn("http://localhost:18080");


        try {
            DepthService depthService = ExchangeServiceProvider.newInstance(Binance.BINANCE, DepthService.class);
            assertNotNull(depthService);
            Result<Depth> result = depthService.getDepth(Symbol.of("BTC", "USDT"));
            assertTrue(result.ok());
            assertNotNull(result.get());
            assertNotNull(result.getOrigin());

        }catch (Exception e){

        }

        Thread.sleep(30000);
        System.out.println(server.retrieveRecordedRequests(httpRequest, Format.JSON));
    }
}