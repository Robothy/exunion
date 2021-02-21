package com.robothy.exunion.huobi;

import com.robothy.exunion.huobi.util.HuobiSignUtil;
import com.robothy.exunion.rest.spi.Options;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractHuobiAuthorizedExchangeService extends AbstractHuobiExchangeService {

    @Override
    public void init(Options options) {
        super.init(options);
        Objects.requireNonNull(options.getToken().getApiKey(), "The access key is required to visited Huobi authorized service.");
        Objects.requireNonNull(options.getToken().getApiSecret(), "The secret key is required to visited Huobi authorized service.");
    }

    protected String signedUrl(String method, String path, Map<String, Object> params){
        return HuobiSignUtil.sign(method, options.getApiServer(), path, options.getToken().getApiKey(), options.getToken().getApiSecret(), params);
    }
}
