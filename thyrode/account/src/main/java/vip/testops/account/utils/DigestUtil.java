package vip.testops.account.utils;

import com.google.common.primitives.Bytes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {

    public static String digest(String cotent,String algorithm) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] digest = messageDigest.digest(cotent.getBytes(StandardCharsets.UTF_8));
        return bytes2string(digest);
    }

    public static String bytes2string(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int tmp=bytes[i]<0?bytes[i]+256:bytes[i];
            sb.append(StringUtil.lpadding(Integer.toHexString(tmp),2,'0'));
        }
        return sb.toString();
    }
}
