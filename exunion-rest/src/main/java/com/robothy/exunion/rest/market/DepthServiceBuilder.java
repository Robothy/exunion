package com.robothy.exunion.rest.market;

import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.rest.AbstractExchangeServiceBuilder;

public class DepthServiceBuilder extends AbstractExchangeServiceBuilder<DepthServiceBuilder> {

    public DepthServiceBuilder exchange(SupportedExchange exchange){
        return super.exchange(exchange, DepthService.class);
    }

    public DepthService build(){
        super.build();                  // from AbstractExchangeServiceBuilder
        super.exchangeService.init();   // from ExchangeService
        return new DepthServiceProxy((DepthService) super.exchangeService);
    }

    public static DepthServiceBuilder create(){
        return new DepthServiceBuilder();
    }
}
