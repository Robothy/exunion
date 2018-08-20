package exunion.exchange;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import exunion.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import exunion.metaobjects.Account;
import exunion.metaobjects.Depth;
import exunion.metaobjects.Order;
import exunion.metaobjects.OrderSide;
import exunion.metaobjects.OrderStatus;
import exunion.metaobjects.Ticker;
import exunion.metaobjects.Account.Balance;
import exunion.metaobjects.Depth.PriceQuotation;
import exunion.standardize.Standardizable;

abstract class HuobiBase extends AExchange {

	private Logger logger = LogManager.getLogger(HadaxExchange.class);
	
	protected void setLogger(Logger logger){
		this.logger = logger;
	}
	
	private String exchangeName = "";
	
	void setExchangeName(String exchangeName){
		this.exchangeName = exchangeName;
	}
	
	private String urlBase = "";
	
	void setUrlBase(String urlBase){
		this.urlBase = urlBase;
	}
	
	private String hostName = "";
	
	void setHostName(String hostName){
		this.hostName = hostName;
	}
	
	private static final Map<String, String> header = new HashMap<>();
	
	private static List<String> accountsId = null;
	
	static{
		header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
	}
	
	private static final Standardizable<String, String> currencyStandizer = new Standardizable<String, String>() {

		@Override
		public String standardize(String l) {
			return l.toUpperCase();
		}

		@Override
		public String localize(String s) {
			return s.replace("_", "").toLowerCase();
		}
		
	};
	
	private static final Standardizable<String, String> orderSideStandizer = new Standardizable<String, String>() {

		@Override
		public String standardize(String l) {
			if(l.contains("buy")){
				return OrderSide.BUY;
			}else{
				return OrderSide.SELL;
			}
		}

		@Override
		public String localize(String s) {
			return s.toLowerCase() + "-limit";
		}
	};
	
	private static final Standardizable<String, String> orderStatusStandizer = new Standardizable<String, String>() {
		
		@Override
		public String standardize(String l) {
			if(l.equals("filled")){
				return OrderStatus.FILLED;
			}else{
				return OrderStatus.NEW;
			}
		}

		@Override
		public String localize(String s) {
			return s;
		}
	};

	private Sign sign;
	
