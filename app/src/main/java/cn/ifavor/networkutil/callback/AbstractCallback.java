package cn.ifavor.networkutil.callback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import cn.ifavor.networkutil.exception.AppException;
import cn.ifavor.networkutil.exception.ExceptionHelper;
import cn.ifavor.networkutil.exception.ExceptionResponse;
import cn.ifavor.networkutil.listener.OnProgressUpdateListener;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-25
 * @Time: 19:30
 * @des ${TODO}
 */
public abstract class AbstractCallback<T> implements ICallback<T> {
    private boolean isCancel;

    @Override
    public T parse(HttpURLConnection connection, OnProgressUpdateListener onProgressUpdateListener) throws AppException {

        // 检查是否需要取消
        checkIfCancel();

        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                InputStream is = connection.getInputStream();
                //创建一个内存流
                ByteArrayOutputStream baos = null;

                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                //将数据从InputStream拷贝到内存流中
                while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                    // 检查是否需要取消
                    checkIfCancel();

                    baos.write(buffer, 0, len);
                }

                baos.close();
                is.close();

                //通过内存流的toByteArray()方法得到一个byte数组
                byte[] newBuffer = baos.toByteArray();
                //根据byte数组创建字符串

                //根据不同的编码解析成不同格式的字符串
                String temp = new String(newBuffer, 0, newBuffer.length, Charset.defaultCharset());

                // 检查是否需要取消
                checkIfCancel();

                T t = bindData(temp);
                return postRequest(t);
            } else {

                // 返回服务器错误
                ExceptionResponse e = new ExceptionResponse();
                e.setStatusCode(connection.getResponseCode());
                e.setMessage(connection.getResponseMessage());

                throw new AppException(e);
            }
        } catch (InterruptedIOException timeoutEx){
            throw new AppException(ExceptionResponse.ErrorType.TIMEOUT,"连接服务器超时");

        }catch (IOException e) {
            throw ExceptionHelper.getAppExceptionFromException(e);

        }
    }


    @Override
    public T postRequest(T t) {
        return t;
    }

    @Override
    public void onProgressUpdate(Integer state, int currLen, int totalLen) {

    }

    @Override
    public T parse(HttpURLConnection connection) throws AppException {
        return parse(connection, null);
    }

    public abstract T bindData(String temp);

    @Override
    public void cancel() {
        isCancel = true;
    }

    /**
     * 检查是否需要取消
     * @throws AppException
     */
    public void checkIfCancel() throws AppException {
        if (isCancel){
            ExceptionHelper.throwCancelException();
        }
    }

    @Override
    public void onCancel() {

    }


    @Override
    public T preRequest() {
        return null;
    }
}
