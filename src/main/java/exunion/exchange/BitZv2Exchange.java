package exunion.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import exunion.util.Md5Sign;
import exunion.util.Sign;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import exunion.metaobjects.Account;
import exunion.metaobjects.Depth;
import exunion.metaobjects.Depth.PriceQuotation;
import exunion.metaobjects.Order;
import exunion.metaobjects.OrderSide;
import exunion.metaobjects.OrderStatus;
import exunion.metaobjects.Ticker;
import exunion.metaobjects.Account.Balance;
import exunion.standardize.Standardizable;
import exunion.util.UrlParameterBuilder;

/**
 * bitz v2版本API实现类
 */
public class BitZv2Exchange extends AExchange {

    private static final String exchangeName = "bit-z.com";

    private static final String serverHost = "https://apiv2.bitz.com";

    private Sign sign;

    private static Map<String, String> header = new HashMap<>();
    static {
        header.put("User-Agent", "Mozilla/5.0 \\(Windows NT 6.1; WOW64\\) AppleWebKit/537.36 \\(KHTML, like Gecko\\) Chrome/39.0.2171.71 Safari/537.36");
    }

    Logger logger = LogManager.getLogger(BitZExchange.class);

    private Standardizable<String, String> currencyStandardizer = new Standardizable<String, String>() {

        @Override
        public String standardize(String l) {
            return l.toUpperCase();
        }

        @Override
        public String localize(String s) {
            return s.toLowerCase();
        }
    };

    private Standardizable<String, String> orderSideStandizer = new Standardizable<String, String>() {

        @Override
        public String standardize(String l) {
            return "1".equals(l) ? OrderSide.BUY : "2".equals(l) ? OrderSide.SELL : "unknow";
        }

        @Override
        public String localize(String s) {
            return OrderSide.BUY.equals(s) ? "1" : OrderSide.SELL.equals(s) ? "2" : "unknow";
        }

    };

    private Standardizable<String, String> orderStatusStandizer = new Standardizable<String, String>() {
        @Override
        public String standardize(String s) {
            return s.equals("0") ? OrderStatus.NEW :
                    s.equals("1") ? OrderStatus.PARTIALLY_FILLED :
                    s.equals("2") ? OrderStatus.FILLED :
                    s.equals("3") ? OrderStatus.CANCELED : "UNKNOW";
        }

        @Override
        public String localize(String s) {
            return null;
        }
    };

    public BitZv2Exchange(String key, String secret, Boolean needProxy) {
        super(key, secret, needProxy);
        sign = new Md5Sign();
    }