	HuobiBase(String key, String secret, Boolean needProxy) {
		super(key, secret, needProxy);
		sign = new HmacSign(HmacSign.HmacAlgorithm.HmacSHA256, secret);
	}
	
	
	
	
	@Override
	public Account getAccount() {
		if(null == accountsId){
			accountsId = this.getAccountsId();
			if(accountsId == null){
				logger.error("无法获取账户ID。");
				return null;
			}
		}
		
		header.put("Content-Type", "application/x-www-form-urlencoded");
		Map<String, String> params = commonParams();
		String path = "/v1/hadax/account/accounts/" + accountsId.get(0) + "/balance";
		String sign = sign("GET", hostName,  path, params);
		String requestUrl = UrlParameterBuilder.MapToUrlParameter(params) + "&Signature=" + sign;
		String json = client.get(urlBase + path + "?" + requestUrl, header);
		if(null == json){
			logger.error("获取账户{}信息时服务器{}无数据返回。", accountsId.get(0), exchangeName);
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		
		if(null == jsonObject.getString("status") || !"ok".equalsIgnoreCase(jsonObject.getString("status"))){
			logger.error("获取账户{}信息时服务器{}返回错误信息: {}", accountsId.get(0), exchangeName, json);
			return null;
		}
		
		Account account = new Account();		
		Map<String, Balance> balances = new ConcurrentHashMap<String, Account.Balance>();
		jsonObject.getJSONObject("data").getJSONArray("list")
		.parallelStream().forEach(obj -> {
			if(obj instanceof JSONObject){
				JSONObject jsonObj = (JSONObject) obj;
				String currency = jsonObj.getString("currency").toUpperCase();
				BigDecimal bal = jsonObj.getBigDecimal("balance");
				if(null == balances.get(currency)){
					Balance balance = new Balance();
					balance.setAsset(currency);
					balances.put(currency, balance);
				}
				if("trade".equalsIgnoreCase(jsonObj.getString("type"))){
					balances.get(currency).setFree(bal);
				}else{
					balances.get(currency).setLocked(bal);
				}
			}
			
		});
		
		account.setBalances(balances);
		
		return account;
	}

	@Override
	public Depth getDepth(String currency) {
		
		Map<String, String> params = new HashMap<>();
		params.put("symbol", currencyStandizer.localize(currency));
		params.put("type", "step0");
		
		String json  = client.get(urlBase + "/market/depth?" + UrlParameterBuilder.MapToUrlParameter(params), header);
		if(null == json){
			logger.error("获取{}深度信息时{}服务器无数据返回。", currency, exchangeName);
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		if(jsonObject.getString("status") == null || !"ok".equals(jsonObject.getString("status"))){
			logger.error("获取{}深度信息时{}服务器返回错误信息:{}", currency, exchangeName, json);
			return null;
		}

		JSONObject tick = jsonObject.getJSONObject("tick");

		Depth depth = new Depth();
		Function<String, List<PriceQuotation>> parse = dep->{
			List<PriceQuotation> result = new ArrayList<>();
			tick.getJSONArray(dep)
					.parallelStream()
					.forEach(e->{
						if(e instanceof JSONArray){
							JSONArray pq = (JSONArray) e;
							PriceQuotation priceQuotation = new PriceQuotation();
							priceQuotation.setPrice(new BigDecimal(pq.get(0).toString()));
							priceQuotation.setQuantity(new BigDecimal(pq.get(1).toString()));
							result.add(priceQuotation);
						}
					});
			return result;
		};
		
		depth.setAsks(parse.apply("asks"));
		depth.setBids(parse.apply("bids"));
		depth.setCurrency(currency);
		depth.setExchange(exchangeName);
		depth.setTimestamp(jsonObject.getLong("ts"));
		
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
		Map<String, String> params = commonParams();
		String path = "/v1/order/orders/" + orderId;
		
		String sign = sign("GET", hostName, path, params);
		String requestUrl = urlBase + path + "?" + UrlParameterBuilder.MapToUrlParameter(params) + "&Signature=" + sign;
		header.put("Content-Type", "application/x-www-form-urlencoded");
		String json = client.get(requestUrl, header);
		
		if(null == json){
			logger.error("获取订单{}信息时{}服务器无数据返回。", orderId, exchangeName);
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		if(null == jsonObject.getString("status") || !"ok".equalsIgnoreCase(jsonObject.getString("status"))){
			logger.error("获取订单{}信息时{}服务器返回错误信息：{}", orderId, exchangeName, json);
			return null;
		}
		
		jsonObject = jsonObject.getJSONObject("data");
		Order order = new Order();
		order.setOrderId(orderId);
		order.setCurrency(currency);
		order.setSide(orderSideStandizer.standardize(jsonObject.getString("type")));
		order.setCreateDate(jsonObject.getLong("created-at"));
		order.setPrice(jsonObject.getBigDecimal("price"));
		order.setQuantity(jsonObject.getBigDecimal("amount"));
		order.setTradeQuantity(jsonObject.getBigDecimal("field-amount"));
		order.setTradeMoney(jsonObject.getBigDecimal("field-cash-amount"));
		order.setStatus(orderStatusStandizer.standardize(jsonObject.getString("state")));
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> getHistoryOrders(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order order(String side, String currency, BigDecimal quantity, BigDecimal price) {
		Map<String, String> params = commonParams();
		if(null == accountsId){
			accountsId = getAccountsId();
			if(null == accountsId){
				logger.error("无法获取挂单账号。");
				return null;
			}
		}
		
		Map<String, String> formData = new HashMap<>();
		formData.put("account-id", accountsId.get(0));
		formData.put("amount", quantity.stripTrailingZeros().toPlainString());
		formData.put("price", price.stripTrailingZeros().toPlainString());
		formData.put("symbol", currencyStandizer.localize(currency));
		formData.put("type", orderSideStandizer.localize(side));
		
		String path = "ETH_BTC".equals(currency) ? "/v1/order/orders/place" : "/v1/hadax/order/orders/place";
		String sign = sign("POST", "ETH_BTC".equals(currency) ? "api.huobi.pro" : hostName, path, params);
		String requestUrl = ("ETH_BTC".equals(currency) ? "https://api.huobi.pro" : urlBase) + path + "?" + UrlParameterBuilder.MapToUrlParameter(params) + "&Signature=" + sign;
		header.put("Content-Type", "application/json");
		params.put("Signature", sign);
		String json = client.post(requestUrl, header, JSON.toJSONString(formData));
		
				
		if(null == json){
			logger.error("挂单失败，{}服务器无数据返回。", exchangeName);
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		if(null == jsonObject.getString("status") || !"ok".equalsIgnoreCase(jsonObject.getString("status"))){
			logger.error("挂单失败， {}服务器返回错误信息：{}", exchangeName, json);
			return null;
		}
		
		Order order = new Order();
		order.setCurrency(currency);
		order.setSide(side);
		order.setPrice(price);
		order.setQuantity(quantity);
		order.setStatus(OrderStatus.NEW);
		order.setOrderId(jsonObject.getString("data"));
		order.setCreateDate(System.currentTimeMillis());
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
	
	private List<String> getAccountsId(){

		List<String> accountsId = Collections.synchronizedList(new ArrayList<>());
		Map<String, String> params = commonParams();
		String sign = sign("GET", hostName, "/v1/account/accounts", params);
		
		String urlParams = UrlParameterBuilder.MapToUrlParameter(params);
		
		header.put("Content-Type", "application/x-www-form-urlencoded");
		String requestUrl = urlBase+ "/v1/account/accounts?" + urlParams + "&Signature=" + sign;
		
		String json = client.get(requestUrl, header);
		
		if(null == json){
			logger.error("获取账户ID时{}服务器无数据返回。", exchangeName);
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		
		if(null == jsonObject.get("status") || !"ok".equalsIgnoreCase(jsonObject.getString("status"))){
			logger.error("获取账户ID时{}服务器返回错误信息: {}", exchangeName, json);
			return null;
		}
		
		jsonObject.getJSONArray("data")
		.parallelStream()
		.forEach(e -> {
			if(e instanceof JSONObject){
				JSONObject jsonObj = (JSONObject) e;
				if("spot".equalsIgnoreCase(jsonObj.getString("type"))){
					accountsId.add(jsonObj.getString("id"));
				}
			}
		});
		
		return accountsId;
	}
	
	private String sign(String method, String host, String path, Map<String, String> params){

		StringBuilder toBeEncodStr = new StringBuilder();
		toBeEncodStr
		.append(method)
		.append("\n")
		.append(host)
		.append("\n")
		.append(path)
		.append("\n")
		.append(UrlParameterBuilder.MapToUrlParameter(params)); 		
		
		
		//byte[] signByte = EncryptionTools.HmacSHA256Hex(secret, toBeEncodStr.toString());
		byte[] signByte = sign.hexSign(toBeEncodStr.toString());
		
		String sign = null;
		
		try {
			sign = URLEncoder.encode(Base64.encodeBase64String(signByte), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("对请求URL编码时出错。", e);
		}
		
		return sign;
	}
	
	private Map<String, String> commonParams(){
		Map<String, String> params = new HashMap<>();
		String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(GetUTCTimeUtil.getUTCTime()).replace(":", "%3A");
		params.put("AccessKeyId", key);
		params.put("SignatureMethod", "HmacSHA256");
		params.put("SignatureVersion", "2");
		params.put("Timestamp", timestamp);
		return params;
	}
	
}
