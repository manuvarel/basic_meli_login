package com.melilogin.webservices.utils;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.Base64Utils;

public class AuthUtil {

    private static final String AES = "AES";
    private static final String UTF_8 = "UTF-8";

    // We avoid storing the key in a String:
    private static byte[] keyBytes = { 69, 54, 68, 73, 65, 65, 65, 72, 51, 52, 83, 57, 54, 90, 79, 71 };
    private static SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, AES);

    public static String encryptShopId(String shopId, boolean encodeURL) throws IOException, GeneralSecurityException {
        Cipher cipherAes = Cipher.getInstance(AES);
        cipherAes.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipherAes.doFinal(shopId.getBytes(UTF_8));
        String strBase64 = Base64Utils.encodeToString(encryptedBytes);

        String encodedString = strBase64;
        if (encodeURL) {
            encodedString = URLEncoder.encode(strBase64, UTF_8);
        }
        return encodedString;
    }

    public static String decryptShopId(String encShopId, boolean decodeURL) throws IOException, GeneralSecurityException {
        String strBase64 = encShopId;
        if (decodeURL) {
            strBase64 = URLDecoder.decode(encShopId, UTF_8);
        }

        byte[] encBytes = Base64Utils.decodeFromString(strBase64);

        Cipher cipherAes = Cipher.getInstance(AES);
        cipherAes.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipherAes.doFinal(encBytes);
        String decryptedString = new String(decryptedBytes, UTF_8);

        return decryptedString;
    }
}