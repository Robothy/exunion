package exunion.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class EncryptionTools {
	
	/**
	 * 
	 * @param secret
	 * @param data
	 * @return the cipher
	 */
	public static String HmacSHA256(String secret, String data){
		return HmanSHA(secret, data, "HmacSHA256");
	}
	
	public static byte[] HmacSHA256Hex(String secret, String data){
		return HmanSHAHex(secret, data, "HmacSHA256");
	}
	
	/**
	 * 
	 * @param secret
	 * @param data
	 * @return the cipher
	 */
	public static String HmacSHA512(String secret, String data){
		return HmanSHA(secret, data, "HmacSHA512");
	}
	
	public static String HmacMd5(String secret, String data){
		return HmanSHA(secret, data, "HmacMD5");
	}
	
	public static String SHA256(String data){
		return SHA(data, "SHA-256");
    }
	
	public static String SHA1(String data){
		return SHA(data, "SHA-1");
	}
	
	public static String Md532(String data){
		
		MessageDigest messageDigest = null;    
		try    
		{    
			messageDigest = MessageDigest.getInstance("MD5");    
			messageDigest.reset();    
			messageDigest.update(data.getBytes("UTF-8"));    
		} catch (NoSuchAlgorithmException e)    
		{    
			System.out.println("NoSuchAlgorithmException caught!");    
			System.exit(-1);    
		} catch (UnsupportedEncodingException e)    
		{    
			e.printStackTrace();    
		}    
		
		byte[] byteArray = messageDigest.digest();    
		
		StringBuffer md5StrBuff = new StringBuffer();    
		
		for (int i = 0; i < byteArray.length; i++)    
		{    
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)    
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));    
			else    
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));    
		}    
		return md5StrBuff.toString();    
	}
	
	private static String SHA(String data, String algorithm){
		MessageDigest messageDigest;
		String encdeStr = "";
		try {
			messageDigest = MessageDigest.getInstance(algorithm);
			byte[] hash = messageDigest.digest(data.getBytes("UTF-8"));
			encdeStr = Hex.encodeHexString(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encdeStr;
	}
	
	private static String HmanSHA(String secret, String data, String algorithm){
		return Hex.encodeHexString(HmanSHAHex(secret, data, algorithm));
	}
	
	private static byte[] HmanSHAHex(String secret, String data, String algorithm){
		
		byte[] cipher = null; 
		
		Mac sha256_HMAC = null;
		try {
			sha256_HMAC = Mac.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		SecretKeySpec secret_key = null;
		secret_key = new SecretKeySpec(secret.getBytes(), algorithm);
		try {
			sha256_HMAC.init(secret_key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		try {
			cipher = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return cipher;
	
	}
	
	
	
	public static void main(String[] args){
		//System.out.println(HmanSHA("6186ec9223b8f8e3fe28b5f5c831427ed99950a6", "accesskey=6d8f62fd-3086-46e3-a0ba-c66a929c24e2&method=getAccountInfo", "HmacMD5"));
		//System.out.println(SHA1("48939bbc-8d49-402b-b731-adadf2ea9628"));
		System.out.println(HmacMd5("wZYHnNS396a4F5OVviUHG3dBgGK1zPTyFRwnRYsJIYy40CerZKgsN4ECrd8KHDX6", "api_key=48e5e01ca50cc6ac722b6502d3cd938e&nonce=559120&timestamp=1520347559wZYHnNS396a4F5OVviUHG3dBgGK1zPTyFRwnRYsJIYy40CerZKgsN4ECrd8KHDX6"));
		System.out.println(HmacMd5("wZYHnNS396a4F5OVviUHG3dBgGK1zPTyFRwnRYsJIYy40CerZKgsN4ECrd8KHDX6", "api_key=48e5e01ca50cc6ac722b6502d3cd938e&nonce=559120&timestamp=1520347559"));
		System.out.println(HmacMd5("wZYHnNS396a4F5OVviUHG3dBgGK1zPTyFRwnRYsJIYy40CerZKgsN4ECrd8KHDX6", "api_key=48e5e01ca50cc6ac722b6502d3cd938e&nonce=559120&timestamp=1520347559wZYHnNS396a4F5OVviUHG3dBgGK1zPTyFRwnRYsJIYy40CerZKgsN4ECrd8KHDX6"));
		System.out.println(HmacMd5("wZYHnNS396a4F5OVviUHG3dBgGK1zPTyFRwnRYsJIYy40CerZKgsN4ECrd8KHDX6", "api_key=48e5e01ca50cc6ac722b6502d3cd938e&nonce=559120&timestamp=1520347559"));
		System.out.println(HmacMd5("wZYHnNS396a4F5OVviUHG3dBgGK1zPTyFRwnRYsJIYy40CerZKgsN4ECrd8KHDX6", "api_key=48e5e01ca50cc6ac722b6502d3cd938e&nonce=559120&timestamp=1520347559wZYHnNS396a4F5OVviUHG3dBgGK1zPTyFRwnRYsJIYy40CerZKgsN4ECrd8KHDX6"));
		System.out.println(HmacMd5("wZYHnNS396a4F5OVviUHG3dBgGK1zPTyFRwnRYsJIYy40CerZKgsN4ECrd8KHDX6", "api_key=48e5e01ca50cc6ac722b6502d3cd938e&nonce=559120&timestamp=1520347559"));
	}
	
	
}
