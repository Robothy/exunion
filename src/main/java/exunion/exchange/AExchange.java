package exunion.exchange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import exunion.httpclient.Client;
import exunion.httpclient.ProxyPool;
import exunion.httpclient.ProxyPool.ProxyServer;

public abstract class AExchange implements Exchange {
	
	protected Client client = null;
	
	private static final Logger logger = LogManager.getLogger(AExchange.class);

	protected static final AExchange exchange = null;
	
	protected String key = "";
	
	protected String secret = "";
	
	public AExchange(Boolean needProxy){

		if(needProxy){
			if(ProxyPool.hasNext()){
				ProxyServer proxyServer = ProxyPool.next();			
				client = new Client(proxyServer.getIpAddress(), proxyServer.getPost());
			}else{
				logger.warn("获取代理服务器信息失败，没有代理服务器的配置信息。");
				client = new Client();
			}			
		}else{
			client = new Client();
		}
	}
	
	public AExchange setKey(String key){
		this.key = key;
		return this;
	}
	
	public AExchange setSecret(String secret){
		this.secret = secret;
		return this;
	}
}
