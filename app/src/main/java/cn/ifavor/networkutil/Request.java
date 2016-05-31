package cn.ifavor.networkutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import cn.ifavor.networkutil.callback.ICallback;
import cn.ifavor.networkutil.listener.OnGlobalExceptionListener;
import cn.ifavor.networkutil.utils.FileEntity;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-25
 * @Time: 18:28
 * @des ${TODO}
 */
public class Request {

    private RequestTask mTask;

    public enum Method {GET, POST, PUT, DELETE}

    public enum RequestTool {HTTPCLINET, HTTPURLCONNECTION}

    // Content-Type 类型
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_PLAIN = "text/plain";

    // 请求地址
    private String url;

    //region Description 待提交的请求内容
    // 请求内容
    private String content;

    private String filePath;

    private ArrayList<FileEntity> fileEntities;
    //endregion


    // 请求头信息
    private Map<String, String> headers;

    // 请求方法（默认get）
    private Method method = Method.GET;

    // 回调接口
    private ICallback mCallback;

    // 请求工具
    private RequestTool mRequestTool = RequestTool.HTTPURLCONNECTION;

    // 请求参数
    private Map<String, String> params;

    // 是否需要打开下载进度更新监听回调
    private boolean mEnableProgressUpdate;

    // 全局异常处理回调
    private OnGlobalExceptionListener mOnGlobalExceptionListener;

    // 重试次数
    private int mRetryCount;

    // 是否取消本次请求
    private boolean isCancel;

    // 用来被取消请求的tag标记
    private String mTag;

    /**
     * execute in Request
     * @param executor
     */
    public void execute(Executor executor) {
        mTask = new RequestTask(this);
        mTask.executeOnExecutor(executor);
    }

    public Request(String url) {
        this(url, null);
    }

    public Request(String url, Map<String, String> headers) {
        this(url, null, headers, Method.GET);
    }

    public Request(String url, String content, Map<String, String> headers, Method method) {
        this.url = url;
        this.content = content;
        this.headers = headers;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        if (this.headers == null){
            this.headers = headers;
            return;
        }

        this.headers.putAll(headers);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public ICallback getCallback() {
        return mCallback;
    }

    public void setCallback(ICallback callback) {
        mCallback = callback;
    }

    public RequestTool getRequestTool() {
        return mRequestTool;
    }

    public void setRequestTool(RequestTool requestTool) {
        mRequestTool = requestTool;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setContentType(String contentType) {
        if (headers == null) {
            headers = new HashMap<>();
        }

        headers.put("Content-Type", contentType);
    }

    public void setEnableProgressUpdate(boolean enableProgressUpdate) {
        mEnableProgressUpdate = enableProgressUpdate;
    }

    public boolean isEnableProgressUpdate() {
        return mEnableProgressUpdate;
    }

    public void setOnGlobalExceptionListener(OnGlobalExceptionListener onGlobalExceptionListener) {
        mOnGlobalExceptionListener = onGlobalExceptionListener;
    }

    public OnGlobalExceptionListener getOnGlobalExceptionListener() {
        return mOnGlobalExceptionListener;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    /**
     * 设置重试次数，如果为0，则不重试
     * @param retryCount
     */
    public void setRetruCount(int retryCount) {
        this.mRetryCount = retryCount;
    }

    public boolean isCancel() {
        return isCancel;
    }

    /**
     * 取消当前的所有请求（正在执行的请求：通过埋点取消，没有执行的通过cancel取消）
     * @param force 强制退出
     */
    public void cancel(boolean force) {
        this.isCancel = true;

        if (mCallback != null){
            mCallback.cancel();
        }

        // 执行 AsyncTask 的cancel 不再有回调
        if (force && mTask != null){
            mTask.cancel(true);
        }

    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public String getTag() {
        return mTag;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<FileEntity> getFileEntities() {
        return fileEntities;
    }

    public void setFileEntities(ArrayList<FileEntity> fileEntities) {
        this.fileEntities = fileEntities;
    }
}
