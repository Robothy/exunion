package exunion.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Sign implements Sign {

    private MessageDigest messageDigest;

    public Md5Sign(){
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
        }
    }

    @Override
    public byte[] hexSign(byte[] data) {
        messageDigest.update(data);
        byte[] byteArray = messageDigest.digest();
        StringBuilder md5StrBuff = new StringBuilder();
        for (byte aByteArray : byteArray) {
            if (Integer.toHexString(0xFF & aByteArray).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & aByteArray));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
        }
        return md5StrBuff.toString().getBytes();
    }
}
