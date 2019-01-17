package com.zlj.forum.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author tori
 * 2018/7/30 下午1:01
 */
public class Blowfish {

    private static final String SECRET_KEY = "HUIJIE_BLOWFISH";

    private static final SecretKeySpec KEY_SPEC = new SecretKeySpec(SECRET_KEY.getBytes(), "Blowfish");

    /**
     * 将输入字符串通过Blowfish加密
     * @param data 要加密的字符串
     * @return base64后的加密串
     */
    public static String encode(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, KEY_SPEC);
        return Base64Util.getBase64().encodeAsString(cipher.doFinal(data.getBytes("UTF-8")));
    }

    /**
     * 将Blowfish后的加密串解密
     * @param dataBase64 Blowfish加密后的base64串
     * @return 解密后的字符串
     */
    public static String decode(String dataBase64) throws Exception {
        byte[] encodeBytes = Base64Util.getBase64().decode(dataBase64);
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, KEY_SPEC);
        return new String(cipher.doFinal(encodeBytes), "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        try {
            String before = "Bc888888";
            String after = Blowfish.encode(before);
            System.out.println(before + " -> " + after);
            String decodeResult = Blowfish.decode(after);
            System.out.println(after + " => " + decodeResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

