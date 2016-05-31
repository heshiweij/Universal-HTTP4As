package cn.ifavor.networkutil;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-28
 * @Time: 19:37
 * @des ${TODO}
 */
public class RequestManager {
    private static final int FIXED_THREAD = 5;
    private static volatile RequestManager mSingleInstance = null;

    private HashMap<String, ArrayList<Request>> mRequestMap;
    private final Executor mExecutor;

    private RequestManager() {
        mRequestMap = new HashMap<>();
        mExecutor = Executors.newFixedThreadPool(FIXED_THREAD);

    }

    public static RequestManager getSingleInstance() {
        if (mSingleInstance == null) {
            synchronized (RequestManager.class) {
                if (mSingleInstance == null) {
                    mSingleInstance = new RequestManager();
                }
            }
        }

        return mSingleInstance;
    }

    /**
     * execute request
     *
     * @param request
     */
    public void startRequest(Request request) {
        request.execute(mExecutor);

        String tag = request.getTag();
        if (tag != null) {
            if (!mRequestMap.containsKey(tag)) {
                mRequestMap.put(tag, new ArrayList<Request>());
            }

            ArrayList<Request> requests = mRequestMap.get(tag);
            requests.add(request);

        }
    }

    /**
     * execute request
     *
     * @param request
     */
    public static void performRequest(Request request) {
       getSingleInstance().startRequest(request);
    }

    /**
     * cancel request by tag
     * @param tag
     */
    public void cancelTag(String tag) {
        cancelTag(tag, false);
    }

    /**
     * cancel request by tag
     *
     * @param tag
     */
    public void cancelTag(String tag, boolean force) {
        // find requests by tag, and cancel them

        if (TextUtils.isEmpty(tag)) {
            return;
        }

        for (Map.Entry<String, ArrayList<Request>>
                entry : mRequestMap.entrySet()) {

            if (mRequestMap.containsKey(tag)) {
                ArrayList<Request> requests = mRequestMap.remove(tag); // use reomve not get, to remove list from map
                for (Request request : requests) {
                    request.cancel(force);
                }
                // clear all requset in this arraylist
                requests.clear();
            }

        }
    }

    /**
     * cancel all request by tag
     */
    public void cancelAll() {
        for (Map.Entry<String, ArrayList<Request>>
                entry : mRequestMap.entrySet()) {

            ArrayList<Request> requests = entry.getValue();
            for (Request request : requests) {

                // true means force cancel all request task
                request.cancel(true);
            }
            requests.clear();
        }
    }

}
