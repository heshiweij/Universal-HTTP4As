package cn.ifavor.networkutil.net;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.ifavor.networkutil.Request;
import cn.ifavor.networkutil.exception.AppException;
import cn.ifavor.networkutil.listener.OnProgressUpdateListener;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-26
 * @Time: 09:55
 * @des ${TODO}
 */
public interface UrlStack<T> {
    public static final int CONNECCTION_TIMEOUT_SECONDS = 15;
    public static final int READ_TIMEOUT_SECONDS = 5;

    /**
     * 执行具体的请求
     * @param request
     * @param onProgressUpdateListener
     * @return
     * @throws Exception
     */
    public T execute(Request request, OnProgressUpdateListener onProgressUpdateListener) throws AppException;

    /**
     * 执行POST请求
     * @param request
     * @param onProgressUpdateListener
     * @return
     * @throws Exception
     */
    public  T post(Request request, OnProgressUpdateListener onProgressUpdateListener) throws AppException;

    /**
     * 执行GET请求
     * @param request
     * @return
     * @throws Exception
     */
    public  T get(Request request) throws AppException ;

    /**
     * 添加头信息
     * @param headers
     * @param connection
     */
    public  void addHeaders(Map<String, String> headers, T connection);

    /**
     * 添加参数
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public  String addParams(Map<String, String> params, boolean isPost) throws AppException;
}
