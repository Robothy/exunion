package exunion.exchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HadaxExchange extends HuobiBase {

	private static final Logger logger = LogManager.getLogger(HadaxExchange.class);
	
	private static final String exchangeName = "hadax.com";
	
	private static final String urlBase = "https://api.hadax.com";
	
	private static final String hostName = "api.hadax.com";

    private static final String wsUrl = "wss://api.hadax.pro/ws";
	
	public HadaxExchange(String key, String secret, Boolean needProxy) {
		super(key, secret, needProxy);
		setLogger(logger);
		setExchangeName(exchangeName);
		setUrlBase(urlBase);
		setHostName(hostName);
		setWsUrl(wsUrl);
	}
}
