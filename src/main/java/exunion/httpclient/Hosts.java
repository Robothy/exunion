package exunion.httpclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 加载hosts文件，先尝试从应用启动目录查找hosts文件，若找到，则加载此文件
 * 若不能找到，则尝试从jar包里加载，若jar包里也不存在，则不加载hosts映射。
 * @author robothy
 *
 */
class Hosts {
	
	private static String hostsFileName = "hosts";
	
	private static final Logger logger = LogManager.getLogger(Hosts.class);
	
	private static final Map<String, String> hostsMapping = new HashMap<String, String>();
	
	static {
		
		File file = new File(hostsFileName);
		if(file.exists()){
			BufferedReader reader;
			try {
				logger.info("加载 hosts ...");
				reader = new BufferedReader(new FileReader(file));
				String line = null;
				while((line = reader.readLine()) != null){
					handleHostsTextLine(line);
				}
			} catch (FileNotFoundException e) {
				logger.error("读取 hosts 文件 " + file.getAbsolutePath() + " 时出现文件无法找到异常。", e);
			} catch (IOException e){
				logger.error("读取 hosts 文件 " + file.getAbsolutePath() + " 时出现IO异常。", e);
			}
		}else{
			ClassLoader classLoader = ProxyPool.class.getClassLoader();
			
			InputStream proxyFileStream = classLoader.getResourceAsStream(hostsFileName);
			
			if(null == proxyFileStream){
				logger.info("未能找到 hosts 文件 {}", hostsFileName);
			}else{
				logger.info("加载 hosts ...");
				Scanner scanner = null;
				scanner = new Scanner(proxyFileStream);
				while(scanner.hasNextLine()){
					String line = scanner.nextLine().trim();
					handleHostsTextLine(line);				
				}
				scanner.close();
			}			
		}
	}
	
	private static void handleHostsTextLine(String line){
		if("".equals(line) || line.startsWith("#")){
			return;
		}
		String[] arr = line.split("/");
		String domain = arr[0];
		String ip = arr[1];
		hostsMapping.put(domain, ip);
	}
	
	/**
	 * 根据域名获取应用hosts文件中配置的IP地址
	 * @param domain 域名
	 * @return 与域名对应的IP地址
	 */
	public static String getIpByDomain(String domain){
		return hostsMapping.get(domain);
	}
	
}
