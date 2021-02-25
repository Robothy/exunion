package com.robothy.exunion.huobi.market;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.robothy.exunion.core.market.Depth;
import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.huobi.common.HuobiResponse;
import com.robothy.exunion.huobi.meta.Huobi;
import com.robothy.exunion.rest.AbstractExchangeService;
import com.robothy.exunion.rest.Result;
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
        if (super.options.getJsonFactory() == null) {
            super.options.setJsonFactory(new JacksonFactory());
        }
    }

    @Override
    public Exchange exchange() {
        return Huobi.SINGLETON;
    }

    @Override
    public Result<Depth> getDepth(Symbol symbol) throws IOException {
        return getDepth(symbol, 20);
    }

    @Override
    public Result<Depth> getDepth(Symbol symbol, int originalDepth) throws IOException {
        int depth;
        if (originalDepth <= 5) depth = 5;
        else if (originalDepth <= 10) depth = 10;
        else depth = 20;

        String url = String.format("%s/market/depth?symbol=%s&type=step1&depth=%d", options.getApiServer(), symbol, depth);
        LOGGER.debug("Request URL: \n" + url);
        HttpResponse response = options.getHttpRequestFactory().buildGetRequest(new GenericUrl(url)).execute();

        HuobiDepth huobiDepth = response.parseAs(HuobiDepth.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Parsed Result: \n" + options.getJsonFactory().toPrettyString(huobiDepth));
        }

        Result<Depth> result = new Result<>();
        result.setOrigin(huobiDepth);
        if (HuobiResponse.Status.ERROR.equals(huobiDepth.getStatus())) {
            result.setStatus(Result.Status.ERROR);
            result.setCode(huobiDepth.getErrCode());
            result.setMessage(huobiDepth.getErrMsg());
        } else {
            Depth ret = huobiDepth.toDepth();
            ret.setSymbol(symbol);
            ret.setBids(ret.getBids().stream().limit(depth).collect(Collectors.toList()));
            ret.setAsks(ret.getAsks().stream().limit(depth).collect(Collectors.toList()));
            result.setStatus(Result.Status.OK);
            result.set(ret);
        }
        return result;
    }
}
