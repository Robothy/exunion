package exunion.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import exunion.metaobjects.*;
import exunion.util.UrlParameterBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import exunion.metaobjects.Account.Balance;
import exunion.metaobjects.Depth.PriceQuotation;
import exunion.standardize.Standardizable;
import exunion.util.EncryptionTools;

public class GateExchange extends AExchange {

	private static final String EXCHANGE_NAME = "gate.io";
	
	private static final String HOST = "https://data.gateio.io";
	
	private static final Logger LOGGER = LogManager.getLogger(GateExchange.class);
	
	private static final Standardizable<String, String> currencyStandizer = new Standardizable<String, String>() {

		public String standardize(String l) {
			return l.toUpperCase();
		}

		public String localize(String s) {
			return s.toLowerCase();
		}
	};

	private static final Standardizable<String, String> orderStatusStandizer = new Standardizable<String, String>() {
		@Override
		public String standardize(String s) {
			return "open".equals(s) ? OrderStatus.NEW
					: "cancelled".equals(s) ? OrderStatus.CANCELED
					: "done".equals(s) ? OrderStatus.FILLED
					: s;

		}

		@Override
		public String localize(String s) {
			return null;
		}
	};
	
	public GateExchange(String key, String secret, Boolean needProxy) {
		super(key, secret, needProxy);
	}

	@Override
	public Account getAccount() {

		String json = client.post(HOST+"/api2/1/private/balances", buildHeader(null));
		if(null == json){
			LOGGER.error("从{}服务器获取账户余额时无数据返回。", EXCHANGE_NAME);
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		if(!"true".equals(jsonObject.get("result"))){
			LOGGER.error("获取账户信息时{}服务器返回错误信息: {}", EXCHANGE_NAME, json);
			return null;
		}
		
		Account account = new Account();
		Map<String, Balance> balances = new ConcurrentHashMap<>();
		JSONObject available = jsonObject.getJSONObject("available");
		available.keySet()
		.parallelStream()
		.forEach(e ->{
			Balance balance = new Balance();
			balance.setAsset(e);
			balance.setFree(available.getBigDecimal(e));
			balances.put(e, balance);
		});
		
		JSONObject locked = jsonObject.getJSONObject("locked");
		locked.keySet()
		.parallelStream()
		.forEach(e -> {
			Balance b = balances.get(e);
			if(b != null){
				b.setLocked(locked.getBigDecimal(e));
			}
		});
		
		account.setBalances(balances);
		return account;
	}

	@Override
	public Depth getDepth(String currency) {
		String json = client.get(HOST + "/api2/1/orderBook/" + currencyStandizer.localize(currency));
		if(null == json){
			LOGGER.error("获取{}深度信息时{}服务器无数据返回。", currency, EXCHANGE_NAME);
			return null;
		}
	
		JSONObject jsonObject = JSON.parseObject(json);
		
		if(!"true".equals(jsonObject.get("result"))){
			LOGGER.error("获取{}深度信息时{}服务器返回错误信息: {}。", currency, EXCHANGE_NAME, json);
			return null;
		}
		
		Depth depth = new Depth();
		
		List<PriceQuotation> asks = new ArrayList<>();

		Function<String, List<PriceQuotation>> parseDepth = (dep) ->{
			List<PriceQuotation> pqs = new ArrayList<>();
			jsonObject.getJSONArray(dep).forEach(e->{

				if(e instanceof JSONArray){
					JSONArray a = (JSONArray)e;
					PriceQuotation pq = new PriceQuotation(a.getBigDecimal(0), a.getBigDecimal(1));
					pqs.add(pq);
				}
			});
			return pqs;
		};

		depth.setAsks(parseDepth.apply("asks"));
		depth.setBids(parseDepth.apply("bids"));
		depth.setExchange(EXCHANGE_NAME);
		depth.setCurrency(currency);
		depth.setTimestamp(System.currentTimeMillis());
				//- new Long(jsonObject.getString("elapsed").replace("ms", "")));
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
		Map<String, String> form = new HashMap<>();
		form.put("orderNumber", orderId);
		form.put("currencyPair", currency.toLowerCase());

		Map<String, String> header = buildHeader(form);

		String requestUrl = HOST + "/api2/1/private/getOrder";

		String json = client.post(requestUrl, header, form);
		if(null == json){
			LOGGER.error("获取订单(currencyPair={}, orderID={}时{}服务器无数据返回。)", currency, orderId, EXCHANGE_NAME);
			return null;
		}

		JSONObject jsonObject = JSON.parseObject(json);
		if(!true==jsonObject.getBoolean("result")){
			LOGGER.error("获取订单(currencyPair={}, orderID={}时{}服务器返回错误信息：{}。)", currency, orderId, EXCHANGE_NAME, json);
			return null;
		}

		JSONObject od = jsonObject.getJSONObject("order");

		Order order = new Order();
		order.setPrice(od.getBigDecimal("initialRate"));
		order.setQuantity(od.getBigDecimal("initialAmount"));
		order.setTradeQuantity(od.getBigDecimal("amount"));
		order.setOrderId(orderId);
		order.setCurrency(currency);
		order.setStatus(orderStatusStandizer.standardize(od.getString("status")));
		order.setTradeMoney(order.getTradeQuantity().multiply(od.getBigDecimal("rate")));
		order.setSide(od.getString("type").toUpperCase());
		return null;
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

		Map<String, String> params = new HashMap<>();
		params.put("currencyPair", currency);
		params.put("rate", price.toString());
		params.put("amount", quantity.toString());

		Map<String, String> header = buildHeader(params);

		String requestUrl = HOST;

		if("BUY".equals(side)){
			requestUrl += "/api2/1/private/buy";
		}else if("SELL".equals(side)){
			requestUrl += "/api2/1/private/sell";
		}else {
			LOGGER.error("不正确的交易方向：{}", side);
			return null;
		}

		String json = client.post(requestUrl, header, params);

		if(null == json){
			LOGGER.error("下单(side={}, currencyPair={}, price={}, quantity={})时{}服务器无数据返回。", side, currency, price, quantity, EXCHANGE_NAME);
			return null;
		}

		JSONObject jsonObject = JSON.parseObject(json);
		if(!true==jsonObject.getBoolean("result")){
			LOGGER.error("下单(side={}, currencyPair={}, price={}, quantity={})时{}服务器返回错误信息：{}。", side, currency, price, quantity, EXCHANGE_NAME, json);
			return null;
		}

		Order order = new Order();
		order.setStatus(OrderStatus.NEW);
		order.setOrderId(jsonObject.getString("orderNumber"));
		order.setCurrency(currency);
		order.setSide(side);
		order.setPrice(price);
		order.setQuantity(quantity);
		return order;
	}

	@Override
	public Order cancel(String currency, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlantformName() {
		return EXCHANGE_NAME;
	}

	/**
	 * 构建头部信息
	 * @param params 待发送到服务器的参数
	 * @return 头部信息
	 */
	private Map<String, String> buildHeader(Map<String, String> params) {
		Map<String, String> header = new HashMap<>();
		//header.put("Content-Type", "application/x-www-form-urlencoded");
		header.put("Key", key);
		String urlParamsStr = UrlParameterBuilder.MapToUrlParameter(params);
		header.put("Sign", EncryptionTools.HmacSHA512(secret, urlParamsStr == null ? "" : urlParamsStr));
		return header;
	}

}
