package vip.testops.account.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class AESUtil {

    public static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String IV_STRING = "testops.vip.curr";

    public static String decrypt(String encryptValue, Key key) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return aesDecryptByBytes(base64Decode(encryptValue), key);
    }

    public static String encrypt(String value, Key key) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        return base64Encode(aesEncryptToBytes(value, key));

    }

    public static Key generateKey(String aesKey) {
        return null;
    }

    private static String base64Encode(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }

    private static byte[] base64Decode(String base64Code) {
        if (base64Code == null) {
            return null;
        }
        return Base64.getDecoder().decode(base64Code);
    }

    private static AlgorithmParameters generateIV(String ivVal) throws IOException, NoSuchAlgorithmException {
        byte[] bytes = ivVal.getBytes(StandardCharsets.UTF_8);
        AlgorithmParameters paramters = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        paramters.init(bytes);
        return paramters;
    }

    private static byte[] aesEncryptToBytes(String content, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        AlgorithmParameters iv = generateIV(IV_STRING);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    private static String aesDecryptByBytes(byte[] encryptBytes, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        AlgorithmParameters iv = generateIV(IV_STRING);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] depcryBytes = cipher.doFinal(encryptBytes);
        return new String(depcryBytes);
    }
}
