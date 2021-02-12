package com.robothy.exunion.rest;

import com.robothy.exunion.core.auth.Token;
import com.robothy.exunion.core.exception.InvalidOperationException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.rest.spot.SpotTradingService;
import com.robothy.exunion.rest.util.TradingServiceProvider;

import java.util.Map;

/**
 * @param <T> the builder that extents <code>AbstractExchangeServiceBuilder</code>.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractExchangeServiceBuilder<T> {

    protected ExchangeService exchangeService;

    protected AbstractExchangeService abstractExchangeService;

    protected T exchange(SupportedExchange exchange, Class<? extends ExchangeService> clazz){
        this.exchangeService = TradingServiceProvider.newInstance(exchange, clazz);
        if(this.exchangeService instanceof AbstractExchangeService){
            this.abstractExchangeService = (AbstractExchangeService) TradingServiceProvider.newInstance(exchange, SpotTradingService.class);
        }
        return (T)this;
    }

    public T apiKey(String apiKey) {
        checkExchange();
        checkAbstractExchangeService("API Key");
        if (this.abstractExchangeService.getToken() == null) {
            this.abstractExchangeService.setToken(new Token());
        }
        this.abstractExchangeService.getToken().setApiKey(apiKey);
        return (T)this;
    }

    public T apiSecret(String apiSecret) {
        checkExchange();
        checkAbstractExchangeService("API Secret");
        if (this.abstractExchangeService.getToken() == null) {
            this.abstractExchangeService.setToken(new Token());
        }
        this.abstractExchangeService.getToken().setApiSecret(apiSecret);
        return (T)this;
    }

    public T token(Token token){
        checkExchange();
        checkAbstractExchangeService("Token");
        this.abstractExchangeService.setToken(token);
        return (T) this;
    }

    public T extraProperties(Map<String, Object> extraProperties) {
        checkExchange();
        checkAbstractExchangeService("Extra Properties");
        this.abstractExchangeService.setExtraProperties(extraProperties);
        return (T)this;
    }

    private void checkAbstractExchangeService(String property) {
        if (this.abstractExchangeService == null) {
            throw new InvalidOperationException("The " + this.exchangeService.getClass() + " doesn't implemented the AbstractTradingService, " +
                    "so you are not required and cannot set the " + property + " to build this trading service.");
        }
    }

    protected void checkExchange() {
        if (this.abstractExchangeService == null) {
            throw new InvalidOperationException("Please call exchange() as the first step to build trading service instance");
        }
    }

}
