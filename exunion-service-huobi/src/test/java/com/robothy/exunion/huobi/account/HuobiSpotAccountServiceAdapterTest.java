package com.robothy.exunion.huobi.account;

import com.google.api.client.http.HttpMethods;
import com.huobi.service.huobi.HuobiAccountService;
import com.robothy.exunion.core.account.SpotAccount;
import com.robothy.exunion.core.auth.Token;
import com.robothy.exunion.huobi.meta.Huobi;
import com.robothy.exunion.rest.Result;
import com.robothy.exunion.rest.account.SpotAccountService;
import com.robothy.exunion.rest.spi.ExchangeServiceProvider;
import com.robothy.exunion.rest.spi.Options;
import com.robothy.exunion.rest.spi.OptionsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;

import java.io.IOException;

@MockServerSettings(ports = 9090)
@ExtendWith(MockServerExtension.class)
class HuobiSpotAccountServiceAdapterTest {

    @Test
    void query(ClientAndServer server) throws IOException, InterruptedException {

        server.when(
                HttpRequest.request()
                .withMethod(HttpMethods.GET)
                .withPath(HuobiAccountService.GET_ACCOUNTS_PATH)
        ).respond(
                HttpResponse.response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody("{status:\"ok\",data:[{id:4213123,type:\"spot\",subtype:\"\",state:\"working\"},{id:123233,type:\"margin\",subtype:\"btcusdt\",state:\"working\"},{id:2927146,type:\"point\",subtype:\"\",state:\"working\"}]}")
        );

        server.when(
                HttpRequest.request()
                .withMethod(HttpMethods.GET)
                .withPath("/v1/account/accounts/.{1,}/balance")
        ).respond(
                HttpResponse.response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody("{\"status\":\"ok\",\"data\":{\"id\":3009896,\"type\":\"spot\",\"state\":\"working\",\"list\":[{\"currency\":\"lun\",\"type\":\"trade\",\"balance\":\"0\"},{\"currency\":\"lun\",\"type\":\"frozen\",\"balance\":\"0\"},{\"currency\":\"ksm\",\"type\":\"trade\",\"balance\":\"0\"}]}}")
        );


        Options options = OptionsBuilder.create()
                .token(new Token("bewr5drtmh-1384f8dc-0ec5225d-4a44b", "155bc62c-8a165d1d-efaa8ad7-b9fda"))
                .apiServer("http://localhost:9090")
                .build();

        SpotAccountService spotAccountService = ExchangeServiceProvider.newInstance(Huobi.SINGLETON, SpotAccountService.class, options);
        Result<SpotAccount> result = spotAccountService.query();
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.ok());
    }
}