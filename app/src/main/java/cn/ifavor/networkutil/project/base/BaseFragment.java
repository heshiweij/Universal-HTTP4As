package cn.ifavor.networkutil.project.base;

import android.support.v4.app.Fragment;

import cn.ifavor.networkutil.RequestManager;
import cn.ifavor.networkutil.exception.AppException;
import cn.ifavor.networkutil.listener.OnGlobalExceptionListener;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-28
 * @Time: 15:42
 * @des ${TODO}
 */
public abstract class BaseFragment extends Fragment implements OnGlobalExceptionListener {

    @Override
    public boolean handleException(AppException e) {
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        RequestManager.getSingleInstance().cancelTag(toString());
    }
}
