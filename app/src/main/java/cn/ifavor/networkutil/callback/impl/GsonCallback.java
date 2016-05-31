package cn.ifavor.networkutil.callback.impl;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.ifavor.networkutil.callback.AbstractCallback;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-25
 * @Time: 20:15
 * @des ${TODO}
 */
public abstract class GsonCallback<T> extends AbstractCallback<T> {
//    private Class<T> clazz;

    @Override
    public T bindData(String temp) {
        Gson gson = new Gson();
        return gson.fromJson(temp, getClassEntity());
    }

    /**
     * 根据泛型获取class对象
     * @return
     */
    public Class<T> getClassEntity(){
        Class<T> entityClass = null;
        Type t = getClass().getGenericSuperclass();
        if(t instanceof ParameterizedType){
            Type[] p = ((ParameterizedType)t).getActualTypeArguments();
            entityClass = (Class<T>)p[0];
        }
        return entityClass;
    }

//    public GsonCallback setReturnType(Class<T> returnType) {
//        clazz = returnType;
//        return this;
//    }
}
