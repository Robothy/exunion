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
     * @param secret
     * @param data
     * @return the cipher
     */
    public static String HmacSHA256(String secret, String data) {
        return HmanSHA(secret, data, "HmacSHA256");
    }

    /**
     * @param secret
     * @param data
     * @return the cipher
     */
    public static String HmacSHA512(String secret, String data) {
        return HmanSHA(secret, data, "HmacSHA512");
    }

    public static String HmacMd5(String secret, String data) {
        return HmanSHA(secret, data, "HmacMD5");
    }

    public static String SHA256(String data) {
        return SHA(data, "SHA-256");
    }

    public static String SHA1(String data) {
        return SHA(data, "SHA-1");
    }

    private static String SHA(String data, String algorithm) {
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

    private static String HmanSHA(String secret, String data, String algorithm) {


        String cipher = "";

        Mac sha256_HMAC = null;
        try {
            sha256_HMAC = Mac.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SecretKeySpec secret_key = null;
        try {
            secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), algorithm);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            sha256_HMAC.init(secret_key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            cipher = Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return cipher;

    }

    public static void main(String[] args) {
        //System.out.println(HmanSHA("6186ec9223b8f8e3fe28b5f5c831427ed99950a6", "accesskey=6d8f62fd-3086-46e3-a0ba-c66a929c24e2&method=getAccountInfo", "HmacMD5"));
        System.out.println(SHA1("48939bbc-8d49-402b-b731-adadf2ea9628"));
    }


}
