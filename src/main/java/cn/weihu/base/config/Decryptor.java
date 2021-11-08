package cn.weihu.base.config;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * DES解密类
 *
 * @author wuweiwei_kzx
 */
public class Decryptor {

    /**
     * 解密Cipher对象
     */
    private Cipher decryptCipher;

    /**
     * 解密类构造函数
     *
     * @param key
     * @throws Exception
     */
    public Decryptor(String key) throws Exception {
        Key deskey = getKey(key.getBytes(StandardCharsets.UTF_8));
        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, deskey);
    }

    /**
     * 将指定字符串生成密钥
     *
     * @param bytes
     * @return 密钥Key
     */
    private Key getKey(byte[] bytes) {
        byte[] arrB = new byte[8];
        for(int i = 0; i < bytes.length && i < arrB.length; i++)
            arrB[i] = bytes[i];
        return new SecretKeySpec(arrB, "DES");
    }

    /**
     * 解密字节数组
     *
     * @param arrB
     * @return 解密后的byte[]
     * @throws Exception
     */
    private byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     *
     * @param strIn
     * @return 解密后的String
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)), StandardCharsets.UTF_8);
    }

    /**
     * 将表示16进制值的字符串转换为byte数组
     *
     * @param strIn
     * @return 明文byte[]
     */
    private byte[] hexStr2ByteArr(String strIn) {
        byte[] arrB   = strIn.getBytes(StandardCharsets.UTF_8);
        int    iLen   = arrB.length;
        byte[] arrOut = new byte[iLen / 2];
        for(int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2, StandardCharsets.UTF_8);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

}
