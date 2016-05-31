package cn.ifavor.networkutil.listener;

import cn.ifavor.networkutil.exception.AppException;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-28
 * @Time: 15:30
 * @des ${TODO}
 */
public interface OnGlobalExceptionListener {

    boolean handleException(AppException e);

}
