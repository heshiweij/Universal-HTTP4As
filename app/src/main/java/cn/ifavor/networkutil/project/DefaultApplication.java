package cn.ifavor.networkutil.project;

import android.app.Application;
import android.content.Context;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-28
 * @Time: 20:17
 * @des ${TODO}
 */
public class DefaultApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
