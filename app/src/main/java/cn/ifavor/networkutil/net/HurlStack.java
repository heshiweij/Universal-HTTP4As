package cn.ifavor.networkutil.net;

import android.text.TextUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import cn.ifavor.networkutil.Request;
import cn.ifavor.networkutil.exception.AppException;
import cn.ifavor.networkutil.exception.ExceptionHelper;
import cn.ifavor.networkutil.exception.ExceptionResponse;
import cn.ifavor.networkutil.listener.OnProgressUpdateListener;
import cn.ifavor.networkutil.utils.URLUtil;
import cn.ifavor.networkutil.utils.UploadUtil;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-25
 * @Time: 18:01
 * @des ${TODO}
 */
public class HurlStack implements  UrlStack<HttpURLConnection>{


    /**
     * 执行具体的请求
     * @param request 请求对象
     * @param onProgressUpdateListener
     * @return
     * @throws Exception
     */
    public  HttpURLConnection execute(Request request, OnProgressUpdateListener onProgressUpdateListener) throws AppException {
        // 检查URL合法性
        if (!URLUtil.isNetworkUrl(request.getUrl().toString())){

            ExceptionResponse e = new ExceptionResponse();
            e.setMessage("the url: " + request.getUrl() + " is not valid");
            e.setErrorType(ExceptionResponse.ErrorType.URL_INVALID);
            throw new AppException(e);
        }

        switch (request.getMethod()){
            case DELETE:
            case GET:
                return get(request);
            case PUT:
            case POST:
                return post(request, onProgressUpdateListener);
            default:
                return null;
        }
    }


    /**
     * GET 请求
     * @param request
     * @return
     * @throws AppException
     */
    public  HttpURLConnection get(Request request) throws AppException {
        try {

            // 检查是否需要取消
            checkIfCancel(request);

            URL _url = new URL(request.getUrl() + addParams(request.getParams(), false));
            HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
            connection.setRequestMethod(request.getMethod().name());

            // 设置连接超时时间
            connection.setConnectTimeout(CONNECCTION_TIMEOUT_SECONDS * 1000);
            // 设置读取超时时间
            connection.setReadTimeout(READ_TIMEOUT_SECONDS * 1000);

            // 设置请求头信息
            addHeaders(request.getHeaders(), connection);

            // 检查是否需要取消
            checkIfCancel(request);

            return connection;
        } catch (IOException e) {

            throw ExceptionHelper.getAppExceptionFromException(e);
        }
    }

    /**
     * POST 请求
     * @param request
     * @param listener
     * @return
     * @throws AppException
     */
    public HttpURLConnection post(Request request, OnProgressUpdateListener listener) throws AppException {
        try {
            // 检查是否需要取消
            checkIfCancel(request);

            URL _url = new URL(request.getUrl());

            HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
            connection.setRequestMethod(request.getMethod().name());
            // 设置允许写入内容
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // 添加头部信息
            addHeaders(request.getHeaders(), connection);

            // 写数据
            OutputStream outputStream = connection.getOutputStream();

            if (TextUtils.isEmpty( request.getContent() )){
                if (!TextUtils.isEmpty(request.getFilePath())){
                    UploadUtil.upload(outputStream, request.getFilePath());
                }
            } else {
                if (listener != null){
                    UploadUtil.upload(outputStream, request.getContent(), request.getFileEntities(), listener);
                } else {
                    UploadUtil.upload(outputStream, request.getContent(), request.getFileEntities());
                }
            }

//            if ( !TextUtils.isEmpty( request.getContent() )){
//                outputStream.write(request.getContent().getBytes());
//            } else {
//                outputStream.write( addParams(request.getParams(), true).getBytes() );
//            }

            // 设置连接超时时间
            connection.setConnectTimeout(CONNECCTION_TIMEOUT_SECONDS * 1000);
            // 设置读取超时时间
            connection.setReadTimeout(READ_TIMEOUT_SECONDS * 1000);


            // 检查是否需要取消
            checkIfCancel(request);

            return connection;
        } catch (IOException e) {

            throw ExceptionHelper.getAppExceptionFromException(e);
        }
    }

    /**
     * 设置请求头信息
     *
     * @param headers    头部信息 - 键值对
     * @param connection 连接对象
     */
    public  void addHeaders(Map<String, String> headers, HttpURLConnection connection) {
        // 设置请求头信息
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 设置请求参数
     *
     * @param params    参数信息 - 键值对
     */
    public  String addParams(Map<String, String> params, boolean isPost) throws AppException {
        try {
            StringBuilder sb = new StringBuilder(isPost ? "" : "?");

            if (params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String encodedKey = URLEncoder.encode(entry.getKey(), "UTF-8");
                    String encodedVal = URLEncoder.encode(entry.getValue(), "UTF-8");
                    sb.append( String.format("%s=%s&",encodedKey, encodedVal) );
                }
            }

            sb.delete(sb.length() - 1, sb.length());

            return sb.toString();
        } catch (UnsupportedEncodingException e) {
           throw ExceptionHelper.getAppExceptionFromException(e);
        }
    }

    /**
     * 检查是否取消
     * @param request
     * @throws AppException
     */
    private void checkIfCancel(Request request) throws AppException {
        if (request.isCancel()){
            ExceptionHelper.throwCancelException();
        }
    }


}
