package cn.ifavor.networkutil.exception;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-28
 * @Time: 15:00
 * @des 将Exception转换AppException
 */
public class ExceptionHelper {

    public static AppException getAppExceptionFromException(Exception e){
        // 如果AppException 说明之前已经封装成AppException了
        if (e instanceof  AppException){
            return (AppException) e;
        }

        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(e.getMessage());
        response.setCause(e.getCause());

//        // 判断是否是超时引起的异常
//        if (e instanceof SocketTimeoutException){
//            response.setErrorType(ExceptionResponse.ErrorType.TIMEOUT);
//        }

        return new AppException(response);
    }

    /**
     * 抛出用于取消的异常
     */
    public static void throwCancelException() throws AppException {
        throw new AppException(ExceptionResponse.ErrorType.CANCEL, "用户已取消本次请求");
    }
}
