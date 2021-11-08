package cn.weihu.kol.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public class ExceptionUtil {
    public static String getMessage(Exception e) {
        StringWriter sw = null;
        PrintWriter  pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            // 将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if(sw != null) {
                try {
                    sw.close();
                } catch(IOException e1) {
                    e1.printStackTrace();
                }
            }
            if(pw != null) {
                pw.close();
            }
        }
//		log.error(sw.toString());
        return sw.toString();
    }
}