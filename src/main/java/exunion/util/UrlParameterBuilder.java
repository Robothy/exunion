package exunion.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UrlParameterBuilder {
	
	/**
	 * MapToUrlParameter convert a map object to URL parameters,
	 * key of Entry as parameter name and value as parameter value.
	 * for example, Map object with values ["k1":"vl", "k2", "v2"] will 
	 * by converted as "k1=v1&k2=v2" and return as a string.
	 * if map is empty or map is null, it will return an empty string.
	 * @param map
	 * @return
	 */
	public static String MapToUrlParameter(Map<String, String> map){
		if(null == map || map.isEmpty()){
			return "";
		}
		
		StringBuilder urlParameters = new StringBuilder();
		
		List<String> keyList = new ArrayList<String>(map.keySet());
		Collections.sort(keyList);

		for (String key : keyList){
			urlParameters.append(key)
			.append("=")
			.append(map.get(key))
			.append("&");
		}
		
		// remove the last '&'
		urlParameters.deleteCharAt(urlParameters.length() - 1);
		
		return urlParameters.toString();
	}
	
	/**
	 * buildUrlParamsWithHmacSHA256Sign build URL parameters with HmacSHA256 signature.
	 * the signature parameter at the end of all parameters.  
	 * @param secret a key of HmacSHA256
	 * @param signParamName the signature parameter name.
	 * @param param the data.
	 * @return cipher
	 */
	public static String buildUrlParamsWithHmacSHA256Sign(String secret, String signParamName, Map<String, String> params){
		StringBuilder urlParameters = new StringBuilder();
		String urlParams = MapToUrlParameter(params);
		String cipher = EncryptionTools.HmacSHA256(secret, urlParams);
		urlParameters.append(urlParams)
		.append("&").append(null==signParamName?"signature": signParamName).append("=").append(cipher);
		return urlParameters.toString();
	}
	
	public static String buildUrlParamsWithHmacSHA512Sign(String secret, String signParamName, Map<String, String> params){
		StringBuilder urlParameters = new StringBuilder();
		String urlParams = MapToUrlParameter(params);
		String cipher = EncryptionTools.HmacSHA512(secret, urlParams);
		urlParameters.append(urlParams)
		.append("&").append(null==signParamName?"signature": signParamName).append("=").append(cipher);
		return urlParameters.toString();
	}
	
	public static String buildUrlParamsWithHmacMD5Sign(String secret, String signParamName, Map<String, String> params){
		StringBuilder urlParameters = new StringBuilder();
		String urlParams = MapToUrlParameter(params);
		String cipher = EncryptionTools.HmacMd5(secret, urlParams);
		urlParameters.append(urlParams)
		.append("&").append(null==signParamName?"signature": signParamName).append("=").append(cipher);
		return urlParameters.toString();
	}
	
	/**
	 * 
	 * @param secret
	 * @param signParamName
	 * @param params
	 * @return
	 */
	public static String buildUrlParamsWithMD5Sign(String signParamName, Map<String, String> params){
		StringBuilder urlParameters = new StringBuilder();
		String urlParams = MapToUrlParameter(params);
		String cipher = EncryptionTools.Md532(urlParams);
		urlParameters.append(urlParams)
		.append("&").append(null==signParamName?"signature": signParamName).append("=").append(cipher);
		return urlParameters.toString();
	}
	
	/**
	 * 
	 * @param secret
	 * @param suffix 拼接在待加密字符后面的字符串
	 * @param signParamName
	 * @param params
	 * @return
	 */
	public static String buildUrlParamsWithMD532Sign(String suffix, String signParamName, Map<String, String> params){
		StringBuilder urlParameters = new StringBuilder();
		String urlParams = MapToUrlParameter(params);
		String cipher = EncryptionTools.Md532(urlParams + suffix);
		urlParameters.append(urlParams)
		.append("&").append(null==signParamName?"signature": signParamName).append("=").append(cipher);
		return urlParameters.toString();
	}
	
	
	
}
