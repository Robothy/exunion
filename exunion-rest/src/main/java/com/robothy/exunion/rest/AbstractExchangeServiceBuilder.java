package com.robothy.exunion.rest;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.robothy.exunion.core.auth.Token;
import com.robothy.exunion.core.exception.InvalidOperationException;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.rest.util.ExchangeServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * @param <T> the builder that extents <code>AbstractExchangeServiceBuilder</code>.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractExchangeServiceBuilder<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExchangeServiceBuilder.class);

    protected ExchangeService exchangeService;

    protected AbstractExchangeService abstractExchangeService;

    protected T exchange(SupportedExchange exchange, Class<? extends ExchangeService> clazz) {
        this.exchangeService = ExchangeServiceProvider.newInstance(exchange, clazz);
        if (this.exchangeService instanceof AbstractExchangeService) {
            this.abstractExchangeService = (AbstractExchangeService) exchangeService;
        }
        return (T) this;
    }

    public T apiKey(String apiKey) {
        checkExchange();
        checkAbstractExchangeService("API Key");
        if (this.abstractExchangeService.getToken() == null) {
            this.abstractExchangeService.setToken(new Token());
        }
        this.abstractExchangeService.getToken().setApiKey(apiKey);
        return (T) this;
    }

    public T apiSecret(String apiSecret) {
        checkExchange();
        checkAbstractExchangeService("API Secret");
        if (this.abstractExchangeService.getToken() == null) {
            this.abstractExchangeService.setToken(new Token());
        }
        this.abstractExchangeService.getToken().setApiSecret(apiSecret);
        return (T) this;
    }

    public T token(Token token) {
        checkExchange();
        checkAbstractExchangeService("Token");
        this.abstractExchangeService.setToken(token);
        return (T) this;
    }

    public T httpTransport(HttpTransport httpTransport) {
        checkExchange();
        checkAbstractExchangeService("com.google.api.client.http.HttpTransport");
        this.abstractExchangeService.setHttpTransport(httpTransport);
        return (T) this;
    }

    public T jsonFactory(JsonFactory jsonFactory) {
        checkExchange();
        checkAbstractExchangeService("com.google.api.client.json.JsonFactory");
        this.abstractExchangeService.setJsonFactory(jsonFactory);
        return (T) this;
    }

    public T extraProperties(Map<String, Object> extraProperties) {
        checkExchange();
        checkAbstractExchangeService("Extra Properties");
        this.abstractExchangeService.setExtraProperties(extraProperties);
        return (T) this;
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

    /**
     * Set the API server. If this property doesn't set, the {@link SupportedExchange#getDefaultApiServer()} will be used.
     *
     * @param apiServer the API server url. For example: https://api.sample.com
     *
     */
    public T apiServer(String apiServer){
        checkExchange();
        checkAbstractExchangeService("API Server");
        this.abstractExchangeService.setApiServer(apiServer);
        return (T) this;
    }

    protected Object build() {
        checkExchange();
        if (this.abstractExchangeService != null) {
            Objects.requireNonNull(this.abstractExchangeService.getJsonFactory(), "The JsonFactory is required to build an exchange service instance.");

            if (this.abstractExchangeService.getHttpTransport() == null) {
                LOGGER.info("HttpTransport doesn't set, Use " + NetHttpTransport.class.getName() + " as default HttpTransport" );
                this.abstractExchangeService.setHttpTransport(new NetHttpTransport());
            }

            HttpTransport httpTransport = this.abstractExchangeService.getHttpTransport();
            JsonFactory jsonFactory = this.abstractExchangeService.getJsonFactory();
            this.abstractExchangeService.requestFactory = httpTransport.createRequestFactory(request -> request.setParser(new JsonObjectParser(jsonFactory)));

            if(this.abstractExchangeService.getApiServer()==null){
                this.abstractExchangeService.setApiServer(this.abstractExchangeService.exchange().getDefaultApiServer());
            }
        }
        return this;
    }

}
