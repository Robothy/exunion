package com.robothy.exunion.huobi.util;

import com.robothy.exunion.core.util.SignAlgorithm;
import com.robothy.exunion.core.util.SignUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class HuobiSignUtil {

    private static final ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(() -> {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm'%3A'ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    });

    /**
     * @param method    the http method
     * @param apiServer the API server with prefix "https". For example: https://api.huobe.be
     * @param params    the URL parameters
     * @return the final request URL with signatures.
     */
    public static String sign(String method, String apiServer, String path, String accessKey, String secretKey, Map<String, Object> params) {
        Objects.requireNonNull(method, "The HTTP method must be specified.");
        Objects.requireNonNull(apiServer, "The API server must be specified.");
        Objects.requireNonNull(accessKey, "The access key cannot be null.");

        String host = apiServer;
        if (host.startsWith("https://")) {
            host = host.substring("https://".length());
        } else if (host.startsWith("http://")) {
            host = host.substring("http://".length());
        }

        TreeMap<String, Object> sortedParams = params == null ? new TreeMap<>() : new TreeMap<>(params);
        sortedParams.put("AccessKeyId", accessKey);
        sortedParams.put("SignatureMethod", SignAlgorithm.HmacSHA256);
        sortedParams.put("SignatureVersion", 2);
        sortedParams.put("Timestamp", dateFormat.get().format(new Date()));

        StringBuilder text = new StringBuilder()
                .append(method).append("\n")
                .append(host).append("\n")
                .append(path).append("\n");

        StringBuilder paramStr = mapToString(sortedParams);
        text.append(paramStr);

        byte[] signedBytes = SignUtil.sign(SignAlgorithm.HmacSHA256, secretKey, text.toString());
        String sign = Base64.getEncoder().encodeToString(signedBytes);

        StringBuilder url = new StringBuilder();
        url.append(apiServer).append(path).append("?").append(paramStr).append("&Signature=");
        try {
            return url.append(URLEncoder.encode(sign, StandardCharsets.UTF_8.name())).toString();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Invalid arguments passed into sign method.", e);
        }
    }

    private static StringBuilder mapToString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();

        // The argument map cannot be empty, so it's safe to call next() without check by hasNext().
        Map.Entry<String, Object> firstParam = it.next();
        sb.append(firstParam.getKey()).append("=").append(firstParam.getValue());
        while (it.hasNext()) {
            Map.Entry<String, Object> param = it.next();
            sb.append("&").append(param.getKey()).append("=").append(param.getValue());
        }
        return sb;
    }

}