    @Override
    public Account getAccount() {
        Map<String, String> params = new HashMap<>();
        params.put("apiKey", key);
        params.put("timeStamp", Long.toString(System.currentTimeMillis()/1000));
        params.put("nonce", nonce());
        String toBeSignStr = UrlParameterBuilder.MapToUrlParameter(params);
        String urlParams = toBeSignStr + "&sign=" + sign(toBeSignStr + secret);
        String json = client.get(serverHost + "/Assets/getUserAssets?" + urlParams, header);
        if(null == json){
            logger.error("{}服务器无数据返回。", exchangeName);
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(json);

        Integer status = jsonObject.getInteger("status");

        if(null == status || 200!=status){
            logger.error("{}服务器返回错误信息: {}", exchangeName, json);
            return null;
        }
        Account account = new Account();

        jsonObject.getJSONObject("data").getJSONArray("info").forEach(e->{
            if(e instanceof JSONObject){
                JSONObject f = (JSONObject) e;
                Balance bal = new Balance();
                bal.setAsset(f.getString("name").toUpperCase());
                bal.setFree(f.getBigDecimal("over"));
                bal.setLocked(f.getBigDecimal("lock"));
                account.putBalance(bal);
            }
        });
        return account;
    }

    @Override
    public Depth getDepth(String currency) {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", currencyStandardizer.localize(currency));
        String urlParams = UrlParameterBuilder.MapToUrlParameter(params);
        String json = client.get(serverHost + "/Market/depth?" + urlParams);
        if(null == json ){
            logger.error("{}服务器无数据返回。", exchangeName);
            return null;
        }

        JSONObject oriObj = JSON.parseObject(json);

        Integer status = oriObj.getInteger("status");
        if(null == status || 200!=status){
            logger.error("{}服务器返回错误信息: {}", exchangeName, json);
            return null;
        }

        Depth depth = new Depth();

        JSONObject jsonObject = oriObj.getJSONObject("data");

        Function<String, List<PriceQuotation>> parse = (e)->{
            List<PriceQuotation> dep = new ArrayList<>();
            jsonObject.getJSONArray(e).forEach(d ->{
                if (d instanceof JSONArray){
                    JSONArray quotation = (JSONArray) d;
                    BigDecimal price = new BigDecimal(quotation.get(0).toString());
                    BigDecimal quantity = new BigDecimal(quotation.get(1).toString());
                    Depth.PriceQuotation priceQuotation = new PriceQuotation(price, quantity);
                    dep.add(priceQuotation);
                }
            });
            return dep;
        };

        depth.setBids(parse.apply("bids"));
        depth.setAsks(parse.apply("asks"));
        depth.setExchange(exchangeName);
        depth.setCurrency(currency);
        depth.setTimestamp(getTimestamp(oriObj));
        return depth;
    }

    @Override
    public Ticker getTicker(String currency) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Ticker> getAllTickers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Order getOrder(String currency, String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("apiKey", key);
        params.put("timeStamp", Long.toString(System.currentTimeMillis()/1000));
        params.put("nonce", nonce());
        params.put("entrustSheetId", orderId);
        String sign = sign(UrlParameterBuilder.MapToUrlParameter(params) + secret);
        params.put("sign", sign);
        String requestUrl = serverHost + "/Trade/getEntrustSheetInfo";
        String json = client.post(requestUrl, header, params);

        if(null == json){
            logger.error("获取订单{}时{}服务器无数据返回。", orderId,exchangeName);
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(json);

        Integer status = jsonObject.getInteger("status");

        if(null == status || 200!=status){
            logger.error("获取订单{}时{}服务器返回错误信息: {}",orderId, exchangeName, json);
            return null;
        }

        JSONObject data = jsonObject.getJSONObject("data");
        Order order = new Order();
        order.setCurrency(currency);
        order.setOrderId(orderId);
        order.setCreateDate(getTimestamp(jsonObject));
        order.setStatus(orderStatusStandizer.standardize(data.getString("status")));
        order.setTradeQuantity(data.getBigDecimal("numberDeal"));
        order.setTradeMoney(order.getTradeQuantity().multiply(data.getBigDecimal("price")));
        order.setExchangeName(exchangeName);
        return order;
    }

    @Override
    public List<Order> getOpenOrders(String currency, String side) {
        // TODO Auto-generated method stub
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
        Map<String, String> params = new HashMap<>();
        params.put("apiKey", key);
        params.put("timeStamp", Long.toString(System.currentTimeMillis()/1000));
        params.put("nonce", nonce());
        params.put("type", orderSideStandizer.localize(side));
        params.put("price", price.toString());
        params.put("number", quantity.toString());
        params.put("symbol", currencyStandardizer.localize(currency));
        params.put("tradePwd", "");

        String toEncrStr = UrlParameterBuilder.MapToUrlParameter(params) + secret;
        String sign = sign(toEncrStr);
        params.put("sign", sign);
        String json = client.post(serverHost + "/Trade/addEntrustSheet", header, params);

        if(null == json){
            logger.error("下单(currencyPair={}, OrderSide={}, OrderPrice={}, OrderQuantity={})时，{}服务器无数据返回。", currency, side, price, quantity, exchange);
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(json);

        Integer status = jsonObject.getInteger("status");
        String dataId = jsonObject.getJSONObject("data").getString("id");
        if(null == status || 200!=status || null == dataId){
            logger.error("{}服务器返回错误信息: {}", exchangeName, json);
            return null;
        }

        Order order = new Order();
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setCurrency(currency);
        order.setSide(side);
        order.setCreateDate(getTimestamp(jsonObject));
        order.setOrderId(dataId);
        order.setExchangeName(exchangeName);
        return order;
    }

    @Override
    public Order cancel(String currency, String orderId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPlantformName() {
        return exchangeName;
    }


    private String nonce(){
        return Double.toString(Math.random()).substring(2, 8);
    }

    private Long getTimestamp(JSONObject jsonObject){
        return new Long(jsonObject.getString("time") + jsonObject.getString("microtime").substring(2,5));
    }

    /**
     * 生成MD5签名
     * @param toBeSignStr 待签名字符串
     * @return 签名结果
     */
    private String sign(String toBeSignStr){
        return sign.strSign(toBeSignStr);
    }

}
