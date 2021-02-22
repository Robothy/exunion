package com.robothy.exunion.huobi;

import com.google.api.client.http.HttpMethods;
import com.robothy.exunion.huobi.util.HuobiSignUtil;
import com.robothy.exunion.rest.spi.Options;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractHuobiAuthorizedExchangeService extends AbstractHuobiExchangeService {

    @Override
    public void init(Options options) {
        super.init(options);
        Objects.requireNonNull(options.getToken().getApiKey(), "The access key is required to visited Huobi authorized service.");
        Objects.requireNonNull(options.getToken().getApiSecret(), "The secret key is required to visited Huobi authorized service.");
    }

    /**
     * POST data with signature to huobi API server.
     * @param path the API entrypoint without url parameters.
     * @param params the URL parameters to sign and build the full request URL.
     * @param body the request body.
     * @param responseType the response class.
     * @param <T> the response data type.
     * @return the response body.
     * @throws IOException when network error occurs.
     */
    protected <T> T postWithSign(String path, Map<String, Object> params, Object body, Class<T> responseType) throws IOException {
        String url = signedUrl(HttpMethods.POST, path, params);
        return super.post(url, body, responseType);
    }

    /**
     * GET data with signature.
     * @param path the API entrypoint with url parameter.
     * @param params the URL parameters to sign and build the request URL.
     * @param responseType the response class.
     * @param <T> the response type.
     * @return the response.
     * @throws IOException when network error occurs.
     */
    protected <T> T getWithSign(String path, Map<String, Object> params, Class<T> responseType) throws IOException {
        String url = signedUrl(HttpMethods.GET, path, params);
        return super.get(url, responseType);
    }

    protected <T> T postWithSign(String path, Object body, Class<T> responseType) throws IOException {
        return this.postWithSign(path, null, body, responseType);
    }

    protected <T> T getWithSign(String path, Class<T> responseType) throws IOException {
        return this.getWithSign(path, null, responseType);
    }

    protected String signedUrl(String method, String path, Map<String, Object> params){
        return HuobiSignUtil.sign(method, options.getApiServer(), path, options.getToken().getApiKey(), options.getToken().getApiSecret(), params);
    }
}
