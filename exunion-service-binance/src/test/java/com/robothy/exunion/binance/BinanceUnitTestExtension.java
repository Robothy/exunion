package com.robothy.exunion.binance;

import com.binance.api.client.config.BinanceApiConfig;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class BinanceUnitTestExtension implements BeforeAllCallback, AfterAllCallback {

    private MockedStatic<BinanceApiConfig> mockedConfig;

    @Override
    public void beforeAll(ExtensionContext context) {
        this.mockedConfig = Mockito.mockStatic(BinanceApiConfig.class);
        this.mockedConfig.when(BinanceApiConfig::getApiBaseUrl).thenReturn("http://localhost:8080");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        this.mockedConfig.close();
    }
}
