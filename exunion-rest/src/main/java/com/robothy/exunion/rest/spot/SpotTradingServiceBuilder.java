package com.robothy.exunion.rest.spot;

import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.exception.InvalidOperationException;
import com.robothy.exunion.rest.AbstractExchangeServiceBuilder;

import java.math.BigDecimal;

/**
 * Spot trading service builder. The <code>exchange()</code> method should be invoked in the first to
 * set the exchange, then set the other properties. Finally, call <code>build()</code> to get
 * the instance.
 */
public class SpotTradingServiceBuilder extends AbstractExchangeServiceBuilder<SpotTradingServiceBuilder> {

    private AbstractSpotTradingService abstractSpotTradingService;

    public SpotTradingServiceBuilder exchange(SupportedExchange exchange) {
        super.exchange(exchange, SpotTradingService.class);
        if (super.exchangeService instanceof AbstractSpotTradingService) {
            this.abstractSpotTradingService = (AbstractSpotTradingService) super.abstractExchangeService;
        }
        return this;
    }

    public SpotTradingServiceBuilder defaultFeeRate(BigDecimal feeRate) {
        super.checkExchange();
        this.checkAbstractSpotTradingService("Default Fee Rate");
        this.abstractSpotTradingService.setDefaultFeeRate(feeRate);
        return this;
    }

    public SpotTradingServiceBuilder defaultBuyInFeeRate(BigDecimal defaultBuyInFeeRate) {
        super.checkExchange();
        this.checkAbstractSpotTradingService("Default Buy-In Fee Rate");
        this.abstractSpotTradingService.setDefaultBuyInFeeRate(defaultBuyInFeeRate);
        return this;
    }

    public SpotTradingServiceBuilder defaultSellOutFeeRate(BigDecimal defaultSellOutFeeRate) {
        super.checkExchange();
        this.checkAbstractSpotTradingService("Default Sell-Out Fee Rate");
        this.abstractSpotTradingService.setDefaultSellOutFeeRate(defaultSellOutFeeRate);
        return this;
    }

    public SpotTradingService build() {
        super.build();                  // from AbstractExchangeServiceBuilder
        super.exchangeService.init();   // from ExchangeService
        return new SpotTradingServiceProxy(this.abstractSpotTradingService);
    }

    private void checkAbstractSpotTradingService(String property) {
        if (this.abstractSpotTradingService == null) {
            throw new InvalidOperationException("The " + this.abstractExchangeService.getClass() + " doesn't extend the AbstractSpotTradingService, " +
                    "so you are not required and cannot set the " + property + " to build this trading service.");
        }
    }
}
