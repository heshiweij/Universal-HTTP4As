package cn.ifavor.networkutil.callback.impl;

import cn.ifavor.networkutil.callback.AbstractCallback;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-25
 * @Time: 20:19
 * @des ${TODO}
 */
public abstract class StringCallback extends AbstractCallback<String> {

    @Override
    public String bindData(String temp) {
        return temp;
    }
}
