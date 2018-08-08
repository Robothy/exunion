package exunion.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;

public interface Sign {

    /**
     * 生成签名字节数组
     * @param data 待签名的字符数组
     * @return 生成的签名字符数组
     */
    byte[]  hexSign(byte[] data);


    /**
     * 生成字符串签名
     * @param data 待签名的字符串
     * @return 签名字符串
     */
    default String strSign(String data) {
        return new String(hexSign(data));
    }

    /**
     * 生成字符串签名
     * @param data 待签名的字节数组
     * @return 签名字符串
     */
    default String strSign(byte[] data) {
        return new String(hexSign(data));
    }

    /**
     * 生成签名字节数组
     * @param data 待签名的字符串
     * @return 签名字符数组
     */
    default byte[] hexSign(String data) {
        try {
            return hexSign(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Logger logger = LogManager.getLogger(Sign.class);
            logger.error(e);
        }
        return null;
    }
}
