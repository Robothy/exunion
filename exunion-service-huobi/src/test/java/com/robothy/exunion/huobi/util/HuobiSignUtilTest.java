package com.robothy.exunion.huobi.util;

import com.google.api.client.http.HttpMethods;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        String signedUrl = HuobiSignUtil.sign(method, apiServer, path, apiKey, apiSecret, params);
        assertNotNull(signedUrl);
        assertTrue(signedUrl.startsWith(apiServer + path));
    }
}