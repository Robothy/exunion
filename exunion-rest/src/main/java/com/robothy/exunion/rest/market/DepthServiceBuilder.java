package com.robothy.exunion.rest.market;

import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.rest.AbstractExchangeServiceBuilder;

public class DepthServiceBuilder extends AbstractExchangeServiceBuilder<DepthServiceBuilder> {

    public DepthServiceBuilder exchange(SupportedExchange exchange){
        return super.exchange(exchange, DepthService.class);
    }

    public DepthService build(){
        super.exchangeService.init();
        DepthService depthService = (DepthService) super.exchangeService;
        return new DepthServiceProxy(depthService);
    }

    public static DepthServiceBuilder builder(){
        return new DepthServiceBuilder();
    }

}
