package exunion.exchange;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import exunion.config.Configuration;
import exunion.httpclient.Client;
import exunion.metaobjects.Account;
import exunion.metaobjects.Depth;
import exunion.metaobjects.Order;
import exunion.metaobjects.Ticker;
import exunion.standardize.Standardizable;
import exunion.util.UrlParameterBuilder;

public class BinanceExchange implements Exchange {

    private static final String API_KEY = "QlQzkoWqY4Y2wBVKVHnG0kCT3yXJFESexMvYEU6Zj8IaLBv2VzoDZE8yvaS2b6zA";

    private static final String API_SECRET = "37W43fTu5RE8tbEqdHnezIv7Ufqgir7kc6kXtKi3fAQjmRWmUULa5r56qeoIGrNL";

    private static final String KEY = Configuration.getProperties().getStringProperty("key");

    private static final String SECRET = Configuration.getProperties().getStringProperty("binance.com.secret");

    private static final String API_SERVER = "https://api.binance.com";

    private static final Logger logger = LogManager.getLogger(BinanceExchange.class);

    private static final String[] CURRENCYS = {"BTCUSDT", "ETHUSDT"};

    private static final String PLANT_FORM = "binance.com";

    private static final Standardizable<String, String> currencyStandizer = new Standardizable<String, String>() {

        private String[] quoteCurrencies = {"BNB", "BTC", "ETH", "USDT"};

        public String standardize(String l) {
            String s = "";
            for (String quote : quoteCurrencies) {
                if (l.endsWith(quote)) {
                    s = l.replace(quote, "_" + quote);
                    break;
                }
            }
            return "".equals(s) ? l : s;
        }

        public String localize(String s) {
            String[] currencys = s.split("\\_");
            return currencys[0] + currencys[1];
        }

    };

    private Client client = null;

    public BinanceExchange() {
        client = new Client();
    }

    public String buy(double amt, String currency, double price) {
        return order(amt, currency, price, "BUY");
    }

    public String sell(double amt, String currency, double price) {
        return order(amt, currency, price, "SELL");
    }

    /**
     * 下单
     *
     * @param amt
     * @param currency
     * @param price
     * @param direction 买 - BUY; 卖 - SELL
     * @return
     */
    private String order(double amt, String currency, double price, String direction) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("symbol", currency);
        params.put("side", direction);
        params.put("type", "LIMIT");
        params.put("timeInForce", "GTC");
        params.put("quantity", (new Double(amt)).toString());
        params.put("price", (new Double(price)).toString());
        params.put("recvWindow", "6000000");
        params.put("timestamp", (new Long(System.currentTimeMillis()).toString()));

        Map<String, String> header = new HashMap<String, String>();
        header.put("X-MBX-APIKEY", API_KEY);

        String jsonString = client.post("/api/v3/order?");

        //BinanceOrder order = null;

