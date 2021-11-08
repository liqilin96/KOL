package cn.weihu.kol.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneCheckUtil {

    /**
     * 手机号
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        Pattern pattern = Pattern.compile("^((\\+86)|(86)|(0))?[1][3456789][0-9]{9}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * 座机号
     *
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {
        Pattern pattern = Pattern.compile("^((\\+86)|(86)|(0))?[1][3456789][0-9]{9}$");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public static void main(String[] args) {
        String  phone = "158111789540";
        boolean b     = checkPhone(phone);
        System.out.println(">>>>>>>> " + b);
    }
}
