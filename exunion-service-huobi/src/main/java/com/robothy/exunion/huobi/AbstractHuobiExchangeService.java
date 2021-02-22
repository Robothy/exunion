package com.robothy.exunion.huobi;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpResponse;
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
        HttpResponse httpResponse = options.getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), new JsonHttpContent(options.getJsonFactory(), body))
                .execute();
        return parseResult(httpResponse, responseType);
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
        HttpResponse httpResponse = options.getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(url))
                .execute();
        return parseResult(httpResponse, responseType);
    }

    private <T> T parseResult(HttpResponse httpResponse, Class<T> responseType) throws IOException {
        T result;
        if(responseType == String.class){
            result = responseType.cast(httpResponse.parseAsString());
        }else {
            result = httpResponse.parseAs(responseType);
        }
        logResponse(result);
        return result;
    }

    private void logRequest(String method, String url) throws IOException {
        logRequest(method, url, null);
    }

    private void logRequest(String method, String url, Object body) throws IOException {
        if(LOGGER.isDebugEnabled()){
            StringBuilder text = new StringBuilder()
                    .append("Request -> ").append("\n")
                    .append(exchange().id()).append("\n")
                    .append(method).append("\n")
                    .append(url).append("\r");
            if(null != body) text.append(options.getJsonFactory().toPrettyString(body));
            LOGGER.debug(text.toString());
        }
    }

    private void logResponse(Object response) throws IOException {
        if(LOGGER.isDebugEnabled()){
            StringBuilder text = new StringBuilder()
                    .append("Response -> ").append("\n")
                    .append(exchange().id()).append("\n");
            if(response instanceof String){
                text.append(response);
            }else{
                text.append(options.getJsonFactory().toPrettyString(response));
            }
            LOGGER.debug(text.toString());
        }
    }

}