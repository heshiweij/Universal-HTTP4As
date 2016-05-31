package cn.ifavor.networkutil.exception;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-28
 * @Time: 14:48
 * @des  异常实体封装类
 */
public class ExceptionResponse {
    public enum  ErrorType {URL_INVALID, SERVER_ERROR, UNKOWN, CANCEL, TIMEOUT,UPLOAD}

    /**
     * 状态码
     */
    private int statusCode;

    /**
     * 消息（对应Exception 的 message）
     */
    private String message;

    /**
     * 原因（对应Exception 的 cause）
     */
    private Throwable cause;

    private ErrorType mErrorType = ErrorType.UNKOWN;


    public ExceptionResponse() {
        // Empty Construct
    }

    public ExceptionResponse(int statusCode, String message, Throwable cause) {
        this.statusCode = statusCode;
        this.message = message;
        this.cause = cause;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public ErrorType getErrorType() {
        return mErrorType;
    }

    public void setErrorType(ErrorType errorType) {
        mErrorType = errorType;
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", cause=" + cause +
                ", mErrorType=" + mErrorType +
                '}';
    }
}
