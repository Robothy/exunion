package com.robothy.exunion.huobi.account;

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

import java.io.IOException;

class HuobiSpotAccountServiceAdapterTest {

    @Test
    void query() throws IOException {
        Options options = OptionsBuilder.create()
                .token(new Token("fake", "fake"))
                .apiServer("https://api.huobi.be")
                .build();

        SpotAccountService spotAccountService = ExchangeServiceProvider.newInstance(Huobi.SINGLETON, SpotAccountService.class, options);
        Result<SpotAccount> result = spotAccountService.query();
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.ok());
    }
}