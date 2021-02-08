package exunion.httpclient;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Client 包含一系列 http 客户端的操作，由于使用场景大部分为API接口调用，故返回值均为字符串类型。
 * @author robothy
 *
 */
public class Client {
	
	private static final Logger LOGGER = LogManager.getLogger(Client.class);
	
	private static final HttpClientBuilder clientBuilder = HttpClientBuilder.create();
	
	private HttpClient client = null;
	
	/**
	 * 构建一个新的 Clinet
	 */
	public Client(){
		this.client = clientBuilder.build();
	}
	
	/**
	 * get 向服务器发送一个 get 请求
	 * @param uri 超链接
	 * @return 服务器返回的结果
	 */
	public String get(String uri){
		return this.get(uri, null);
	}
	
	/**
	 * get 向服务器发送一个带HTTP头信息的 get 请求
	 * @param uri 超链接
	 * @param header 一个包含头部信息的 Map, 其中 Key 放名称， value放值。
	 * @return 服务器返回的结果
	 */
	public String get(String uri, Map<String, String> header){
		HttpRequestBase requestBase = new HttpGet(uri);
		return httpBaseOperate(requestBase, header);
	}
	
	/**
	 * post 向服务器发送一个 post 请求
	 * @param uri 请求资源的超链接
	 * @return 服务器返回的文本结果
	 */
	public String post(String uri){
		return this.post(uri, null);
	}
	
	/**
	 * post 向服务器发送一个 post 请求
	 * @param uri 请求的资源地址
	 * @param header 一个包含头部信息的 Map, 其中 Key 放名称， value放值。
	 * @return 服务器返回的文本结果
	 */
	public String post(String uri, Map<String, String> header){
		HttpRequestBase post = new HttpPost(uri);
		return this.httpBaseOperate(post, header);
	}
	
	private String httpBaseOperate(HttpRequestBase requestBase, Map<String, String> header){
		HttpResponse response = null;
		String responseBody = null;
		
		if (null != header){
			for( Entry<String, String> entry : header.entrySet()){
				requestBase.addHeader(entry.getKey(), entry.getValue());
			}
		}
		
		try {
			response = this.client.execute(requestBase);
		} catch (ClientProtocolException e) {
			LOGGER.error("向服务发送请求时出现客户端协议异常。", e);
		} catch (IOException e) {
			LOGGER.error("向服务器发送请求时出现IO异常。", e);
		}
		
		if(null != response){
			HttpEntity entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode >= 200 && statusCode < 300){
				try {
					responseBody = EntityUtils.toString(entity);
				} catch (ParseException e) {
					LOGGER.error("将服务器返回的实体转化为字符串时出现解析异常。", e);
				} catch (IOException e) {
					LOGGER.error("将服务器返回的实体转化为字符串时出现IO异常。", e);
				}
			}
		}
		return responseBody;
	}
	
}
