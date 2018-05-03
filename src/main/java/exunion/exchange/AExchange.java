package exunion.exchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import exunion.httpclient.Client;
import exunion.httpclient.ProxyPool;
import exunion.httpclient.ProxyPool.ProxyServer;

public abstract class AExchange implements Exchange {
	
	protected Client client = null;
	
	private static final Logger logger = LogManager.getLogger(AExchange.class);
	
	protected static String KEY = "";
	
	protected static String SECRET = "";
	
	public AExchange(Boolean needProxy){

		if(needProxy){
			if(ProxyPool.hasNext()){
				ProxyServer proxyServer = ProxyPool.next();			
				client = new Client(proxyServer.getIpAddress(), proxyServer.getPost());
			}else{
				logger.warn("没有代理服务器的配置信息。");
				client = new Client();
			}			
		}else{
			client = new Client();
		}
	
	}
	
	public static void setKey(String key){
		KEY = key;
	}
	
	public static void setSecret(String secret){
		SECRET = secret;
	}
}
