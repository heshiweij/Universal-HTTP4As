package cn.ifavor.networkutil.project.base;

import android.support.v7.app.AppCompatActivity;

import cn.ifavor.networkutil.RequestManager;
import cn.ifavor.networkutil.exception.AppException;
import cn.ifavor.networkutil.listener.OnGlobalExceptionListener;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-28
 * @Time: 15:41
 * @des ${TODO}
 */
public abstract class BaseActivity extends
        AppCompatActivity implements OnGlobalExceptionListener {

    @Override
    public boolean handleException(AppException e) {
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        String tag = toString();
        System.out.println(tag + ", on Class: " + getClass().getSimpleName());
        RequestManager.getSingleInstance().cancelTag(tag);
    }

}
