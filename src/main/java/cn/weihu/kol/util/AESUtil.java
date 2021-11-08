package cn.weihu.kol.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
@Slf4j
public class AESUtil {

    /**
     * 加密
     *
     * @param sSrc
     * @param encodingFormat
     * @param sKey
     * @param iv
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String encodingFormat, String sKey, String iv) throws Exception {
        Cipher          cipher          = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[]          raw             = sKey.getBytes();
        SecretKeySpec   skeySpec        = new SecretKeySpec(raw, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
    }

    /**
     * 解密
     *
     * @param sSrc
     * @param encodingFormat
     * @param sKey
     * @param iv
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String encodingFormat, String sKey, String iv) throws Exception {
        try {
            byte[]          raw             = sKey.getBytes(StandardCharsets.US_ASCII);
            SecretKeySpec   skeySpec        = new SecretKeySpec(raw, "AES");
            Cipher          cipher          = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
            byte[] original   = cipher.doFinal(encrypted1);
            return new String(original, encodingFormat);
        } catch(Exception ex) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        // 需要加密的字串
        String cSrc = "123456";
        System.out.println("加密前的字串是：" + cSrc);
        // 加密
        String enString = AESUtil.encrypt(cSrc, "utf-8", "sKey", "ivParameter");
        System.out.println("加密后的字串是：" + enString);

        System.out.println("1jdzWuniG6UMtoa3T6uNLA==".equals(enString));

        // 解密
        String DeString = AESUtil.decrypt(enString, "utf-8", "sKey", "ivParameter");
        System.out.println("解密后的字串是：" + DeString);
    }

    // AES CBC 加密需要满足byte数组的长度是16的整数倍,如果不满足,则用空byte填充
    public static byte[] fillAsk(String askJson) {
        int len = askJson.getBytes().length;
        int fillLen = 0;
        // System.out.println(askJson.getBytes().length);
        // System.out.println(askJson.getBytes().length % 16);
        if (len % 16 != 0) {
            fillLen = len + 16 - len % 16;
        } else {
            return askJson.getBytes();
        }
        byte[] fillByte = new byte[fillLen];
        // System.out.println(fillLen);
        System.arraycopy(askJson.getBytes(), 0, fillByte, 0, len);
        return fillByte;
    }

    // 进行AES加密
    public static String encryptByte(byte[] sSrc, String sKey, String sIv) throws Exception {
        if (sKey == null || sSrc == null) {
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(sIv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encryptVal = cipher.doFinal(sSrc);
        encryptVal = Arrays.copyOfRange(encryptVal, 0, encryptVal.length - 16);
        // noinspection Since15
        return Base64.encodeBase64String(encryptVal);
    }


    // 加密
    public static String encrypt(String sSrc, String sKey) {
        try {
            if (StringUtils.isBlank(sSrc)) {
                return sSrc;
            }
            if (sKey == null) {
                log.error("Key为空null");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        } catch (Exception e) {
            log.error("AES加密错误", e);
            return null;
        }
    }

    // 解密
    public static String decrypt(String sSrc, String sKey) {
        try {
            if (StringUtils.isBlank(sSrc)) {
                return sSrc;
            }
            // 判断Key是否正确
            if (sKey == null) {
                log.error("Key为空null");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception e) {
            log.error("AE解密错误", e);
            return null;
        }
    }
}