package com.robothy.exunion.huobi.market;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.robothy.exunion.core.exception.ExchangeException;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.SupportedExchange;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.rest.AbstractExchangeService;
import com.robothy.exunion.rest.market.DepthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.Collectors;

import static com.robothy.exunion.core.meta.SupportedExchange.HUOBI;

public class HuobiDepthService extends AbstractExchangeService
        implements DepthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HuobiDepthService.class);

    @Override
    public void init() {

    }

    @Override
    public SupportedExchange exchange() {
        return HUOBI;
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

        String url = String.format("%s/market/depth?symbol=%s&type=step5&depth=%s", HUOBI.getDefaultApiServer(), symbol, depth);
        HttpResponse result = requestFactory.buildGetRequest(new GenericUrl(url)).execute();
        LOGGER.debug("Request URL: " + url);
        if (result.getStatusCode() != HttpStatusCodes.STATUS_CODE_OK) {
            throw new ExchangeException(result.parseAsString());
        }
        Depth ret = result.parseAs(HuobiDepth.class).toDepth();
        ret.setSymbol(symbol);

        ret.setBids(ret.getBids().stream().limit(depth).collect(Collectors.toList()));
        ret.setAsks(ret.getAsks().stream().limit(depth).collect(Collectors.toList()));
        return ret;
    }
}
