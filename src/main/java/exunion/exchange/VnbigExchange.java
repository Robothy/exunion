package exunion.exchange;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
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

public class VnbigExchange extends AExchange {

	private static final Logger logger = LogManager.getLogger(VnbigExchange.class);
	
	private static final String exchangeName = "vnbig.com";
	
	private static final String baseUrl = "http://api.vnbig.com";
	
	private static final Standardizable<String, String> currencyStandizer = new Standardizable<String, String>() {
		@Override
		public String standardize(String l) {
			String[] x = l.split("_");
			return x[1].toUpperCase() + "_" + x[0].toUpperCase();
		}
		
		@Override
		public String localize(String s) {
			String[] x = s.split("_");
			return x[1].toLowerCase() + "_" + x[0].toLowerCase();
		}
	};
	
	private Standardizable<String, String> orderSideStandizer = new Standardizable<String, String>() {

		@Override
		public String standardize(String l) {
			return l.toUpperCase();
		}

		@Override
		public String localize(String s) {
			return s.toLowerCase();
		}
	};
	
	private Standardizable<String, String> orderStatusStandizer = new Standardizable<String, String>() {

		@Override
		public String standardize(String l) {
			if("_5".equals(l)){
				return OrderStatus.CANCELED;
			}else if("_0".equals(l)){
				return OrderStatus.NEW;
			}else if("_2".equals(l)){
				return OrderStatus.PARTIALLY_FILLED;
			}else if("_3".equals(l)){
				return OrderStatus.FILLED;
			}
			return null;
		}

		@Override
		public String localize(String s) {
			
			return null;
		}
	};
	
