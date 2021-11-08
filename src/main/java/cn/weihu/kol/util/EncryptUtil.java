package cn.weihu.kol.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class EncryptUtil {

    public static String encrypt(String type, String key, String content) {
        if(StringUtils.isBlank(type)) {
            log.error("encrypt type is null");
            return null;
        }
        if("aes".equalsIgnoreCase(type)) {
            return AESUtil.encrypt(content, key);
        }
        if("sm4".equalsIgnoreCase(type)) {
            SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes());
            return sm4.encryptHex(content);
        }
        if("des".equalsIgnoreCase(type)) {
            DESUtils desUtils = new DESUtils(key);
            return desUtils.encrypt(content);
        }
        return null;
    }

    public static String decrypt(String type, String key, String content) {
        if(StringUtils.isBlank(type)) {
            log.error("decrypt type is null");
            return null;
        }
        if("aes".equalsIgnoreCase(type)) {
            return AESUtil.decrypt(content, key);
        }
        if("sm4".equalsIgnoreCase(type)) {
            SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes());
            return sm4.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
        }
        if("des".equalsIgnoreCase(type)) {
            DESUtils desUtils = new DESUtils(key);
            return desUtils.decrypt(content);
        }
        return null;
    }

    public static void main(String[] args) {
        String key = "1234567890123456";
        System.out.println(">>>>>> key:" + key);
//        String content = "{\"name\":\"20210910-01\",\"sayUser\":\"xiaoyun\",\"filePath\":\"\",\"caller\":\"95795403\",\"contactsList\":[{\"params\":{\"name\":\"王麻子\"},\"phone\":\"15811178954\"}],\"templateSn\":\"81ebfe14-6cf2-44ad-b500-56a04c4804df\",\"taskType\":\"notice\"}";
//        String content = "{\"name\":\"20210915-01\",\"contactsList\":[{\"params\":{\"content\":\"建行语音通知测试\"},\"phone\":\"15811178954\"}],\"templateSn\":\"96343df9-7524-40b0-811f-c2bf839328df\",\"taskType\":\"notice\"}";
       String content = "61ecdf22-9ad0-4ec2-89d1-a222ae8ad6ec";
        String encrypt = encrypt("sm4", key, content);
        System.out.println(">>>>>> 加密后结果:" + encrypt);
        String decrypt = decrypt("sm4", key, encrypt);
        System.out.println(">>>>>> 解密后结果:" + decrypt);
    }
}