        if (null != jsonString) {
            //order = JSON.parseObject(jsonString, BinanceOrder.class);
        }
        return null;// == order ? null : new Long(order.getOrderId()).toString();
    }

    public int getOrderStatus(String orderId) {
        Map<String, Object> order = getOrderInfo(orderId);

        if (null == order) {
            return -1;
        }

        String status = (String) order.get("status");

        if ("NEW".equals(status)) {
            return 0;
        } else if ("CANCELED".equals(status)) {
            return 1;
        } else if ("FILLED".equals(status)) {
            return 2;
        } else if ("PARTIALLY_FILLED".equals(status)) {
            return 3;
        }

        return -1;
    }

    public boolean cancel(String orderId) {
        String jsonString = null;

        Map<String, String> header = new HashMap<String, String>();
        header.put("X-MBX-APIKEY", API_KEY);

        Map<String, String> param = new HashMap<String, String>();
        param.put("symbol", "HSRBTC");
        param.put("timestamp", (new Long(System.currentTimeMillis()).toString()));
        param.put("orderId", orderId);

        //jsonString = client.delete("/api/v3/order?");

        //BinanceOrder order = null;

        if (jsonString != null) {
            //order = JSON.parseObject(jsonString, BinanceOrder.class);
        }

        String status = null;

        //if (null != order) {
        //	status = order.getStatus();
        //}

        return status.equals("CANCELED") ? true : false;
    }

    public Map<String, Object> getOrderInfo(String orderId) {
        String jsonString = null;
        Map<String, String> header = new HashMap<String, String>();
        header.put("X-MBX-APIKEY", API_KEY);

        Map<String, String> param = new HashMap<String, String>();
        param.put("symbol", "HSRBTC");
        param.put("timestamp", (new Long(System.currentTimeMillis()).toString()));
        param.put("orderId", orderId);

        //BinanceOrder order = null;

        //jsonString = get("/api/v3/order?", null);
        if (jsonString != null) {
            //order = JSON.parseObject(jsonString, BinanceOrder.class);
        }

		/*if (order != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderId", order.getOrderId());
			map.put("status", order.getStatus());
			map.put("side", order.getSide());
			map.put("price", order.getPrice());
			map.put("currency", order.getSymbol());
			return map;
		}*/

        return null;
    }

    public Account getAccount() {
        // TODO Auto-generated method stub
        return null;
    }

    //获取深度信息
    public Depth getDepth(String currency) {
        String localCurrency = currencyStandizer.standardize(currency);
        String json = client.get("https://api.binance.com/api/v1/depth?symbol=" + localCurrency + "&limit=1000");
        String errorMessage = "未能从" + PLANT_FORM + "获取" + currency + "的深度信息。";
        if (null == json) {
            logger.info(errorMessage + "服务器无数据返回。");
            return null;
        }

        if (json.contains("code") || json.contains("message")) {
            logger.info(errorMessage + "服务器返回错误信息：" + json);
            return null;
        }

        Depth depth = null;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray askObjects = jsonObject.getJSONArray("asks");
            JSONArray bidObjects = jsonObject.getJSONArray("bids");
            //TODO 解析深度值
        } catch (JSONException ex) {
            logger.warn("解析 json 文本时出现错误。", ex);
            logger.debug(json);
            return null;
        }

        depth.setCurrency(currency);
        return depth;
    }

    //获取某币种的市场信息
    public exunion.metaobjects.Ticker getTicker(String currency) {
        String localCurrency = currencyStandizer.localize(currency);
        String json = client.get("https://api.binance.com/api/v1/ticker/24hr?symbol" + localCurrency);

        return null;
    }

    //获取所有的市场信息
    public List<Ticker> getAllTickers() {
        String json = client.get("https://api.binance.com/api/v1/ticker/24hr");

        return null;
    }

    //获取某一订单信息
    public Order getOrder(String currency, String orderId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("symbol", currencyStandizer.localize(currency));
        params.put("orderId", orderId);
        params.put("recvWindow", "500");
        params.put("timestamp", new Long(System.currentTimeMillis()).toString());
        String json = client.get("https://api.binance.com/api/v3/order?" + UrlParameterBuilder.MapToUrlParameter(params));

        String errorMessage = "从" + PLANT_FORM + "获取订单[ currency=" + currency + ", orderId=" + orderId + "] 的信息失败。";

        if (null == json) {
            logger.error(errorMessage + "服务器无数据返回。");
            return null;
        }

        if (json.contains("code") || json.contains("message")) {
            logger.warn(errorMessage + "服务器返回错误信息：" + json);
        }

        JSONObject jsonObject = null;
        Order order = null;
        try {
            jsonObject = JSON.parseObject(json);
            order = jsonObjectToOrder(jsonObject);
        } catch (JSONException e) {
            logger.warn(errorMessage + "将json文本转化为json对象时出现异常。");
            logger.debug(json);
            return null;
        }
        order.setCurrency(currency);
        return order;
    }

    //获取所有的挂单
    public List<Order> getOpenOrders(String currency, String side) {

        String json = client.get("https://api.binance.com/api/v3/openOrders?recvWindow=500&timestamp" + System.currentTimeMillis());
        String errorMessage = "未能从" + PLANT_FORM + "获取[currency=" + currency + ", side=" + side + "] 的挂单信息。";
        if (null == json) {
            logger.warn(errorMessage + "服务器无数据返回。");
            return null;
        }

        if (json.contains("code") || json.contains("message")) {
            logger.warn(errorMessage + "服务器返回错误信息：" + json);
            return null;
        }

        List<Order> orders = new ArrayList<Order>();

        try {
            JSONArray jsonArray = JSONArray.parseArray(json);
            for (int i = 0; i < jsonArray.size(); i++) {
                orders.add(jsonObjectToOrder(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            logger.warn(errorMessage + "解析json文本时出错。");
            logger.debug(json);
            return null;
        }
        return orders;
    }

    // 将一个 json 对象转化为 Order 对象
    private Order jsonObjectToOrder(JSONObject jsonObject) {
        Order order = new Order();
        order.setOrderId(jsonObject.getString("orderId"));
        order.setSide(jsonObject.getString("side"));
        order.setPrice(new BigDecimal(jsonObject.getString("price")));
        order.setQuantity(new BigDecimal(jsonObject.getString("origQty")));
        order.setStatus(jsonObject.getString("status"));
        order.setType(jsonObject.getString("type"));
        return order;
    }

    public List<exunion.metaobjects.Order> getHistoryOrders(String currency) {
        // TODO Auto-generated method stub
        return null;
    }

    public exunion.metaobjects.Order order(String side, String currency, BigDecimal quantity, BigDecimal price) {
        // TODO Auto-generated method stub
        return null;
    }

    public exunion.metaobjects.Order cancel(String currency, String orderId) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getPlantformName() {
        return PLANT_FORM;
    }

    public List<Order> getOpenOrders(String currency) {
        // TODO Auto-generated method stub
        return null;
    }

}
