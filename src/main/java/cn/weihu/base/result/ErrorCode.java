package cn.weihu.base.result;


public enum ErrorCode {
    SUCCESS("0", "success"),
    NOT_AUTH("1001", "没有权限"),
    ERROR("8888", "系统异常"),
    PARAMS_NULL("3001", "参数为空"),

    USER_NOT_FOUND("403", "用户不存在"),
    NO_AUTH("403", "no auth"),
    USERNAME_OR_PASSWORD_INVALID("403", "用户名或密码错误"),
    OLD_PASSWORD_ERROR("403", "原密码错误"),
    USER_IS_DISABLE("403", "用户已被禁用"),
    ACCESS_KEY_INVALID("403", "accessKeyId错误"),
    ACCESS_KEY_OR_SECRET_INVALID("403", "accessKeyId或accessKeySecret错误"),


    USERNAME_ALREADY_EXISTED("100101", "用户名已存在"),
    COMPANY_SHORT_NAME_IS_EXIST("100102", "企业简称已存在"),
    ROLE_NAME_IS_EXIST("100103", "角色名已存在"),
    COMPANY_LACK_OF_BALANCE("100104", "余额不足"),
    BLACKLIST_SOURCE_IS_NULL("100105", "黑名单来源不能为空"),
    BLACKLIST_SOURCE_ILLEGAL("100106", "黑名单来源不合法"),
    BLACKLIST_THRESHOLD_IS_NULL("100107", "黑名单阈值不能为空"),
    NUMBER_PORTABILITY_CHECK_FAILURE("100108", "调用失败,请联系管理员"),
    NUMBER_PORTABILITY_CHECK_ERROR("100109", "携号转网检测功能异常"),
    COMPANY_LIMIT_ILLEGAL("100110", "企业设置并发值不能小于已被分配的总并发值"),
    COMPANY_NOT_FOUND("100111", "企业信息不存在"),

    FLOW_NOT_FOUND("100201", "流程不存在"),
    FLOW_PARAM_ASYNC_FAILURE("100202", "流程参数同步失败"),
    FLOW_STATUS_ILLEGAL("100203", "流程状态不合法"),
    GROUP_NOT_FOUND("100204", "技能组不存在"),
    FLOW_LIMIT_ILLEGAL("100204", "流程并发不能为0"),
    FLOW_PARAM_IS_NULL("100205", "流程无入参"),
    TEMPLATE_EXPORT_ERROR("100206", "流程入参模板导出错误"),
    FLOW_SOURCE_ILLEGAL("100207", "流程来源不合法"),
    FLOW_CREATE_LIMITS_ILLEGAL("100208", "机器人并发值不能大于"),
    FLOW_PARAMS_ILLEGAL("100209", "流程入参数据格式不合法"),
    FLOW_NAME_EXISTED("100210", "流程名称已存在"),


    TASK_NOT_FOUND("100301", "任务信息不存在"),
    TASK_LOG_NOT_FOUND("100302", "任务记录信息不存在"),
    CONTACTS_AND_FILEPATH_IS_NULL("100303", "任务联系人不能为空"),
    NOFIND_FLOW_SOURCE("1003004", "未找到流程版本"),
    NOFIND_TASK_MANAGER("1003005", "未找对应版本的任务处理器"),
    CONTACTS_FILE_LOAD_ERROR("1003006", "名单文件加载异常"),
    TASK_STATUS_ILLEGAL("1003007", "非法的任务状态"),
    PQ_TASK_NOT_FOUND("1003008", "错误的任务信息数据"),
    PERIOD_CALL_TIME_EMPTY("1003009", "可外呼时间段不能为空"),
    PERIOD_CALL_TIME_INVALID("1003010", "可外呼时间段格式错误"),
    STARTTIME_IS_AFTER_ENDTIME("1003011", "结束时间不能早于开始时间"),
    ENDTIME_IS_BEFORE_CURRENT("1003012", "结束时间不能早于当前时间+1小时"),
    NOFIND_BLACKLIST_SOURCE("1003013", "未找到黑名单来源"),
    NOFIND_BLACKLIST_MANAGER("1003014", "未找对应来源的黑名单处理器"),
    TASK_DISPATCH_ERROR("1003015", "此任务不在可外呼时间段内,不可启动"),
    TASK_STATUS_UPDATING("1003016", "任务状态变更中"),

    CALLNUMS_IS_NULL("1004001", "外显号码不能为空"),
    CALLNUMS_MORE_THEN_50("1004002", "单次添加不能超过50个外显号码"),
    CALLNUM_IS_BIND_CALLER("1004003", "当前外显号码已被绑定主叫号"),
    CALLER_IS_EXIST("1004004", "此主叫号已经存在"),
    CALLER_IS_NULL("1004005", "主叫号不能为空"),
    CALLER_COMPANY_UNBIND("1004006", "未绑定的主叫号码"),
    CALLER_CALLNUM_UNBIND("1004007", "当前主叫号未绑定外显号码"),

    SIP_NAME_IS_EXIST("1005001", "此名称已经存在"),
    SIP_COMPANY_NOT_BIND_CALLER("1005002", "该企业未绑定主叫号码"),
    SIP_GATEWAY_USERNAME_IS_NULL("1005003", "网关注册时,注册用户名不能为空"),
    SIP_GATEWAY_PASSWORD_IS_NULL("1005004", "网关注册时,注册密码不能为空"),
    SIP_GATEWAY_EXPIRETIME_IS_NULL("1005005", "网关注册时,注册超时时间不能为空"),
    SIP_USER_ID_IS_EXIST("1005006", "此SIP账号已经存在"),
    SIP_AGENT_NUMBER_IS_EXIST("1005007", "此座席工号已经存在"),
    SIP_AGENT_NOT_FOUND("1005008", "座席信息不存在"),
    SIP_AGENT_USER_NOT_FOUND("1005009", "座席用户不存在,请联系管理员"),

    FORM_NOT_FOUND("1006001", "表单信息不存在"),
    FORM_DATA_NOT_FOUND("1006002", "表单数据信息不存在"),

    HERMES_AUTH_MISSING("1007001", "认证信息缺失"),
    HERMES_AUTH_ERROR("1007002", "认证信息错误"),
    AUTH_ERROR_NODESKEY("1007003", "未配置DESKey");

    private String code;
    private String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
