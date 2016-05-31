package cn.ifavor.networkutil;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.HttpURLConnection;

import cn.ifavor.networkutil.exception.AppException;
import cn.ifavor.networkutil.exception.ExceptionResponse;
import cn.ifavor.networkutil.listener.OnProgressUpdateListener;
import cn.ifavor.networkutil.net.HurlStack;
import cn.ifavor.networkutil.net.UrlStack;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-25
 * @Time: 19:02
 * @des ${TODO}
 */
public class RequestTask extends AsyncTask<Void, Integer, Object> {
    private static final String TAG = "RequestTask";

    // 当前已重试次数
    private int currRetryIndex = 0;

    Request mRequest;

    // 进度更新类型
    private static int STATE_UPLOAD = 0;
    private static int STATE_DOWNLOAD = 1;

    /**
     * 构造方法，传入请求对象
     *
     * @param request
     */
    public RequestTask(Request request) {
        this.mRequest = request;

        // 检查 Request 是否为空
        if (request == null){
            throw new RuntimeException("Request is null");
        }

    }

    /**
     * 在后台执行
     *
     * @param params
     * @return
     */
    @Override
    protected Object doInBackground(Void... params) {
        // if has preData, will not execute request
        Object preData = mRequest.getCallback().preRequest();
        if (preData != null){
            return preData;
        }

        return requestNetwork();
    }

    @Nullable
    private Object requestNetwork() {
        try {
            UrlStack stack = null;
            if (mRequest.getRequestTool() == Request.RequestTool.HTTPURLCONNECTION) {
                // HTTPUrlConnection

                stack = new HurlStack();
                HttpURLConnection connection = (HttpURLConnection) stack.execute(mRequest, new OnProgressUpdateListener(){

                    @Override
                    public void onUpdate(int currLen, int totalLen) {
                        publishProgress(STATE_UPLOAD, currLen, totalLen);
                    }
                });

                if (mRequest.isEnableProgressUpdate()){
                    return mRequest.getCallback().parse(connection, new OnProgressUpdateListener() {

                        @Override
                        public void onUpdate(int currLen, int totalLen) {
                            publishProgress(STATE_DOWNLOAD ,currLen, totalLen);
                        }
                    });
                } else {
                    return mRequest.getCallback().parse(connection);
                }

            }

            return null;
        } catch (AppException e) {

            // 重试机制
            if (e.getErrorType() == ExceptionResponse.ErrorType.TIMEOUT){
                if (currRetryIndex < mRequest.getRetryCount()){
                    Log.d(TAG, "正在尝试，当前重试次数："+currRetryIndex);
                    currRetryIndex ++;

                    requestNetwork();

                }
            }

            return e;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mRequest.getCallback().onProgressUpdate(values[0], values[1], values[2]);
    }

    /**
     * 在前台执行
     *
     * @param o
     */
    @Override
    protected void onPostExecute(Object o) {
        if (o instanceof AppException) {
            AppException appException = (AppException) o;
            if (appException.getErrorType() == ExceptionResponse.ErrorType.CANCEL){
                mRequest.getCallback().onCancel();

                return;
            }

            if (mRequest.getOnGlobalExceptionListener() != null){
                if (!mRequest.getOnGlobalExceptionListener().handleException(appException)){
                    mRequest.getCallback().onFailure(appException);
                }
            } else {
                mRequest.getCallback().onFailure(appException);
            }

        } else {
            mRequest.getCallback().onSuccess(o);
        }

    }
}
