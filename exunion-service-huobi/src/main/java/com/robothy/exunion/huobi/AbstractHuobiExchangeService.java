package com.robothy.exunion.huobi;

import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.huobi.meta.Huobi;
import com.robothy.exunion.rest.AbstractExchangeService;

public abstract class AbstractHuobiExchangeService extends AbstractExchangeService {

    @Override
    public Exchange exchange() {
        return Huobi.SINGLETON;
    }

}