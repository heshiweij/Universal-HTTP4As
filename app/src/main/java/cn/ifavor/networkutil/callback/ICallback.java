package cn.ifavor.networkutil.callback;

import java.net.HttpURLConnection;

import cn.ifavor.networkutil.exception.AppException;
import cn.ifavor.networkutil.listener.OnProgressUpdateListener;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-25
 * @Time: 19:05
 * @des ${TODO}
 */
public interface ICallback<T> {
    /**
     * execute on  main thread
     * @param content
     */
    void onSuccess(T content);

    /**
     * execute on  main thread
     * @param ex
     */
    void onFailure(AppException ex);

    /**
     * execute on   sub thread
     * @param connection
     * @return
     * @throws AppException
     */
    T parse(HttpURLConnection connection) throws AppException;

    /**
     * execute on   sub thread
     * @param connection
     * @param onProgressUpdateListener
     * @return
     * @throws AppException
     */
    T parse(HttpURLConnection connection, OnProgressUpdateListener onProgressUpdateListener) throws AppException;

    /**
     * execute on  main thread
     * @param state 当前进度的类型
     * @param currLen 当前已经执行的进度
     * @param totalLen 总进度
     */
    void onProgressUpdate(Integer state, int currLen, int totalLen) ;

    void cancel();

    /**
     * execute on  main  thread
     */
    void onCancel();

    /**
     *  execute on  sub  thread
     * @param t
     * @return
     */
    T postRequest(T t);

    /**
     * execute on  sub  thread
     * @return
     */
    T preRequest();

}
