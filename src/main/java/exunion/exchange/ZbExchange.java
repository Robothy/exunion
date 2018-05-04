package exunion.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import exunion.metaobjects.Account;
import exunion.metaobjects.Depth;
import exunion.metaobjects.Order;
import exunion.metaobjects.OrderStatus;
import exunion.metaobjects.Ticker;
import exunion.metaobjects.Account.Balance;
import exunion.metaobjects.Depth.PriceQuotation;
import exunion.standardize.Standardizable;
import exunion.util.EncryptionTools;
import exunion.util.UrlParameterBuilder;

public class ZbExchange extends AExchange {
	
	private static final Logger logger = LogManager.getLogger(ZbExchange.class);
	
	private static final String PLANTFORM = "zb.com";
	
	private static final Standardizable<String, String> currencyStandizer = new Standardizable<String, String>() {

		public String standardize(String l) {
			return l.toUpperCase();
		}

		public String localize(String s) {
			return s.toLowerCase();
		}
	}; 
	
	private static final Standardizable<String, Integer> orderStatusStandizer = new Standardizable<String, Integer>() {

		String s = "";
		public String standardize(Integer l) {
			switch(l){
				case 1 : s = OrderStatus.CANCELED; break;
				case 2 : s = OrderStatus.FILLED; break;
				case 3 : s = OrderStatus.NEW; break;
			}
			return s;
		}

		public Integer localize(String s) {
			Integer l = null;
			if(OrderStatus.CANCELED.equals(s)){
				l = 1;
			}else if(OrderStatus.FILLED.equals(s)){
				l = 2;
			}else if(OrderStatus.NEW.equals(s)){
				l = 3;
			}
			return l;
		}
	};
	
	/**
	 * 
	 * @param key 交易所供身份验证的key
	 * @param secret 交易所供身份验证的secret
	 * @param needProxy 是否需要走代理标志
	 */
	protected ZbExchange(String key, String secret, Boolean needProxy){
		super(key, secret, needProxy);
	}
	
	public AExchange setSecret(String secret){
		secret = EncryptionTools.SHA1(secret);
		return this;
	}
	
	//获取账户信息
	public Account getAccount() {
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("method", "getAccountInfo");
		params.put("accesskey", key);
		String urlParams = UrlParameterBuilder.buildUrlParamsWithHmacMD5Sign(secret, "sign", params);
		urlParams = urlParams + "&reqTime=" + new Long(System.currentTimeMillis()).toString();
		String json = client.get("https://trade.zb.com/api/getAccountInfo?" + urlParams );
		
		String errorMessage = "从" + PLANTFORM + "获取账户信息失败。";
		
		if (null == json){
			logger.warn(errorMessage + "服务器无数据返回。");
			return null;
		}
		
		if( json.contains("message")){
			logger.warn(errorMessage + "服务器返回错误信息：" + json);
			return null;
		}
		
		Account account = new Account();
		List<Account.Balance> balance = new ArrayList<Account.Balance>();
		
		try{
			JSONObject jsonObject = JSON.parseObject(json);
			JSONObject result = jsonObject.getJSONObject("result");
			JSONArray coins = result.getJSONArray("coins");
			for (int i=0; i<coins.size(); i++){
				JSONObject coin = coins.getJSONObject(i);
				Balance bal = new Balance();
				bal.setAsset(coin.getString("enName"));
				bal.setFree(new BigDecimal(coin.getString("available")));
				bal.setLocked(new BigDecimal(coin.getString("freez")));
				balance.add(bal);
			}
			account.setBalances(balance);
		}catch(JSONException e){
			logger.warn(errorMessage + "解析 json 文本时出现异常。" + json, e);
			return null;
		}
		
		return account;
	}

	//获取深度信息
	public Depth getDepth(String currency) {
		
		String localCurrency = currencyStandizer.localize(currency);
		String size = "50";		
		String urlParams = "market=" + localCurrency;
		urlParams = urlParams + "&size=" + size;
		String json = client.get("http://api.zb.com/data/v1/depth?" + urlParams);
		String errorMessage = "从" + PLANTFORM +"获取深度信息失败。";
		if(null == json){
			logger.error(errorMessage + "服务器无数据返回。");
			return null;
		}
		
		if( json.contains("code") || json.contains("message") ){
			logger.error(errorMessage + "服务器返回错误信息：" + json);
			return null;
		}
		
		Depth depth = new Depth();
		
		try{
			JSONObject jsonObject = JSON.parseObject(json);
			depth.setTimestamp(jsonObject.getLong("timestamp"));
			JSONArray asks = jsonObject.getJSONArray("asks");
			JSONArray bids = jsonObject.getJSONArray("bids");
			depth.setAsks(this.parseQuotations(asks) );
			depth.setBids(this.parseQuotations(bids));
		}catch(JSONException e){
			logger.warn(errorMessage + "解析json文本时出现异常。" + json, e);
			return null;
		}
		depth.setCurrency(currency);
		return depth;
	}
	
