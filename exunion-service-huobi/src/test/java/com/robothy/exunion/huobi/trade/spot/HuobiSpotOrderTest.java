package com.robothy.exunion.huobi.trade.spot;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.robothy.exunion.core.meta.Currency;
import com.robothy.exunion.core.meta.Symbol;
import com.robothy.exunion.core.trade.spot.SpotOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

class HuobiSpotOrderTest {

    @Test
    void testJsonDeserialize() throws IOException {
        String json = "{\n" +
                "  \"account-id\": \"100009\",\n" +
                "  \"amount\": \"10.1\",\n" +
                "  \"price\": \"100.1\",\n" +
                "  \"source\": \"api\",\n" +
                "  \"symbol\": \"ethusdt\",\n" +
                "  \"type\": \"buy-limit\",\n" +
                "  \"client-order-id\": \"a0001\"\n" +
                "}";

        JsonFactory jsonFactory = new JacksonFactory();
        HuobiSpotOrder huobiSpotOrder = jsonFactory.fromString(json, HuobiSpotOrder.class);
        Assertions.assertEquals(huobiSpotOrder.getAccountId(), "100009");
        Assertions.assertEquals(huobiSpotOrder.getAmount(), new BigDecimal("10.1"));
        Assertions.assertEquals(huobiSpotOrder.getPrice(), new BigDecimal("100.1"));
        Assertions.assertEquals(huobiSpotOrder.getSource(), "api");
        Assertions.assertEquals(huobiSpotOrder.getSymbol(), "ethusdt");
        Assertions.assertEquals(huobiSpotOrder.getType(), "buy-limit");
        Assertions.assertEquals(huobiSpotOrder.getClientOrderId(), "a0001");
    }

    @Test
    void testJsonSerialize() throws IOException {
        HuobiSpotOrder huobiSpotOrder = new HuobiSpotOrder();
        huobiSpotOrder.setAccountId("10009");
        huobiSpotOrder.setAmount(new BigDecimal("10.12"));
        huobiSpotOrder.setPrice(new BigDecimal("100.1"));
        huobiSpotOrder.setSource("api");
        huobiSpotOrder.setType("sell-limit");
        huobiSpotOrder.setClientOrderId("a0001");
        huobiSpotOrder.setSymbol("btcusdt");

        JsonFactory jsonFactory = new JacksonFactory();
        String json = jsonFactory.toPrettyString(huobiSpotOrder);

        Assertions.assertNotNull(json);
        Assertions.assertTrue(json.contains("account-id"));
        Assertions.assertTrue(json.contains("amount"));
        Assertions.assertTrue(json.contains("source"));
        Assertions.assertTrue(json.contains("symbol"));
        Assertions.assertTrue(json.contains("type"));
        Assertions.assertTrue(json.contains("client-order-id"));
    }

    @Test
    void toSpotOrder() {
        HuobiSpotOrder huobiSpotOrder = new HuobiSpotOrder();
        huobiSpotOrder.setAccountId("10009");
        huobiSpotOrder.setAmount(new BigDecimal("10.12"));
        huobiSpotOrder.setPrice(new BigDecimal("100.1"));
        huobiSpotOrder.setSource("api");
        huobiSpotOrder.setType("sell-limit");
        huobiSpotOrder.setClientOrderId("a0001");
        huobiSpotOrder.setSymbol("btcusdt");

        SpotOrder spotOrder = huobiSpotOrder.toSpotOrder();
        Assertions.assertEquals(new BigDecimal("10.12"), spotOrder.getQuantity());
        Assertions.assertEquals(new BigDecimal("100.1"), spotOrder.getPrice());
        Assertions.assertEquals(SpotOrder.Type.LIMIT, spotOrder.getType());
        Assertions.assertEquals(SpotOrder.Side.SELL, spotOrder.getSide());
        Assertions.assertEquals(Symbol.of(Currency.BTC, Currency.USDT), spotOrder.getSymbol());

        Map<String, Object> extra = spotOrder.getExtraInfo();
        Assertions.assertEquals("10009", extra.get("account-id").toString());
        Assertions.assertEquals("a0001", extra.get("client-order-id").toString());
        Assertions.assertEquals("api", extra.get("source").toString());
    }

    @Test
    void testConstructor(){
        SpotOrder spotOrder = new SpotOrder();
        spotOrder.setSymbol(Symbol.of(Currency.PNT, Currency.ETH));
        spotOrder.setSide(SpotOrder.Side.BUY);
        spotOrder.setType(SpotOrder.Type.LIMIT);
        spotOrder.setPrice(new BigDecimal("1.5"));
        spotOrder.setQuantity(new BigDecimal("100"));

        HashMap<String, Object> extra = new HashMap<>();
        spotOrder.setExtraInfo(extra);
        extra.put("account-id", "A123");
        extra.put("client-order-id", "H001");
        extra.put("source", "exunion-api");
        
        HuobiSpotOrder huobiSpotOrder = new HuobiSpotOrder(spotOrder);
        Assertions.assertEquals("pnteth", huobiSpotOrder.getSymbol());
        Assertions.assertEquals("buy-limit", huobiSpotOrder.getType());
        Assertions.assertEquals(new BigDecimal("1.5"), huobiSpotOrder.getPrice());
        Assertions.assertEquals(new BigDecimal("100"), huobiSpotOrder.getAmount());

        Assertions.assertEquals("A123", huobiSpotOrder.getAccountId());
        Assertions.assertEquals("H001", huobiSpotOrder.getClientOrderId());
        Assertions.assertEquals("exunion-api", huobiSpotOrder.getSource());
    }

}