	VnbigExchange(String key, String secret, Boolean needProxy) {
		super(key, secret, needProxy);
	}
	
	
	@Override
	public Account getAccount() {
		
		Map<String, String> params = new HashMap<>();
		params.put("api_key", key);
		Map<String, String> form = new HashMap<>();
		form.put("api_key", key);
		form.put("sign", EncryptionTools.Md532(UrlParameterBuilder.MapToUrlParameter(form) + "&secret_key=" + secret));
		String json = client.post(baseUrl + "/userinfo", null, form);
		
		if(null == json){
			logger.error("从{}服务器获取账户信息时失败，服务器无数据返回。", exchangeName);
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		
		if(json.contains("errorCode")){
			logger.error("从{}服务器获取账户信息时出错，服务器返回错误信息:{}", exchangeName, errorCode.get(jsonObject.get("errorCode")));
			return null;
		}
		
		Account account = new Account();
		JSONObject free = jsonObject.getJSONObject("free"); 
		free.keySet().stream().forEach(e ->{
			Balance balance = new Balance();
			balance.setAsset(e);
			balance.setFree(new BigDecimal(free.getString(e)));
			account.putBalance(balance);
		});
		
		JSONObject locked = jsonObject.getJSONObject("freezed");
		locked.keySet().stream().forEach(e -> {
			account.getBalance(e).setLocked(new BigDecimal(locked.getString(e)));
		});
		
		return account;
	}

	@Override
	public Depth getDepth(String currency) {
		String json = client.get(baseUrl + "/depth/" + currencyStandizer.localize(currency));
		if(null == json){
			logger.error("从{}服务器获取{}深度信息时失败，服务器无数据返回。", exchangeName, currency);
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		
		if(json.contains("errorCode")){
			logger.error("从{}服务器获取{}深度信息失败，服务器返回错误信息:{}", 
					exchangeName, currency, errorCode.get(jsonObject.getString("errorCode")));
			return null;
		}
		
		Function<String, List<PriceQuotation>> parser = d -> {
			return jsonObject.getJSONArray(d)
			.parallelStream()
			.map(e -> {
				PriceQuotation pq = new PriceQuotation();
				if(e instanceof JSONObject){
					JSONObject o = (JSONObject) e;
					pq.setPrice(new BigDecimal(o.getString("price")));
					pq.setQuantity(new BigDecimal(o.getString("volume")));
				}
				return pq;
			})
			.collect(Collectors.toList());
		};
		
		Depth depth = new Depth();
		depth.setTimestamp(jsonObject.getLong("timeStamp"));
		depth.setAsks(parser.apply("asks"));
		depth.setBids(parser.apply("bids"));
		depth.setExchange(exchangeName);
		depth.setCurrency(currency);
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
		params.put("api_key", key);
		params.put("symbol", currencyStandizer.localize(currency));
		params.put("order_id", orderId);
		params.put("sign", EncryptionTools.Md532(UrlParameterBuilder.MapToUrlParameter(params) + "&secret_key=" + secret));
		String json = client.post(baseUrl + "/orderinfo", null, params);
		
		if(null == json){
			logger.error("获取订单orderId={}信息时失败，{}服务器无数据返回。", orderId, exchangeName);
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		
		if(json.contains("errorCode")){
			logger.error("获取订单orderId={}信息时失败，{}服务器返回错误信息:{}。", orderId, exchange, errorCode.get(jsonObject.getString("errorCode")));
			return null;
		}
		
		jsonObject = jsonObject.getJSONArray("orders").getJSONObject(0);
		
		Order order = new Order();
		order.setCreateDate(jsonObject.getLong("create_date"));
		order.setCurrency(currency);
		order.setOrderId(orderId);
		order.setPrice(jsonObject.getBigDecimal("price"));
		order.setQuantity(jsonObject.getBigDecimal("volume"));
		order.setSide(orderSideStandizer.standardize(jsonObject.getString("type")));
		order.setStatus(orderStatusStandizer.standardize(jsonObject.getString("status")));
		order.setTradeQuantity(jsonObject.getBigDecimal("deal_volume"));
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
		Map<String, String> params = new HashMap<>();
		params.put("api_key", key);
		params.put("symbol", currencyStandizer.localize(currency));
		params.put("type", orderSideStandizer.localize(side));
		params.put("price", price.toPlainString());
		params.put("volume", quantity.toPlainString());
		
		params.put("sign", EncryptionTools.Md532(UrlParameterBuilder.MapToUrlParameter(params) + "&secret_key=" + secret));
		
		String json = client.post(baseUrl + "/trade", null, params);
		if(null == json){
			logger.error("委托下单(currency={}, price={}, quantity={})失败，{}服务器无数据返回。", currency, price, quantity, exchangeName);
			return null;
		}
		
		JSONObject jsonObject = JSON.parseObject(json);
		
		if(json.contains("errorCode")){
			logger.error("委托下单(currency={}, price={}, quantity={})失败，{}服务器返回错误信息：{}。", currency, price, quantity, exchangeName, errorCode.get(jsonObject.getString("errorCode")));
			return null;
		}
		
		if(true != jsonObject.getBoolean("result")){
			logger.error("委托下单(currency={}, price={}, quantity={})失败，{}服务器返回错误信息：{}。", currency, price, quantity, exchangeName, json);
			return null;
		}
		
		Order order = new Order();
		order.setCurrency(currency);
		order.setSide(side);
		order.setQuantity(quantity);
		order.setPrice(price);
		order.setOrderId(jsonObject.getString("order_id"));
		
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
	
	
	// 定义错误代码
	private static Map<String, String> errorCode = new HashMap<>();
	
	static{
		errorCode.put("10000", "Api_key不能为空");
		errorCode.put("10001", "签名不能为空");
		errorCode.put("10002", "Api_key已经过期");
		errorCode.put("10003", "非开放API");
		errorCode.put("10004", "API权限不对");
		errorCode.put("10005", "交易对不能为空");
		errorCode.put("10006", "Api_key不存在");
		errorCode.put("10007", "签名不匹配");
		errorCode.put("10008", "买卖类型不能为空");
		errorCode.put("10009", "委托价不能为空");
		errorCode.put("10010", "委托价格必须大于0");
		errorCode.put("10011", "委托数量不能为空");
		errorCode.put("10012", "委托数量必须大于0");
		errorCode.put("10013", "委托金额不能为空");
		errorCode.put("10014", "委托金额必须大于0");
		errorCode.put("10015", "下单失败");
		errorCode.put("10016", "订单ID不能为空");
		errorCode.put("10017", "最多允许撤消3个订单");
		errorCode.put("10018", "订单不存在");
		errorCode.put("10019", "撤销订单失败");
		errorCode.put("10020", "订单ID太长");
		errorCode.put("10021", "订单ID类型错误");
		errorCode.put("10022", "买卖类型错误");
		errorCode.put("10023", "返回的买卖类型错误");
		errorCode.put("10024", "获取订单详情失败");
		errorCode.put("10025", "获取多个委托失败");
		errorCode.put("10026", "用户信息不存在");
		errorCode.put("10027", "交易对不存在");
		errorCode.put("10028", "周期不能为空");
		errorCode.put("10029", "没有K线类型");
		errorCode.put("10030", "获取历史成交失败");
		errorCode.put("10031", "可用额度不足");
		errorCode.put("10032", "用户请求频率过快，超过了该接口允许的限额");
		errorCode.put("10033", "API暂停对外提供服务");
		errorCode.put("10034", "语言类型不存在或不支持");
		errorCode.put("10035", "排序类型不存在");
		errorCode.put("10036", "此IP不在访问白名单内");
	}

}
