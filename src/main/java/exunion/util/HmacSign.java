package exunion.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacSign implements Sign {

    public enum HmacAlgorithm {

        HmacSHA1("HmacSHA1"),

        HmacSHA256("HmacSHA256"),

        HmacSHA384("HmacSHA384"),

        HmacSHA512("HmacSHA512"),

        HmacMD5("HmacMD5"),;
        /**
         * 算法名称
         */
        private String algorithmName;

        HmacAlgorithm(String algorithmName) {
            this.algorithmName = algorithmName;
        }

        private String getAlgorithmName() {
            return this.algorithmName;
        }
    }

    private static final Logger logger = LogManager.getLogger(HmacSign.class);

    private Mac mac;

    public HmacSign(HmacAlgorithm algorithm, String secret) {
        try {
            mac = Mac.getInstance(algorithm.getAlgorithmName());
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        }
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), algorithm.getAlgorithmName());
        try {
            mac.init(secret_key);
        } catch (InvalidKeyException e) {
            logger.error(e);
        }
    }

    @Override
    public synchronized byte[] hexSign(byte[] data) {
        return mac.doFinal(data);
    }
}