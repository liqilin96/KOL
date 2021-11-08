package cn.weihu.base.exception;


import cn.weihu.base.result.ErrorCode;

public class CheckException extends RuntimeException {
    private String code = "-1";

    public CheckException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
    }

    public CheckException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CheckException(String message) {
        super(message);
    }

    public CheckException(Throwable cause) {
        super(cause);
    }

    public CheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrce) {
        super(message, cause, enableSuppression, writableStackTrce);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}