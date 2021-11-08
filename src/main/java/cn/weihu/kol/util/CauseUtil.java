package cn.weihu.kol.util;

import java.util.HashMap;
import java.util.Map;

public class CauseUtil {
    public static Map<Integer, String> causeMap = new HashMap();

    static {
        causeMap.put(0, "正常结束");
        causeMap.put(901, "语音转换失败");
        causeMap.put(1000, "未识别的请求");
        causeMap.put(1001, "被叫号码格式错误");
        causeMap.put(1002, "被叫号码不可用");
        causeMap.put(1003, "呼叫标识不可用");
        causeMap.put(1017, "用户忙");
        causeMap.put(1018, "用户无响应");
        causeMap.put(1019, "未应答");
        causeMap.put(1021, "拒绝应答");
        causeMap.put(1027, "呼叫受限");
        causeMap.put(1041, "临时故障");
        causeMap.put(1063, "服务不可用");
        causeMap.put(1102, "呼叫超时");
        causeMap.put(1401, "文件不存在");
        causeMap.put(1402, "放音失败");
        causeMap.put(1487, "呼叫取消");
        causeMap.put(1503, "临时故障");
        causeMap.put(1606, "用户未注册");
        causeMap.put(1801, "用户已关机");
        causeMap.put(1802, "用户不在服务区");
        causeMap.put(2000, "未知错误");
    }

    public static String cause2Str(Integer cause) {
        return causeMap.get(cause) == null ? "呼叫异常:" + cause : causeMap.get(cause);
    }
}
