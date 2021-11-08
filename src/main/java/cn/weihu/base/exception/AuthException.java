package cn.weihu.base.exception;


public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrce) {
        super(message, cause, enableSuppression, writableStackTrce);
    }
}