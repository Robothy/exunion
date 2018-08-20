package exunion.exchange;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import exunion.metaobjects.*;
import exunion.standardize.Standardizable;
import exunion.util.EncryptionTools;
import exunion.util.StringUtils;
import exunion.util.UrlParameterBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class FCoinExchange extends AExchange {

    private static final String host = "https://api.fcoin.com";

    private static final String exchangeName = "fcoin.com";

    private static final Logger logger = LogManager.getLogger(FCoinExchange.class);

    // 本机与服务器的时间误差
    private Long timeDeviation;

    private static Standardizable<String, String> currencyStandizer = new Standardizable<String, String>() {
        @Override
        public String standardize(String o) {
            return null;
        }

        @Override
        public String localize(String o) {
            return o.replace("_", "").toLowerCase();
        }
    };

    private static Standardizable<String, String> orderSideStandizer = new Standardizable<String, String>() {
        @Override
        public String standardize(String o) {
            return o.toUpperCase();
        }

        @Override
        public String localize(String o) {
            return o.toLowerCase();
        }
    };

    private static Standardizable<String, String> orderStatusStandizer = new Standardizable<String, String>() {
        @Override
        public String standardize(String s) {
            if("submitted".equals(s)){
                return OrderStatus.NEW;
            }else if("filled".equals(s)){
                return OrderStatus.FILLED;
            }else{
                return s.toUpperCase();
            }
        }

        @Override
        public String localize(String s) {
            return null;
        }
    };

    public FCoinExchange(String key, String secret, Boolean needProxy){
        super(key, secret, needProxy);
        timeDeviation = syncTime();
    }

    @Override
    public Account getAccount() {

        String path = "/v2/accounts/balance";

        String json = client.get(host + path, signedHeader("GET", path, null));
        if (null==json){
            logger.error("获取账户信息时{}服务器无数据返回。", exchangeName);
            return null;
        }

        JSONObject jsonObject = JSONObject.parseObject(json);

        if(null == jsonObject.getInteger("status") || 0 != jsonObject.getInteger("status")){
            logger.error("获取账户信息时{}服务器返回错误信息：{}", exchangeName, json);
            return null;
        }

        Account account = new Account();
        Map<String, Account.Balance> balances = new ConcurrentHashMap<>();
        jsonObject.getJSONArray("data")
                .parallelStream()
                .forEach(e ->{
                    if(e instanceof JSONObject){
                        JSONObject jsonObj = (JSONObject) e;
                        Account.Balance balance = new Account.Balance();
                        String currency = jsonObj.getString("currency").toUpperCase();
                        balance.setAsset(currency);
                        balance.setFree(jsonObj.getBigDecimal("available"));
                        balance.setLocked(jsonObj.getBigDecimal("frozen"));
                        balances.put(currency, balance);
                    }
                });
        account.setBalances(balances);

        return account;
    }

    @Override
    public Depth getDepth(String currency) {

        String json = client.get("https://api.fcoin.com/v2/market/depth/L20/" + currencyStandizer.localize(currency));

        if(null == json){
            logger.error("获取{}深度数据时{}服务器无数据返回。", currency, exchangeName);
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(json);

        if(null == jsonObject.getLong("status") || 0 != jsonObject.getLong("status")){
            logger.error("获取{}深度数据时{}服务器返回错误信息：{}", currency, exchangeName, json);
            return null;
        }

        JSONObject depthObj = jsonObject.getJSONObject("data");

        Function<String, List<Depth.PriceQuotation>> parseDepth = (type) -> {
             JSONArray jsonArray = depthObj.getJSONArray(type);
             List<Depth.PriceQuotation> priceQuotations = new ArrayList<>();
             for(int i=0; i<jsonArray.size(); i+=2){
                 Depth.PriceQuotation pq = new Depth.PriceQuotation();
                 pq.setPrice(new BigDecimal(jsonArray.getString(i)));
                 pq.setQuantity(new BigDecimal(jsonArray.getString(i+1)));
                 priceQuotations.add(pq);
             }
             return priceQuotations;
        };

        Depth depth = new Depth();
        depth.setCurrency(currency);
        depth.setBids(parseDepth.apply("bids"));
        depth.setAsks(parseDepth.apply("asks"));
        depth.setExchange(exchangeName);
        depth.setTimestamp(depthObj.getLong("ts"));
        return depth;
    }

    @Override
    public Ticker getTicker(String currency) {
        String json = client.get(host + "/v2/market/ticker/" + currencyStandizer.localize(currency));
        if(StringUtils.isEmpty(json)){
            logger.error("获取{}行情信息时{}服务器无数据返回。", currency, exchangeName);
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(json);
        Long status = jsonObject.getLong("status");
        if(null == status || 0 != status){
            logger.error("获取{}行情信息时{}服务器返回错误信息：{}", currency, exchangeName, json);
            return null;
        }

        Ticker ticker = new Ticker();
        String[] tk = jsonObject.getJSONObject("data").getJSONArray("ticker").toJavaObject(String[].class);
        ticker.setLastPrice(new BigDecimal(tk[0]));
        ticker.setLastQuantity(new BigDecimal(tk[1]));
        ticker.setBidPrice(new BigDecimal(tk[2]));
        ticker.setAskPrice(new BigDecimal(tk[4]));
        ticker.setVolume(new BigDecimal(tk[10]));

        return ticker;
    }

    @Override
    public List<Ticker> getAllTickers() {
        return null;
    }

    @Override
    public Order getOrder(String currency, String orderId) {
        String method = "GET";
        String path = "/v2/orders/" + orderId;

        String json = client.get(host + path, signedHeader(method, path, null));

        if(null == json){
            logger.error("获取订单{}信息时{}服务器无数据返回。", orderId, exchangeName);
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(json);
        Integer status = jsonObject.getInteger("status");
        if(null == status || 0!= status){
            logger.error("获取订单{}信息时{}服务器返回错误信息：{}", orderId, exchangeName, json);
            return null;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        Order order = new Order();
        order.setType(data.getString("type"));
        order.setCreateDate(data.getLong("created_at"));
        order.setQuantity(data.getBigDecimal("amount"));
        order.setPrice(data.getBigDecimal("price"));
        order.setOrderId(orderId);
        order.setSide(orderSideStandizer.standardize(data.getString("side")));
        order.setCurrency(currency);
        order.setTradeQuantity(data.getBigDecimal("filled_amount"));
        order.setTradeMoney(data.getBigDecimal("executed_value"));
        order.setStatus(orderStatusStandizer.standardize(data.getString("state")));
        order.setExchangeName(exchangeName);
        return order;
    }

    @Override
    public List<Order> getOpenOrders(String currency, String side) {
        return null;
    }

    @Override
    public List<Order> getOpenOrders(String currency) {
        return null;
    }

    @Override
    public List<Order> getHistoryOrders(String currency) {
        return null;
    }

    @Override
    public Order order(String side, String currency, BigDecimal quantity, BigDecimal price) {
        String method = "POST";

        String path = "/v2/orders";

        Map<String, String> params = new HashMap<>();
        params.put("amount", quantity.toString());
        params.put("price", price.toString());
        params.put("side", orderSideStandizer.localize(side));
        params.put("symbol", currencyStandizer.localize(currency));
        params.put("type", "limit");

        Map<String, String> header = signedHeader(method, path, params);

        String json = client.post(host + path, header, JSON.toJSONString(params));


        if(null == json){
            logger.error("下单(currency={}, price={}, quantity={})时{}服务器无数据返回。", currency, price, quantity, exchangeName);
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(json);

        Integer status = jsonObject.getInteger("status");
        if(null == status || 0!= status){
            logger.error("下单(currency={}, price={}, quantity={})时{}服务器返回错误信息：{}。", currency, price, quantity, exchangeName, json);
            return null;
        }

        Order order = new Order();
        order.setCurrency(currency);
        order.setSide(side);
        order.setOrderId(jsonObject.getString("data"));
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setCreateDate(System.currentTimeMillis());
        order.setType("LIMIT");
        order.setExchangeName(exchangeName);
        return order;
    }

    @Override
    public Order cancel(String currency, String orderId) {
        return null;
    }


    @Override
    public String getPlantformName() {
        return exchangeName;
    }

    /**
     * 生成签名
     * @param method http方法 GET, POST, PUT 等
     * @param path 相应的接口路径
     * @param timestamp 时间戳
     * @param params 参数
     * @return 签名
     */
    private String sign(String method, String path, String timestamp, Map<String, String> params){
        String result;
        StringBuilder stringBuilder = new StringBuilder(method);
        stringBuilder.append(host)
                .append(path)
                .append(timestamp)
                .append(UrlParameterBuilder.MapToUrlParameter(params));
        String dataToBeSign = Base64.getEncoder().encodeToString(stringBuilder.toString().getBytes());
        byte[] toBeEncode = EncryptionTools.HmacSHA1Hex(secret, dataToBeSign);
        result = Base64.getEncoder().encodeToString(toBeEncode);
        return result;
    }

    /**
     * 带签名的头部KEY-VALUE
     * @param method HTTP请求方法
     * @param path 资源路径
     * @param params 参数
     * @return 带签名的头部信息
     */
    private Map<String, String> signedHeader(String method, String path, Map<String, String> params){
        Map<String, String> result = new HashMap<>();
        String timestamp = Long.toString(System.currentTimeMillis() + timeDeviation);
        result.put("FC-ACCESS-KEY", key);
        result.put("FC-ACCESS-SIGNATURE", sign(method, path, timestamp, params));
        result.put("FC-ACCESS-TIMESTAMP", timestamp);
        result.put("Content-Type", "application/json");
        return result;
    }

    private Long syncTime(){

        String json = client.get(host + "/v2/public/server-time");
        if(null == json){
            logger.error("与{}服务器同步时间时无数据返回。时间同步失败，默认无时间误差。", exchangeName);
            return 0L;
        }
        Long serverTime = JSON.parseObject(json).getLong("data");

        return serverTime - System.currentTimeMillis();
    }

}
