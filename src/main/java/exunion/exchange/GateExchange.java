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
import com.alibaba.fastjson.JSONObject;

import exunion.metaobjects.Account;
import exunion.metaobjects.Depth;
import exunion.metaobjects.Order;
import exunion.metaobjects.Ticker;
import exunion.metaobjects.Account.Balance;
import exunion.metaobjects.Depth.PriceQuotation;
import exunion.standardize.Standardizable;
import exunion.util.EncryptionTools;

public class GateExchange extends AExchange {

	private static final String EXCHANGE_NAME = "gate.io";
	
	private static final String HOST = "https://data.gate.io";
	
	private static final Logger LOGGER = LogManager.getLogger(GateExchange.class);
	
	private static final Standardizable<String, String> currencyStandizer = new Standardizable<String, String>() {

		public String standardize(String l) {
			return l.toUpperCase();
		}

		public String localize(String s) {
			return s.toLowerCase();
		}
	};
	
	public GateExchange(String key, String secret, Boolean needProxy) {
		super(key, secret, needProxy);
	}

	@Override
	public Account getAccount() {
		
		Map<String, String> header = new HashMap<>();
		header.put("Content-Type", "application/x-www-form-urlencoded");
		header.put("Key", key);
		header.put("Sign", EncryptionTools.HmacSHA512(secret, ""));
		String json = client.post("https://api.gateio.io/api2/1/private/balances", header);
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
		Map<String, Balance> balances = new HashMap<>();
		JSONObject available = jsonObject.getJSONObject("available");
		available.keySet()
		.parallelStream()
		.forEach(e ->{
			Balance balance = new Balance();
			balance.setAsset(e);
			balance.setFree(available.getBigDecimal(e));
			balances.put(e, new Balance());
		});
		
		JSONObject locked = jsonObject.getJSONObject("locked");
		locked.keySet()
		.parallelStream()
		.forEach(e -> balances.get(e).setLocked(locked.getBigDecimal(e)));
		
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
		JSONArray asksArray = jsonObject.getJSONArray("asks");
		JSONArray bidsArray = jsonObject.getJSONArray("bids");
		
		List<PriceQuotation> asks = new ArrayList<>();
		for(int i=0; i<asksArray.size(); i++){
			JSONArray quotation = asksArray.getJSONArray(i);
			BigDecimal price = new BigDecimal(quotation.get(0).toString());
			BigDecimal quantity = new BigDecimal(quotation.get(1).toString());
			Depth.PriceQuotation priceQuotation = new PriceQuotation(price, quantity);
			asks.add(priceQuotation);
		}
		depth.setAsks(asks);
		
		List<PriceQuotation> bids = new ArrayList<>();
		for(int i=0; i<bidsArray.size(); i++){
			JSONArray quotation = bidsArray.getJSONArray(i);
			BigDecimal price = new BigDecimal(quotation.get(0).toString());
			BigDecimal quantity = new BigDecimal(quotation.get(1).toString());
			Depth.PriceQuotation priceQuotation = new PriceQuotation(price, quantity);
			bids.add(priceQuotation);
		}
		depth.setBids(bids);
		depth.setExchange(EXCHANGE_NAME);
		depth.setCurrency(currency);
		depth.setTimestamp((System.currentTimeMillis() - new Long(jsonObject.getString("elapsed").replace("ms", "")))/1000);
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order cancel(String currency, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlantformName() {
		// TODO Auto-generated method stub
		return EXCHANGE_NAME;
	}

}
