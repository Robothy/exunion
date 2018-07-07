package exunion.exchange;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import exunion.metaobjects.Account;
import exunion.metaobjects.Depth;
import exunion.metaobjects.Order;
import exunion.metaobjects.Ticker;
import exunion.metaobjects.Depth.PriceQuotation;
import exunion.standardize.Standardizable;
import exunion.util.UrlParameterBuilder;

public class LBankExchange extends AExchange {

	private static final Logger logger = LogManager.getLogger(LBankExchange.class);
	
	private static final String exchangeName = "lbank.info";
	
	private static final String baseUrl = "http://api.lbank.info";
	
	private static final Map<String, String> header = new ConcurrentHashMap<>();
	
	static{
		header.put("contentType", "application/x-www-form-urlencoded");
	}
	
	private static final Standardizable<String, String> currencyStandizer = new Standardizable<String, String>(){

		@Override
		public String standardize(String l) {
			return l.toUpperCase();
		}

		@Override
		public String localize(String s) {
			return s.toLowerCase();
		}
		
	};
	
	LBankExchange(String key, String secret, Boolean needProxy){
		super(key, secret, needProxy);
	}
	
	@Override
	public Account getAccount() {
		
		return null;
	}

	@Override
	public Depth getDepth(String currency) {
		Map<String, String> params = new HashMap<>();
		params.put("symbol", currencyStandizer.localize(currency));
		params.put("size", "60");
		params.put("merge", "0");
		String json = client.get(baseUrl + "/v1/depth.do?" + UrlParameterBuilder.MapToUrlParameter(params) , header);
		if(null == json){
			logger.error("获取{}深度信息失败，{}服务器无数据返回。", currency, exchangeName);
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		
		Function<String, List<PriceQuotation>> parser = e -> {
			return jsonObject.getJSONArray(e)
			.parallelStream()
			.map(p ->{
				PriceQuotation pq = null;
				if(p instanceof JSONArray){
					JSONArray a = (JSONArray)p;
					pq = new PriceQuotation(a.getBigDecimal(0), a.getBigDecimal(1));
				}
				return pq;
			}).collect(Collectors.toList());
		};
		
		Depth depth = new Depth();
		depth.setAsks(parser.apply("asks"));
		depth.setBids(parser.apply("bids"));
		depth.setExchange(exchangeName);
		depth.setCurrency(currency);
		depth.setTimestamp(jsonObject.getLong("timestamp"));
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
		return exchangeName;
	}

}
