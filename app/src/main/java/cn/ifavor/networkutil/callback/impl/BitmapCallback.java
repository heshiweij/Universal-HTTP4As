package cn.ifavor.networkutil.callback.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;

import cn.ifavor.networkutil.callback.AbstractCallback;
import cn.ifavor.networkutil.exception.AppException;
import cn.ifavor.networkutil.exception.ExceptionHelper;
import cn.ifavor.networkutil.exception.ExceptionResponse;
import cn.ifavor.networkutil.listener.OnProgressUpdateListener;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-25
 * @Time: 20:20
 * @des ${TODO}
 */
public abstract class BitmapCallback extends AbstractCallback<Bitmap> {

    @Override
    public Bitmap parse(HttpURLConnection connection,
                        OnProgressUpdateListener onProgressUpdateListener) throws AppException {
        // 检查是否需要取消
        checkIfCancel();

        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == 200){
                InputStream is = connection.getInputStream();
                //            is.close();


                // 检查是否需要取消
                checkIfCancel();

                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return postRequest(bitmap);
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
    public Bitmap bindData(String temp) {
        return null;
    }

}
