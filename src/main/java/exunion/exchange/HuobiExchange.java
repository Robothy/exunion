package exunion.exchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HuobiExchange extends HuobiBase {

	private static final Logger logger = LogManager.getLogger(HuobiExchange.class);
	
	private static final String exchangeName = "huobi.pro";
	
	private static final String urlBase = "https://api.huobi.pro";
	
	private static final String hostName = "api.huobi.pro";

	private static final String wsUrl = "wss://api.huobi.pro/ws";

	public HuobiExchange(String key, String secret, Boolean needProxy) {
		super(key, secret, needProxy);
		setLogger(logger);
		setExchangeName(exchangeName);
		setUrlBase(urlBase);
		setHostName(hostName);
		setWsUrl(wsUrl);
	}
}
