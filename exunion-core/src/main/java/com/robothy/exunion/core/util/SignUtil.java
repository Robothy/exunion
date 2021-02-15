package com.robothy.exunion.core.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SignUtil {

    public static final Map<String, Mac> CACHE = new HashMap<>();

    public static final Map<String, SpinLock> LOCKS = new HashMap<>();

    public static String sign(String algorithm, String secret, String message){
        String key = algorithm + secret;
        if(!CACHE.containsKey(key)){
            synchronized (SignUtil.class){
                if(!CACHE.containsKey(key)){
                    try {
                        Mac mac = Mac.getInstance(algorithm);
                        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), algorithm);
                        mac.init(secretKeySpec);
                        CACHE.put(key, mac);
                        LOCKS.put(key, new SpinLock());
                    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                        // The application should stop if invalid algorithm or secret passed to this method.
                        throw new IllegalArgumentException(e.getCause());
                    }
                }
            }
        }

        LOCKS.get(key).lock();
        try {
            return Hex.encodeHexString(CACHE.get(key).doFinal(message.getBytes(StandardCharsets.UTF_8)));
        }finally {
            LOCKS.get(key).unlock();
        }
    }

}

class SpinLock {

    private AtomicInteger state;

    SpinLock(){
        this.state = new AtomicInteger();
    }

    void lock(){
        // spin until successfully set the state.
        while (!state.compareAndSet(0, 1));
    }

    void unlock(){
        state.set(0);
    }
}