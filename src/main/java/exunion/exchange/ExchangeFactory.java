package exunion.exchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExchangeFactory {
	
	private static final Logger LOGGER = LogManager.getLogger(ExchangeFactory.class);
	
	/**
	 * 获取一个交易所实例
	 * @param plantform 交易所名称，例如 exx.com, binance.com
	 * @return 一个新的交易所实例
	 */
	public static Exchange newInstance(String plantform){
		return newInstance(plantform, false);
	}
	
	/**
	 * 获取一个交易所实例
	 * @param plantform 平台名称，例如 exx.com, binance.com
	 * @param needProxy 是否需要走代理标志
	 * @return 一个新的交易所实例
	 */
	public static Exchange newInstance(String plantform, Boolean needProxy){
		return newInstance(plantform, null, null, needProxy);
	}
	
	/**
	 * 获取一个交易所实例
	 * @param plantform 交易所名称，例如 exx.com, binance.com
	 * @param key 交易所提供的key
	 * @param secret 交易所提供的 secret
	 * @param needProxy 是否需要走代理标志
	 * @return 一个新的交易所实例
	 */
	public static Exchange newInstance(String plantform, 
			String key, String secret, Boolean needProxy){
		if (plantform.equals("exx.com")){
			return new ExxExchange(key, secret, needProxy);
		}else if(plantform.equals("binance.com")){
			return new BinanceExchange();
		}else if (plantform.equals("zb.com")) {
			return new ZbExchange(key, secret, needProxy);
		}else if(plantform.equals("bit-z.com") || plantform.equals("bit-z.pro")){
			return new BitZExchange(key, secret, needProxy);
		}else if(plantform.equals("gate.io")){
			return new GateExchange(key, secret, needProxy);
		}else if(plantform.equals("huobi.pro")){
			return new HuobiExchange(key, secret, needProxy);
		}
		else {
			LOGGER.error("未找到 " + plantform + " 的交易所实例。");
			return null;
		}
	}
}
