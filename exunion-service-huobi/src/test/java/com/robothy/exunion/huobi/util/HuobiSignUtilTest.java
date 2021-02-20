package com.robothy.exunion.huobi.util;

import com.google.api.client.http.HttpMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class HuobiSignUtilTest {

    @Test
    void sign() {
        String method = HttpMethods.POST;
        String apiServer = "https://api.huobi.be";
        String path = "/v1/order/orders/place";
        String apiKey = "bewr5drtmh-fakef8dc-fake225d-4fake";
        String apiSecret = "155bfake-8a16fake-efaafake-bfake";

        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");
        String expect = "https://api.huobi.be/v1/order/orders/place?AccessKeyId=bewr5drtmh-fakef8dc-fake225d-4fake&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2021-02-20T03%3A22%3A33&param1=value1&param2=value2&Signature=m3ZFwwsmV3GUtO8LyovIgZ%2BRJMRtrHicd8Oce5WFwwk%3D";

        Assertions.assertEquals(expect, HuobiSignUtil.sign(method, apiServer, path, apiKey, apiSecret, params));
    }
}