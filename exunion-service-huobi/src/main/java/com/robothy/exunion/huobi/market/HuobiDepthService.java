package com.robothy.exunion.huobi.market;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.huobi.meta.Huobi;
import com.robothy.exunion.huobi.common.HuobiExchangeError;
import com.robothy.exunion.huobi.common.HuobiResponse;
import com.robothy.exunion.rest.AbstractExchangeService;
import com.robothy.exunion.rest.market.DepthService;
import com.robothy.exunion.rest.spi.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.Collectors;

public class HuobiDepthService extends AbstractExchangeService
        implements DepthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuobiDepthService.class);

    @Override
    public void init(Options options) {
        super.init(options);
        if(super.options.getJsonFactory() == null) {
            super.options.setJsonFactory(new JacksonFactory());
        }
    }

    @Override
    public Exchange exchange() {
        return Huobi.SINGLETON;
    }

    @Override
    public Depth getDepth(Symbol symbol) throws ExchangeException, IOException {
        return getDepth(symbol, 20);
    }

    @Override
    public Depth getDepth(Symbol symbol, int depth) throws ExchangeException, IOException {
        if (depth <= 5) depth = 5;
        else if (depth <= 10) depth = 10;
        else depth = 20;

        String url = String.format("%s/market/depth?symbol=%s&type=step1&depth=%d", options.getApiServer(), symbol, depth);
        LOGGER.debug("Request URL: \n" + url);
        HttpResponse result = options.getHttpRequestFactory().buildGetRequest(new GenericUrl(url)).execute();

        HuobiDepth huobiDepth = result.parseAs(HuobiDepth.class);
        if (HuobiResponse.Status.ERROR.equals(huobiDepth.getStatus())) {
            throw new ExchangeException(HuobiExchangeError.of(huobiDepth.getErrCode(), huobiDepth.getErrMsg()));
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Parsed Result: \n" + options.getJsonFactory().toPrettyString(huobiDepth));
        }

        Depth ret = huobiDepth.toDepth();
        ret.setSymbol(symbol);
        ret.setBids(ret.getBids().stream().limit(depth).collect(Collectors.toList()));
        ret.setAsks(ret.getAsks().stream().limit(depth).collect(Collectors.toList()));
        return ret;
    }
}
