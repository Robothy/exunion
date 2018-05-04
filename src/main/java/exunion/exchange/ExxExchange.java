package exunion.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import exunion.util.UrlParameterBuilder;

public class ExxExchange extends AExchange {
	
	private static final Logger logger = LogManager.getLogger(ExxExchange.class);
	
	private static final String PLANTFORM = "exx.com";
	
	private static final Standardizable<String, String> currencyStandardizer = new Standardizable<String, String>() {

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
				case 0 : s = OrderStatus.NEW; break;
				case 3 : s = OrderStatus.PARTIALLY_FILLED; break;
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
				l = 0;
			}else if(OrderStatus.PARTIALLY_FILLED.equals(s)){
				l = 3;
			}
			return l;
		}
	}; 
	
	public ExxExchange(Boolean needProxy){
		super(needProxy);
	}
	
	//获取账户信息
	public Account getAccount() {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("accesskey", key);
		params.put("nonce", new Long(System.currentTimeMillis()).toString());
		String urlParams = UrlParameterBuilder.buildUrlParamsWithHmacSHA512Sign(secret, null, params);
		String json = client.get("https://trade.exx.com/api/getBalance?" + urlParams);
		String errorMessage = "获取 "+PLANTFORM+" 中的账户信息时出错，";
		if (null == json ){
			logger.error(errorMessage + "服务器无返回结果。");
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		JSONObject funds = jsonObject.getJSONObject("funds");
		Account account = new Account();
		List<Account.Balance> balances = new ArrayList<Account.Balance>();
		for(String fundName : funds.keySet()){
			Balance bal = new Balance();
			bal.setAsset(fundName);
			BigDecimal total = funds.getJSONObject(fundName).getBigDecimal("total");
			BigDecimal freeze = funds.getJSONObject(fundName).getBigDecimal("freeze");
			bal.setFree(total.subtract(freeze));
			bal.setLocked(freeze);
			balances.add(bal);
		}
		
		account.setBalances(balances);
		return account;
	}

	//获取深度信息
	public Depth getDepth(String currency) {
		String localizedCurrency = currencyStandardizer.localize(currency); 
	    String json = client.get("https://api.exx.com/data/v1/depth?currency=" + localizedCurrency);
	    String errorMessage = "从" + PLANTFORM + "获取币种 " + localizedCurrency + " 的深度时失败，";
	    if (null == json){
	    	logger.error( errorMessage + "服务器无返回信息");
	    	return null;
	    }
	    
	    //返回信息中含有错误码
	    if(json.contains("code")){
	    	logger.error(errorMessage + "服务器返回错误信息：" + json);
	    	return null;
	    }
	    
	    Depth depth = new Depth();
	    
	    JSONObject exxDepth = JSON.parseObject(json);
	    List<String[]> exxAsks = exxDepth.getJSONArray("asks").toJavaList(String[].class);
	    List<String[]> exxBids = exxDepth.getJSONArray("bids").toJavaList(String[].class);
	    
	    List<Depth.PriceQuotation> asks = new ArrayList<Depth.PriceQuotation>();
	    List<Depth.PriceQuotation> bids = new ArrayList<Depth.PriceQuotation>();
	    
	    for(String[] dep : exxAsks){
	    	BigDecimal price = new BigDecimal(dep[0]);
	    	BigDecimal quantity = new BigDecimal(dep[1]);
	    	PriceQuotation quotation = new PriceQuotation(price, quantity);
	    	asks.add(quotation);
	    }
	    
	    Collections.sort(asks, new Comparator<PriceQuotation>() {
	    	public int compare(PriceQuotation o1, PriceQuotation o2) {
	    		return o1.getPrice().compareTo(o2.getPrice());
	    	}
	    });
	    
	    for(String[] dep: exxBids){
	    	BigDecimal price = new BigDecimal(dep[0]);
	    	BigDecimal quantity = new BigDecimal(dep[1]);
	    	PriceQuotation quotation = new PriceQuotation(price, quantity);
	    	bids.add(quotation);
	    }
	    
	    Collections.sort(bids, new Comparator<PriceQuotation>() {
			public int compare(PriceQuotation o1, PriceQuotation o2) {
				return o2.getPrice().compareTo(o1.getPrice());
			}
		});
	    
	    depth.setCurrency(currency);
	    depth.setTimestamp(exxDepth.getLong("timestamp"));
	    
	    return depth;
	}

	// 获取行情信息
	public Ticker getTicker(String currency) {
		String localizedCurrency = currencyStandardizer.localize(currency);
		String json = client.get("https://api.exx.com/data/v1/ticker?currency=" + localizedCurrency);
		String errorMessage = "从" + PLANTFORM + "获取币种 " + localizedCurrency + " 的行情信息时失败， ";
		if (null == json){
			logger.error(errorMessage + "服务器无数据返回。");
			return null;
		}
		
		//返回信息中含有错误码
	    if(json.contains("code")){
	    	logger.error(errorMessage + "服务器返回错误信息：" + json);
	    	return null;
	    }
	    
	    JSONObject exxTicker = JSON.parseObject(json);
	    JSONObject tk = exxTicker.getJSONObject("ticker");
	    Ticker ticker = parseTicker(tk);
	    ticker.setOpenTime(exxTicker.getLong("date"));
	    ticker.setCurrency(currency);
	    return ticker; 
	}

	//获取所有币种的行情信息
	public List<Ticker> getAllTickers() {
		String json = client.get("https://api.exx.com/data/v1/tickers");
		String errorMessage = "从" + PLANTFORM + "获取所有币种的行情信息时失败。";
		if (null == json){
			logger.error(errorMessage + "服务器无数据返回。");
			return null;
		}
		
		//返回信息中含有错误码
	    if(json.contains("code")){
	    	logger.error(errorMessage + "服务器返回错误信息：" + json);
	    	return null;
	    }
	    
	    JSONObject exxTickers = JSON.parseObject(json);
	    List<Ticker> tickers = new ArrayList<Ticker>();
	    for(String currency : exxTickers.keySet()){
	    	Ticker ticker = parseTicker(exxTickers.getJSONObject(currency));
	    	tickers.add(ticker);
	    }
	    return tickers;
	}
	
	private Ticker parseTicker(JSONObject tk){
		Ticker ticker = new Ticker();
		ticker.setVolume(tk.getBigDecimal("vol"));
		ticker.setLastPrice(tk.getBigDecimal("last"));
		ticker.setAskPrice(tk.getBigDecimal("sell"));
		ticker.setBidPrice(tk.getBigDecimal("buy"));
		ticker.setRiseRate(tk.getBigDecimal("riseRate"));
		ticker.setHighPrice(tk.getBigDecimal("high"));
		ticker.setLowPrice(tk.getBigDecimal("low"));
		return ticker;
	}

	//根据币种和ID获取订单信息
	public Order getOrder(String currency, String orderId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("currency", currencyStandardizer.localize(currency));
		params.put("id", orderId);
		params.put("accesskey", key);
		params.put("nonce", new Long(System.currentTimeMillis()).toString());
		String urlParams = UrlParameterBuilder.buildUrlParamsWithHmacSHA512Sign(secret, null, params);
		String json = client.get("https://trade.exx.com/api/getOrder?" + urlParams);
		String errorMessage = "从" + PLANTFORM + "获取定单 [currency=" + currency + ", orderId=" + orderId + " 的信息时失败， ";
		if (null == json){
			logger.error(errorMessage + "服务器无数据返回。");
			return null;
		}
		
		//返回信息中含有错误码
	    if(json.contains("code")){
	    	logger.error(errorMessage + "服务器返回错误信息：" + json);
	    	return null;
	    }
	    Order order = parseOrder(JSON.parseObject(json));
	    return order;
	}

	// 获取所有的挂单
	public List<Order> getOpenOrders(String currency, String side) {
		String localCurrency = currencyStandardizer.localize(currency);
		Map<String, String> params = new HashMap<String, String>();
  		params.put("currency", localCurrency);
		if(null != side){
			params.put("type", side);
		}
		params.put("accesskey", key);
		String errorMessage = "从" + PLANTFORM + "获取[currency"+currency+", side="+side+"]的所有挂单信息时出错， ";
		List<Order> orders = new ArrayList<Order>();
		for(int page = 1; page < 1000; page++){
			String nonce = new Long(System.currentTimeMillis()).toString();
			String pageIndex = new Integer(page).toString();
			params.put("nonce", nonce);
			params.put("pageIndex", pageIndex);
			String urlParams = UrlParameterBuilder.buildUrlParamsWithHmacSHA512Sign(secret, null, params);
			String json = client.get("https://trade.exx.com/api/getOpenOrders?" + urlParams);
			
			if (null == json){
				logger.error(errorMessage + "服务器无数据返回。");
				return null;
			}
			
			//返回信息中含有错误码
		    if(json.contains("code")){
		    	logger.error(errorMessage + "服务器返回错误信息：" + json);
		    	return null;
		    }
		    
		    JSONArray ods = JSON.parseArray(json);
		    for(int i=0; i<ods.size(); i++){
		    	JSONObject obj = ods.getJSONObject(i);
		    	Order order = parseOrder(obj);
		    	orders.add(order);
		    }
		    
			if (ods.size() < 10){	// 一页10条，若少于10条，则表示是最后一页
				break;
			}
		}
		return orders;
	}

	private Order parseOrder(JSONObject jsonObject){
		Order order = new Order();
		order.setPrice(new BigDecimal(jsonObject.getString("price")));
		order.setQuantity(jsonObject.getBigDecimal("total_amount"));
	    order.setTradeQuantity(jsonObject.getBigDecimal("trade_amount"));
	    order.setPrice(jsonObject.getBigDecimal("price"));
	    order.setCurrency(currencyStandardizer.standardize("currency"));
	    order.setOrderId(jsonObject.getString("id"));
	    order.setTradeMoney(jsonObject.getBigDecimal("trade_money"));
	    order.setSide("buy".equals(jsonObject.getString("type")) ? "BUY" : "SELL");
	    order.setCreateDate(jsonObject.getLong("trade_date"));
	    order.setStatus(orderStatusStandizer.standardize(jsonObject.getInteger("status")));
		return order;
	}
	
	public List<Order> getHistoryOrders(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	//下订单
	public Order order(String side, String currency, BigDecimal quantity, BigDecimal price) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("accesskey", key);
		params.put("amount", quantity.toString());
		params.put("currency", currencyStandardizer.localize(currency));
		params.put("nonce", new Long(System.currentTimeMillis()).toString());
		params.put("price", price.toString());
		params.put("type", "BUY".equals(side) ? "buy" : "sell");
		String urlParams = UrlParameterBuilder.buildUrlParamsWithHmacSHA512Sign(secret, "signature", params);
		String json = client.get("https://trade.exx.com/api/order?" + urlParams);
		
		String orderInfo = "[ side:" + side + ", currency:" + currency + ", quantity:" + quantity + ", price:" + price + " ]";
		
		if (null == json){
			logger.error("下订单"+ orderInfo +"时出错。服务器无返回结果。");
			return null;
		}
		
		if(!json.contains("操作成功")){
			logger.error("下订单{}出错，服务器返回错误信息：{}" , orderInfo, json);
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		Order order = new Order();
		order.setOrderId(jsonObject.getString("id"));
		order.setPrice(price);
		order.setCurrency(currency);
		order.setSide(side);
		order.setStatus(OrderStatus.NEW);
		logger.info("下单成功。" + orderInfo);
		return order;
	}

	//取消订单
	public Order cancel(String currency, String orderId) {
		String localCurrency = currencyStandardizer.localize(currency);
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
