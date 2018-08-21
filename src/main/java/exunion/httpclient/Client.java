package exunion.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Client 包含一系列 http 客户端的操作，由于使用场景大部分为API接口调用，故返回值均为字符串类型。
 *
 * @author robothy
 */
public class Client {

    private static final Logger LOGGER = LogManager.getLogger(Client.class);

    private static final HttpClientBuilder clientBuilder = HttpClientBuilder.create();

    private static final RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

    private HttpClient client = null;

    /**
     * 构建一个新的 Clinet
     */
    public Client() {
        this(null, 0);
    }

    /**
     * 构建一个带代理服务器的 Client
     *
     * @param proxyHost 代理服务器地址
     * @param port      代理端口
     */
    public Client(String proxyHost, int port) {
        RequestConfig requestConfig = requestConfigBuilder
                .setConnectTimeout(30000)
                .setSocketTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .build();

        if (proxyHost != null) {
            clientBuilder.setProxy(new HttpHost(proxyHost, port));
        }

        this.client = clientBuilder
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * get 向服务器发送一个 get 请求
     *
     * @param uri 超链接
     * @return 服务器返回的结果
     */
    public String get(String uri) {
        return this.get(uri, null);
    }

    /**
     * get 向服务器发送一个带HTTP头信息的 get 请求
     *
     * @param uri    超链接
     * @param header 一个包含头部信息的 Map, 其中 Key 放名称， value放值。
     * @return 服务器返回的结果
     */
    public String get(String uri, Map<String, String> header) {
        HttpRequestBase requestBase = new HttpGet(uri);
        return httpBaseOperate(requestBase, header);
    }

    public String get(String uri, Map<String, String> header, String body) {
        HttpEntity entity = null;
        try {
            entity = new StringEntity(body);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("构建GET方法的字符串实体是出错。", e);
        }
        return this.get(uri, header, entity);
    }

    private String get(String uri, Map<String, String> header, HttpEntity entity) {

        HttpEntityEnclosingRequestBase get = new HttpEntityEnclosingRequestBase() {
            @Override
            public String getMethod() {
                return "GET";
            }
        };

        get.setEntity(entity);
        try {
            get.setURI(new URI(uri));
        } catch (URISyntaxException e) {
            LOGGER.error("URI格式错误。", e);
        }
        return this.httpBaseOperate(get, header);
    }

    /**
     * post 向服务器发送一个 post 请求
     *
     * @param uri 请求资源的超链接
     * @return 服务器返回的文本结果
     */
    public String post(String uri) {
        return this.post(uri, null);
    }

    /**
     * post 向服务器发送一个 post 请求
     *
     * @param uri    请求的资源地址
     * @param header 一个包含头部信息的 Map, 其中 Key 放名称， value放值。
     * @return 服务器返回的文本结果
     */
    public String post(String uri, Map<String, String> header) {
        HttpRequestBase post = new HttpPost(uri);
        return this.httpBaseOperate(post, header);
    }

    /**
     * 向服务器发送一个带参数的post请求
     *
     * @param uri    请求资源的地址
     * @param header 一个包含头部信息的 Map，其中 key 放名称，value放值
     * @param form   待提交的实体
     * @return 服务器返回的文本结果
     */
    public String post(String uri, Map<String, String> header, Map<String, String> form) {
        HttpEntity entity = null;
        if (form != null) {
            List<NameValuePair> parameters = new ArrayList<>();
            for (Entry<String, String> entry : form.entrySet()) {
                NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue());
                parameters.add(nvp);
            }
            try {
                entity = new UrlEncodedFormEntity(parameters, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("构建 POST 请求实体时出现异常。", e);
            }
        }
        return this.post(uri, header, entity);
    }

    /**
     * 向服务器发送一个带字符串实体的POST请求
     * @param uri 资源地址
     * @param header 头部信息
     * @param data 字符串实体
     * @return HTTP服务器返回的结果
     */
    public String post(String uri, Map<String, String> header, String data) {
        HttpEntity entity = null;
        try {
            entity = new StringEntity(data);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("构建POST字符串实体是出错。", data);
        }
        return this.post(uri, header, entity);
    }

    /**
     * 带请求实体的 POST 请求
     * @param uri 资源地址
     * @param header 头部信息
     * @param entity 请求实体
     * @return HTTP服务器返回的结果
     */
    private String post(String uri, Map<String, String> header, HttpEntity entity) {
        HttpPost post = new HttpPost(uri);
        post.setEntity(entity);
        return this.httpBaseOperate(post, header);
    }

    /**
     * HTTP基础操作
     * @param requestBase 基础操作对象
     * @param header 请求头部信息
     * @return HTTP服务器返回的结果
     */
    private String httpBaseOperate(HttpRequestBase requestBase, Map<String, String> header) {
        HttpResponse response = null;
        String responseBody = null;

        URI oldUri = requestBase.getURI();
        URI newUri;
        String ipAddress;
        if ((ipAddress = Hosts.getIpByDomain(oldUri.getHost())) != null) {
            try {
                newUri = new URI(oldUri.toString().replace(oldUri.getHost(), ipAddress));
                requestBase.setURI(newUri);
            } catch (URISyntaxException e) {
                LOGGER.error("uri 语法错误。", e);
            }
        }

        if (null != header) {
            for (Entry<String, String> entry : header.entrySet()) {
                requestBase.addHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            response = this.client.execute(requestBase);
        } catch (ClientProtocolException e) {
            LOGGER.error("向服务发送请求时出现客户端协议异常。", e);
            requestBase.releaseConnection();
        } catch (IOException e) {
            LOGGER.error("向服务器发送请求时出现IO异常。", e);
            requestBase.releaseConnection();
        } catch (Exception e) {
            LOGGER.error("向服务器发送请求时出现异常。", e);
            requestBase.releaseConnection();
        }

        if (null != response) {
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();

            try {
                responseBody = EntityUtils.toString(entity);
                if (statusCode < 200 || statusCode > 300) {
                    LOGGER.warn("服务器返回HTTP状态码：{}，信息：\n{}", statusCode, responseBody);
                    responseBody = null;
                }
            } catch (ParseException e) {
                LOGGER.error("将服务器返回的实体转化为字符串时出现解析异常。", e);
            } catch (IOException e) {
                LOGGER.error("将服务器返回的实体转化为字符串时出现IO异常。", e);
            }
        }
        requestBase.releaseConnection();
        return responseBody;
    }

}