	//转换报价
	private List<Depth.PriceQuotation> parseQuotations(JSONArray quotionObject){
		List<PriceQuotation> quotations = new ArrayList<PriceQuotation>();
		for (int i=0; i<quotionObject.size(); i++){
			JSONArray array = quotionObject.getJSONArray(i);
			BigDecimal price = new BigDecimal(array.getString(0));
			BigDecimal quantity = new BigDecimal(array.getString(1));
			Depth.PriceQuotation quotation = new Depth.PriceQuotation(price, quantity);
			quotations.add(quotation);
		}
		return quotations;
	}

	// 获取行情信息
	public Ticker getTicker(String currency) {
		String localCurrency = currencyStandizer.localize(currency);
		String errorMessage = "获取 " + currency + " 的行情信息失败。";
		String json = client.get("http://api.zb.com/data/v1/ticker?market=" + localCurrency);

		if (null == json){
			logger.warn(errorMessage + "服务器无数据返回。");
			return null;
		}
		
		if(json.contains("code") || json.contains("message")){
			logger.warn(errorMessage + "服务器返回错误信息：" + json );
			return null;
		}
		
		Ticker ticker = new Ticker();
		
		try{
			JSONObject jsonObject = JSON.parseObject(json );
			JSONObject tk = jsonObject.getJSONObject("ticker");
			ticker.setVolume(new BigDecimal(tk.getString("vol")));
			ticker.setAskPrice(new BigDecimal(tk.getString("sell")));
			ticker.setBidPrice(new BigDecimal(tk.getString("buy")));
			ticker.setHighPrice(new BigDecimal(tk.getString("high")));
			ticker.setLowPrice(new BigDecimal(tk.getString("low")));
			ticker.setLastPrice(new BigDecimal(tk.getString("last")));
		}catch(JSONException e){
			logger.warn(errorMessage + "解析 json 文本时出现异常。" + json, e);
			return null;
		}
		ticker.setCurrency(currency);
		return ticker;
	}

	//获取所有币种的行情信息
	public List<Ticker> getAllTickers() {
		// TODO Auto-generated method stub
		return null;
	}

	//根据币种和ID获取订单信息
	public Order getOrder(String currency, String orderId) {
		String localCurrency = currencyStandizer.localize(currency);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accesskey", key);
		params.put("currency", localCurrency);
		params.put("id", orderId);
		params.put("method", "getOrder");
		String urlParams = UrlParameterBuilder.buildUrlParamsWithHmacMD5Sign(secret, "sign", params);
		urlParams = urlParams + "&reqTime=" + new Long(System.currentTimeMillis()).toString();
		String json = client.get("https://trade.zb.com/api/getOrder?" + urlParams);
		String errorMessage = "获取订单[ currency=" + currency + ", orderId=" + orderId + "]的信息失败。";
		if(null == json ){
			logger.warn(errorMessage + "服务器无数据返回。");
			return null;
		}
		
		if(json.contains("code") || json.contains("message")){
			logger.warn(errorMessage + "服务器返回错误信息：" + json);
			return null;
		}
		
		Order order = null;
		try {
			JSONObject jsonObject = JSON.parseObject(json );
			order = parseOrder(jsonObject);
		} catch (JSONException e) {
			logger.error( errorMessage + "解析 json 文本时出现异常。" + json, e);
			return null;
		}
		order.setCurrency(currency);
		return order;
	}

	// 获取所有的挂单
	public List<Order> getOpenOrders(String currency, String type) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("method", "getOrders");
		params.put("accesskey", key);
		if(null != type && (type.equals("BUY") || type.equals("SELL"))){
			params.put("tradeType", type.equals("BUY") ? "1" : "0");
		}
		params.put("currency", currencyStandizer.localize(currency));
		String errorMessage = "从" + PLANTFORM + " 获取 [currency=" + currency + "type=" + type + "]所有订单失败。";
		
		List<Order> orders = new ArrayList<Order>();
		
