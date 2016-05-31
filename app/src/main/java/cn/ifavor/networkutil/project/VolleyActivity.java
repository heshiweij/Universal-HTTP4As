package cn.ifavor.networkutil.project;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import cn.ifavor.networkutil.R;
import cn.ifavor.networkutil.project.base.BaseActivity;
import cn.ifavor.networkutil.project.utils.VolleyTools;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-28
 * @Time: 20:22
 * @des ${TODO}
 */
public class VolleyActivity extends
        BaseActivity implements View.OnClickListener {

    private Button mBtnRequestString;
    private String URL = "http://www.cniao5.com/course/1/0/1";
    private ImageView mIvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mIvTest = (ImageView) findViewById(R.id.tv_test);

        mBtnRequestString = (Button) findViewById(R.id.request_string_btn);
        mBtnRequestString.setOnClickListener(this);
    }

    public void onClick1(View v) {

        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                System.out.println("onResponse:"+s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("onErrorResponse： " +volleyError);
            }
        });
        request.setTag(toString());
        VolleyTools.executeRequest(request);
    }

    @Override
    public void onClick(View v) {

        ImageRequest request = new ImageRequest(URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                mIvTest.setImageBitmap(bitmap);

            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("load error!" + volleyError);

            }
        });

        request.setTag(toString());
        VolleyTools.executeRequest(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 取消请求
        VolleyTools.cancelTag(toString());
    }
}
