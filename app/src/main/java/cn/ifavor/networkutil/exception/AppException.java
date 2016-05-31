package cn.ifavor.networkutil.exception;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-27
 * @Time: 20:58
 * @des ${TODO}
 */
public class AppException extends Exception {
    private int statusCode;

    private ExceptionResponse exceptionResponse;


    public AppException(ExceptionResponse exceptionResponse) {
        // 初始化父类
        super(exceptionResponse.getMessage(), exceptionResponse.getCause());

        this.statusCode =exceptionResponse.getStatusCode();
        this.exceptionResponse = exceptionResponse;
    }

    public AppException(ExceptionResponse.ErrorType errorType, String message){
        // 初始化父类
        super(message);

        exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorType(errorType);
        exceptionResponse.setMessage(message);
    }


    public int getStatusCode() {
        return statusCode;
    }

    public ExceptionResponse.ErrorType getErrorType(){
        return  exceptionResponse.getErrorType();
    }

    @Override
    public String toString() {
        return "AppException{" +
                "statusCode=" + statusCode +
                ", exceptionResponse=" + exceptionResponse +
                '}';
    }
}
