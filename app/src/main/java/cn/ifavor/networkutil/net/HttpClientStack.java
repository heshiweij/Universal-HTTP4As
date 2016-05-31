package cn.ifavor.networkutil.net;

import java.util.Map;

import cn.ifavor.networkutil.Request;
import cn.ifavor.networkutil.exception.AppException;
import cn.ifavor.networkutil.listener.OnProgressUpdateListener;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-26
 * @Time: 10:02
 * @des ${TODO}
 */
public class HttpClientStack implements UrlStack<String> {

    @Override
    public String execute(Request request, OnProgressUpdateListener onProgressUpdateListener) throws AppException {
        return null;
    }

    @Override
    public String post(Request request, OnProgressUpdateListener onProgressUpdateListener) throws AppException {
        return null;
    }

    @Override
    public String get(Request request) throws AppException {
        return null;
    }

    @Override
    public void addHeaders(Map<String, String> headers, String connection) {

    }

    @Override
    public String addParams(Map<String, String> params, boolean isPost) throws AppException {
        return null;
    }

}
