package com.robothy.exunion.core.trade.spot;

import com.robothy.exunion.api.auth.Token;
import com.robothy.exunion.api.meta.SupportedExchange;
import com.robothy.exunion.api.trade.AbstractTradingService;
import com.robothy.exunion.api.trade.spot.AbstractSpotTradingService;
import com.robothy.exunion.api.trade.spot.SpotTradingService;
import com.robothy.exunion.api.trade.TradingService;
import com.robothy.exunion.core.util.BaseExchangeServiceBuilder;
import com.robothy.exunion.core.exception.InvalidOperationException;
import com.robothy.exunion.core.util.TradingServiceProvider;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Spot trading service builder. The <code>exchange()</code> should be invoked in the first to
 * set the exchange, then set the other properties. Finally, call <code>build()</code> to get
 * the instance.
 */
public class SpotTradingServiceBuilder extends BaseExchangeServiceBuilder {

    private TradingService tradingService;

    private AbstractTradingService abstractTradingService;

    private AbstractSpotTradingService abstractSpotTradingService;

    public SpotTradingServiceBuilder exchange(SupportedExchange exchange) {
        this.tradingService = TradingServiceProvider.newInstance(exchange, SpotTradingService.class);

        if (this.tradingService instanceof AbstractTradingService) {
            this.abstractTradingService = (AbstractTradingService) this.tradingService;
        }

        if (this.tradingService instanceof AbstractSpotTradingService) {
            this.abstractSpotTradingService = (AbstractSpotTradingService) this.tradingService;
        }

        return this;
    }

    /**
     * Set API token, equals <code>apiKey(), apiSecret()</code>
     */
    public SpotTradingServiceBuilder token(Token token){
        checkExchange();
        checkAbstractTradingService("Token");
        this.abstractTradingService.setToken(token);
        return this;
    }

    public SpotTradingServiceBuilder apiKey(String apiKey) {
        checkExchange();
        checkAbstractTradingService("API Key");
        if (this.abstractTradingService.getToken() == null) {
            this.abstractTradingService.setToken(new Token());
        }
        this.abstractTradingService.getToken().setApiKey(apiKey);
        return this;
    }

    public SpotTradingServiceBuilder apiSecret(String apiSecret) {
        checkExchange();
        checkAbstractTradingService("API Secret");
        if (this.abstractTradingService.getToken() == null) {
            this.abstractTradingService.setToken(new Token());
        }
        this.abstractTradingService.getToken().setApiSecret(apiSecret);
        return this;
    }

    public SpotTradingServiceBuilder extraProperties(Map<String, Object> extraProperties) {
        checkExchange();
        checkAbstractTradingService("Extra Properties");
        this.abstractTradingService.setExtraProperties(extraProperties);
        return this;
    }

    public SpotTradingServiceBuilder defaultFeeRate(BigDecimal feeRate) {
        checkExchange();
        checkAbstractSpotTradingService("Default Fee Rate");
        this.abstractSpotTradingService.setDefaultFeeRate(feeRate);
        return this;
    }

    public SpotTradingServiceBuilder defaultBuyInFeeRate(BigDecimal defaultBuyInFeeRate) {
        checkExchange();
        checkAbstractSpotTradingService("Default Buy-In Fee Rate");
        this.abstractSpotTradingService.setDefaultBuyInFeeRate(defaultBuyInFeeRate);
        return this;
    }

    public SpotTradingServiceBuilder defaultSellOutFeeRate(BigDecimal defaultSellOutFeeRate) {
        checkExchange();
        checkAbstractTradingService("Default Sell-Out Fee Rate");
        this.abstractSpotTradingService.setDefaultSellOutFeeRate(defaultSellOutFeeRate);
        return this;
    }

    public SpotTradingService build() {
        return (SpotTradingService) this.tradingService;
    }

    private void checkAbstractSpotTradingService(String property) {
        if (this.abstractSpotTradingService == null) {
            throw new InvalidOperationException("The " + this.tradingService.getClass() + " doesn't implemented the AbstractSpotTradingService, " +
                    "so you are not required and cannot set the " + property + " to build this trading service.");
        }
    }

    private void checkAbstractTradingService(String property) {
        if (this.abstractTradingService == null) {
            throw new InvalidOperationException("The " + this.tradingService.getClass() + " doesn't implemented the AbstractTradingService, " +
                    "so you are not required and cannot set the " + property + " to build this trading service.");
        }
    }

    private void checkExchange() {
        if (this.tradingService == null) {
            throw new InvalidOperationException("Please call SpotTradingServiceBuilder.exchange() as the first step of build trading service instance");
        }
    }
}