		for(Integer page = 1; ; page++){
			params.put("pageIndex", page.toString());
			String json = client.get("https://trade.zb.com/api/getOrders?" 
			+ UrlParameterBuilder.buildUrlParamsWithHmacMD5Sign(secret, "sign", params) 
			+ "&reqTime=" + new Long(System.currentTimeMillis()).toString());
			if(null == json){
				logger.warn(errorMessage + "服务器无数据返回。");
				return null;
			}
			
			if(json.contains("code") || json.contains("message")){
				logger.warn(errorMessage + "服务器返回错误：" + json);
				return null;
			}
			JSONArray jsonArray = null;
			try {
				jsonArray = JSON.parseArray(json);
				for(int i=0; i<jsonArray.size(); i++){
					Order order = parseOrder(jsonArray.getJSONObject(i));
					order.setCurrency(currency);
					orders.add(order );
				}
			} catch (Exception e) {
				logger.warn(errorMessage + "解析 json 文本时出现异常。" + json, e);
				return null;
			}
			if(jsonArray.size() < 10){//最后一页
				break;
			}
		}
		return orders;
	}

	private Order parseOrder(JSONObject jsonObject){
		Order order = new Order();
		order.setPrice(new BigDecimal(jsonObject.getString("price")));
		order.setTradeMoney(new BigDecimal(jsonObject.getString("trade_money")));
		order.setQuantity(new BigDecimal(jsonObject.getString("total_amount")));
		order.setTradeQuantity(new BigDecimal(jsonObject.getString("trade_amount")));
		order.setSide(jsonObject.getInteger("type") == 1 ? "BUY" : "SELL");
		order.setStatus(orderStatusStandizer.standardize(jsonObject.getInteger("status")));
		order.setOrderId(jsonObject.getString("id"));
		order.setCreateDate(jsonObject.getLong("trade_date"));
		return order;
	}
	
	public List<Order> getHistoryOrders(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	//下订单
	public Order order(String side, String currency, BigDecimal quantity, BigDecimal price) {
		String tradeType = side.equals("BUY")? "1" : side.equals("SELL") ? "0" : side;
		String localCurrency = currencyStandizer.localize(currency);
		Map<String, String> params = new HashMap<String, String>();
		params.put("tradeType", tradeType);
		params.put("currency", localCurrency);
		params.put("method", "order");
		params.put("accesskey", key);
		params.put("price", price.toString());
		params.put("amount", quantity.toString());
		String urlParams = UrlParameterBuilder.buildUrlParamsWithHmacMD5Sign(secret, "sign", params);
		urlParams = urlParams + "&reqTime=" + new Long(System.currentTimeMillis()).toString();
		String json = client.get("https://trade.zb.com/api/order?" + urlParams);
		String errorMessage = "在" + PLANTFORM + "下订单 [side=" + side + ", currency=" +currency + ", price=" + price + ", quantity=" + quantity + "]失败。";
		
		if(null == json){
			logger.error(errorMessage + "服务器无数据返回。");
			return null;
		}
		
		Order order = null;
		try {
			JSONObject jsonObject = JSON.parseObject(json);
			String code = jsonObject.getString("code");
			if(!"1000".equals(code)){
				logger.error(errorMessage + "服务器返回错误信息：" + json);
				return null;
			}else{
				order = new Order();
				order.setOrderId(jsonObject.getString("id"));
			}
		} catch (JSONException e) {
			logger.error(errorMessage + "解析 json 文本时出现异常。" + json, e);
			return null;
		}
		order.setCurrency(currency);
		order.setSide(side);
		order.setStatus(OrderStatus.NEW);
		return order;
	}

	//取消订单
	public Order cancel(String currency, String orderId) {
		String localCurrency = currencyStandizer.localize(currency);
		Map<String, String> params = new HashMap<String, String>();
		params.put("accesskey", key);
		params.put("currency", localCurrency);
		params.put("id", orderId);
		params.put("method", "cancelOrder");
		String urlParams = UrlParameterBuilder.buildUrlParamsWithHmacMD5Sign(secret, "sign", params);
		urlParams = urlParams + "&reqTime=" + new Long(System.currentTimeMillis()).toString();
		String json = client.get("https://trade.zb.com/api/cancelOrder?" + urlParams);
		String errorMessage = "取消订单[ currency=" + currency + ", orderId=" + orderId + "]失败。";
		if (null == json){
			logger.error(errorMessage + "服务器无数据返回。");
			return null;
		}
		
		Order order = null;
		
		try {
			JSONObject jsonObject = JSON.parseObject(json);
			String code = jsonObject.getString("code");
			if(!"1000".equals(code)){
				logger.error(errorMessage + "服务器返回错误信息：" + json);
				return null;
			}else{
				order = new Order();
			}
		} catch (JSONException e) {
			logger.error(errorMessage + "解析 json 文本时出错。" + json, e);
			return null;
		}
		
		order.setCurrency(currency);
		order.setOrderId(orderId);
		return order;
	}

	public String getPlantformName() {
		return PLANTFORM;
	}

	public List<Order> getOpenOrders(String currency) {
		return this.getOpenOrders(currency, null);
	}
	
}
