package com.robothy.exunion.huobi;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.json.JsonHttpContent;
import com.robothy.exunion.core.meta.Exchange;
import com.robothy.exunion.huobi.meta.Huobi;
import com.robothy.exunion.rest.AbstractExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class AbstractHuobiExchangeService extends AbstractExchangeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHuobiExchangeService.class);

    @Override
    public Exchange exchange() {
        return Huobi.SINGLETON;
    }

    /**
     * Huobi RestAPI POST request without query parameters.
     * @param path the API entrypoint or full URL (starts with 'http').
     * @param body the request body
     * @param responseType the target response type that the response body will be parsed to.
     * @param <T> the response type.
     * @return the response body.
     * @throws IOException when network error occurs.
     */
    protected <T> T post(String path, Object body, Class<T> responseType) throws IOException {
        String url = path.startsWith("http") ? path : options.getApiServer() + path;
        logRequest(HttpMethods.POST, url, body);
        T response = options.getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), new JsonHttpContent(options.getJsonFactory(), body))
                .execute()
                .parseAs(responseType);
        logResponse(response);
        return response;
    }

    /**
     * Huobi API GET operation.
     * @param path the API entrypoint or full URL (starts with 'http').
     * @param responseType the target java type of the response.
     * @param <T> the response type.
     * @return parsed response.
     * @throws IOException network error occurs.
     */
    protected <T> T get(String path, Class<T> responseType) throws IOException {
        String url = path.startsWith("http") ? path : options.getApiServer() + path;
        logRequest(HttpMethods.GET, url);
        T response = options.getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(url))
                .execute()
                .parseAs(responseType);
        logResponse(response);
        return response;
    }

    private void logRequest(String method, String url) throws IOException {
        logRequest(method, url, null);
    }

    private void logRequest(String method, String url, Object body) throws IOException {
        if(LOGGER.isDebugEnabled()){
            StringBuilder text = new StringBuilder().append("\n")
                    .append(exchange()).append("\n")
                    .append(method).append("\n");
            if(null != body) text.append(options.getJsonFactory().toPrettyString(body));
            LOGGER.debug(text.toString());
        }
    }

    private void logResponse(Object response) throws IOException {
        if(LOGGER.isDebugEnabled()){
            StringBuilder text = new StringBuilder().append("\n")
                    .append(exchange()).append("\n")
                    .append(options.getJsonFactory().toPrettyString(response));
            LOGGER.debug(text.toString());
        }
    }

}