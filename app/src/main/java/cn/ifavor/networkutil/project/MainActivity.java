package cn.ifavor.networkutil.project;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import cn.ifavor.networkutil.R;
import cn.ifavor.networkutil.Request;
import cn.ifavor.networkutil.RequestManager;
import cn.ifavor.networkutil.RequestTask;
import cn.ifavor.networkutil.callback.impl.FileCallback;
import cn.ifavor.networkutil.callback.impl.StringCallback;
import cn.ifavor.networkutil.exception.AppException;
import cn.ifavor.networkutil.exception.ExceptionResponse;
import cn.ifavor.networkutil.project.base.BaseActivity;
import cn.ifavor.networkutil.utils.FileEntity;

public class MainActivity extends BaseActivity{

    private static final String TAG = "MainActivity";
    private ImageView mIvTest;
    private Request mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawable();
    }

    private void initDrawable() {
        View view = findViewById(R.id.view);
//        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher);
//        int intrinsicWidth = drawable.getIntrinsicWidth();
//        int intrinsicHeight = drawable.getIntrinsicHeight();
//        System.out.println("intrinsicWidth: " + intrinsicWidth + " , intrinsicHeight: " + intrinsicHeight);

//        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back_shape);
//
//        view.setBackground(drawable);
//        Drawable newDrawable = view.getBackground();
//
//        int intrinsicWidth = drawable.getIntrinsicWidth();
//        int intrinsicHeight = drawable.getIntrinsicHeight();
//        System.out.println("intrinsicWidth: " + intrinsicWidth + " , intrinsicHeight: " + intrinsicHeight);

        //        float density = getResources().getDisplayMetrics().density;
//        System.out.println("density: " + density);


//        ImageView imageView = (ImageView) view;
//        TransitionDrawable transitionDrawable = (TransitionDrawable) imageView.getBackground();
//        transitionDrawable.startTransition(1000);
//        transitionDrawable.reverseTransition(1000);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                background.setLevel(101);
//
//            }
//        }, 2000);

    }

    /**
     * 下载
     *
     * @param v
     */
    public void GetData(View v) {

//        String url = "http://httpbin.org/post";
        String url = "http://httpbin.org/get";
        Request request = new Request(url, null);

        // 添加参数
//        Map<String, String> params = new LinkedHashMap<>();
//        params.put("name", "何世威");
//        params.put("age", "1");
//        request.setParams(params);

        // 设置请求方法
        request.setMethod(Request.Method.GET);

        // 设置Content-Type
//        request.setContentType(Request.CONTENT_TYPE_FORM);

        // 添加参数
//        Map<String, String> headers = new LinkedHashMap<>();
//        headers.put("User-Agent", "cn.ifavor.networkutil");
//        headers.put("customer_header", "cn.ifavor.networkutil123");
//        request.setHeaders(headers);

        // 设置打开监听回调
//        request.setEnableProgressUpdate(true);

        request.setCallback(new StringCallback() {

            /**
             * 指定预处理的数据，如果返回不为null，则直接使用该数据，不再请求网络
             * @return
             */
            @Override
            public String preRequest() {
                return "I am pre data, nice to meet u!";
            }

            @Override
            public String postRequest(String s) {
                System.out.println("postRequest: " + Thread.currentThread().getName() + s);
                return s;
            }

            @Override
            public void onSuccess(String content) {
                System.out.println("onSuccess: " + Thread.currentThread().getName()  + content);
            }

            @Override
            public void onFailure(AppException ex) {
                ex.printStackTrace();
            }

            @Override
            public void onCancel() {
                System.out.println("当前请求已被用户取消");

            }
        });

        // 设置全局统一异常处理监听器
//        request.setOnGlobalExceptionListener(this);
        request.setRetruCount(0);

        RequestTask task = new RequestTask(request);
        task.execute();

    }

    public void cancel(View v){
        mRequest.cancel(true);
    }

    /**
     * 下载文件
     * @param v
     */
    public void download(View v) {
        String url = "http://dlqncdn.miaopai.com/stream/MVaux41A4lkuWloBbGUGaQ__.mp4";
        mRequest = new Request(url);

        mRequest.setEnableProgressUpdate(true);

        // 设置用于取消的tag
        mRequest.setTag(toString());

        mRequest.setCallback(new FileCallback(this) {
            @Override
            public void onSuccess(String content) {
                System.out.println("download success and save in："+content);
            }

            @Override
            public void onFailure(AppException ex) {
                ex.printStackTrace();
                System.out.println("download fail, error info:"+ex.getMessage());

            }

            @Override
            public void onCancel() {
                System.out.println("download cancelled by user");

            }

            @Override
            public void onProgressUpdate(Integer state, int currLen, int totalLen) {
                System.out.println(currLen + " / " + totalLen);
                if (currLen >= totalLen / 2){
                    RequestManager.getSingleInstance().cancelTag(MainActivity.this.toString());
                }
            }
        }.setDirectory(FileCallback.CacheDirectory.SD_PUBLIC_DIR,null, null));


        // execute request with RequestManager( the singleton tool used to execute and cancel request )
        RequestManager.getSingleInstance().performRequest(mRequest);
    }

    @Override
    public boolean handleException(AppException e) {
        /* 过滤处理 */

        // 方法不允许
        if (e.getStatusCode() == 405){
            String message = e.getMessage();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            return true;
        }

        // 服务器内部错误
        if (e.getStatusCode() == 500){
            String message = e.getMessage();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            return true;
        }

        if (e.getErrorType() == ExceptionResponse.ErrorType.TIMEOUT){
            Toast.makeText(MainActivity.this, "Timeout ,Try again later", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    /**
     * 上传
     * @param view
     */
    public void upload(View view){
        String URL = "http://httpbin.org/post";
        Request request = new Request(URL);
        request.setMethod(Request.Method.POST);

        ArrayList<FileEntity> entities = new ArrayList<>();
        FileEntity entity = new FileEntity();
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator + "haha.png");
        entity.setFilePath(file.getAbsolutePath());
        entity.setFileName("haha.png");
        entity.setFileType("image/png");
        entities.add(entity);

        request.setFileEntities(entities);
        request.setContent("I am contnet");

        request.setCallback(new StringCallback() {
            @Override
            public void onSuccess(String content) {
                System.out.println("success: " + content);
            }

            @Override
            public void onFailure(AppException ex) {
                System.out.println("failed: " + ex);
            }

            @Override
            public void onProgressUpdate(Integer state, int currLen, int totalLen) {
                System.out.println("state: " + state + ", currLen: " + currLen + ", totalLen:" + totalLen);
            }
        });

        RequestManager.performRequest(request);
    }
}
