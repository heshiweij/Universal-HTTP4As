package cn.ifavor.networkutil.callback.impl;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;

import cn.ifavor.networkutil.callback.AbstractCallback;
import cn.ifavor.networkutil.exception.AppException;
import cn.ifavor.networkutil.exception.ExceptionHelper;
import cn.ifavor.networkutil.exception.ExceptionResponse;
import cn.ifavor.networkutil.listener.OnProgressUpdateListener;
import cn.ifavor.networkutil.utils.SDCardHelper;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-26
 * @Time: 09:28
 * @des ${TODO}
 */
public abstract class FileCallback extends AbstractCallback<String> {

    private CacheDirectory mDirectory;
    private String mCustomDir;
    private Context mContext;
    private String mDirType;

    public FileCallback(Context context) {
        mContext = context;
    }

    public enum CacheDirectory{
        SD_PUBLIC_DIR,
        SD_PRIVATE_CACHE,
        SD_PRIVATE_FILES,
    }



    public FileCallback setDirectory(CacheDirectory directory, String customDir,  String dirType) {
        this.mDirectory = directory;
        this.mCustomDir = customDir;

        if (customDir == null){
            this.mCustomDir = "CUSTOM_DIR";
        } else {
            this.mCustomDir = customDir;
        }

        if (dirType == null){
            this.mDirType = Environment.DIRECTORY_DOWNLOADS;
        } else {
            this.mDirType = dirType;
        }

        return this;
    }

    @Override
    public String parse(HttpURLConnection connection,
                        OnProgressUpdateListener onProgressUpdateListener) throws AppException {
        // 检查是否需要取消
        checkIfCancel();

        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == 200){
                InputStream is = connection.getInputStream();
                //创建一个内存流
                FileOutputStream fos = null;

                String realPath = getRealPath(connection);

                fos = new FileOutputStream(realPath);
                byte[] buffer = new byte[1024];
                int len = 0;

                int totalLen = connection.getContentLength();
                int currLen = 0;

                //将数据从InputStream拷贝到内存流中
                while ((len = is.read(buffer,0,buffer.length))!=-1){
                    // 检查是否需要取消
                    checkIfCancel();

                    fos.write(buffer, 0, len);

                    currLen += len;
                    if (onProgressUpdateListener != null){
                        onProgressUpdateListener.onUpdate(currLen, totalLen);
                    }

                }

                fos.close();
                is.close();


                // 检查是否需要取消
                checkIfCancel();

                return postRequest(realPath);
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

    private String getRealPath(HttpURLConnection connection) {
        String URLStr = connection.getURL().toString();
        String filename = URLStr.substring(URLStr.lastIndexOf('/'));

        File result =  null;
        switch (mDirectory) {
            case SD_PUBLIC_DIR:
                String sdCardPublicDir = SDCardHelper.getSDCardPublicDir(mDirType);
                result = new File(sdCardPublicDir,  filename);

                break;
            case SD_PRIVATE_CACHE:
                String sdCardPrivateCacheDir = SDCardHelper.getSDCardPrivateCacheDir(mContext);
                result = new File(sdCardPrivateCacheDir,  filename);

                break;
            case SD_PRIVATE_FILES:
                String sdCardPrivateFilesDir = SDCardHelper.getSDCardPrivateFilesDir(mContext, mDirType);
                result = new File(sdCardPrivateFilesDir,  filename);

                break;
        }

        return  result.getAbsolutePath();
    }

    @Override
    public String bindData(String temp) {
        return null;
    }
}
