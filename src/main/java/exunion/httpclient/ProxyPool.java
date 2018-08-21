package exunion.httpclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * 代理服务器池
 * @author robothy
 *
 */
public class ProxyPool {
	
	private ProxyPool(){}
	
	// 代理服务器配置文件名称
	private static String proxyFileName = "httpProxy.properties";
	
	private static final Logger logger = LogManager.getLogger(ProxyPool.class);
	
	// 配置文件中的代理服务器
	private static List<ProxyServer> proxies = new ArrayList<>();

	// 当前索引号
	private static int idx = 0;
	
	//静态初始化块，加载配置的代理服务器池
	static{

		File file = new File(proxyFileName);

		InputStream proxyFileStream = null;

		if(file.exists()){
			try {
				proxyFileStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else {
			ClassLoader classLoader = ProxyPool.class.getClassLoader();
			classLoader.getResourceAsStream(proxyFileName);
		}


		if(null == proxyFileStream){
			logger.info("加载代理服务器配置时未能找到配置文件 {}", proxyFileName);
		}else{
			logger.info("加载代理服务器配置。");
			Scanner scanner = null;
			scanner = new Scanner(proxyFileStream);
			while(scanner.hasNextLine()){
				String line = scanner.nextLine().trim();
				if("".equals(line) || line.startsWith("#")){
					continue;
				}
				String[] arr = line.split(":");
				String ip = arr[0];
				Integer port = new Integer(arr[1]);
				ProxyServer proxyServer = new ProxyServer(ip, port);
				proxies.add(proxyServer);
			}
			scanner.close();
		}
	}
	
	public static List<ProxyServer> getProxyPool(){
		return proxies;
	}
	
	/**
	 * 判断是否含有代理服务器，只要配置了代理服务器就会返回 true
	 * @return
	 */
	public static Boolean hasNext(){
		return proxies.size() > 0;
	}
	
	/**
	 * 轮询获取代理服务器
	 * @return
	 */
	public static ProxyServer next(){
		if(idx == proxies.size()){
			idx = 0;
		}
		return proxies.get(idx++);
	}
	
	/**
	 * 表示代理服务器的静态内部类
	 * @author robothy
	 *
	 */
	public static class ProxyServer{
		
		private String ipAddress;
		
		private Integer port;
		
		public ProxyServer(String ipAddress, Integer port){
			this.ipAddress = ipAddress;
			this.port = port;
		}
		
		public String getIpAddress(){
			return ipAddress;
		}
		
		public Integer getPort(){
			return port;
		}
		
		public String toString(){
			return ipAddress + ":" + port.toString();
		}
		
	}
	
}
