package cn.weihu.kol.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;


public class SM4Test
{
    public static void main( String[] args )
    {
        String content = "sm4 Test";
        // key必须是16位
        String key="1234567890123456";
        SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes());
        String encryptHex = sm4.encryptHex(content);
        String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        System.out.println(encryptHex+"\r\n"+decryptStr);

    }
}