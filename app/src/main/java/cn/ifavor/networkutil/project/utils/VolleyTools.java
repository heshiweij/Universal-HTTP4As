package cn.ifavor.networkutil.project.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import cn.ifavor.networkutil.project.DefaultApplication;

/**
 * Volley 工具类
 */
public class VolleyTools {

	private volatile static VolleyTools mInstance = null;

	private RequestQueue mQueue;
	private MyImageCache mImageCache;
	private ImageLoader mImageLoader;

	private VolleyTools() {
		mQueue = Volley.newRequestQueue(DefaultApplication.getContext());
		mImageCache = new MyImageCache();
		mImageLoader = new ImageLoader(mQueue, mImageCache);
	}

	public static VolleyTools getSingletonInstance() {
		if (mInstance == null) {
			synchronized(VolleyTools.class){
				if (mInstance == null){
					mInstance = new VolleyTools();
				}
			}
		}
		return mInstance;
	}


    ///////////////////////////////////////////////////////////////////////////
    // Instance
    ///////////////////////////////////////////////////////////////////////////

	public RequestQueue getRequestQueue(){
		return mQueue;
	}
	
	public void performRequest(Request request){
		getRequestQueue().add(request);
	}

	public MyImageCache getImageCache(){
		return mImageCache;
	}
	
	public ImageLoader getImageLoader(){
		return mImageLoader;
	}


    ///////////////////////////////////////////////////////////////////////////
    // static
    ///////////////////////////////////////////////////////////////////////////


    public static RequestQueue newRequestQueue(){
        return getSingletonInstance().getRequestQueue();
    }

    public static void executeRequest( Request request){
        getSingletonInstance().performRequest(request);
    }

    public static MyImageCache newImageCache(){
        return getSingletonInstance().getImageCache();
    }

    public static ImageLoader newImageLoader(){
        return getSingletonInstance().getImageLoader();
    }

    public static void  cancelTag(String tag){
        getSingletonInstance().getRequestQueue().cancelAll(tag);
    }

	private class MyImageCache implements ImageLoader.ImageCache {

		private LruCache<String, Bitmap> mCache = null;

		public MyImageCache() {
			int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
			mCache = new LruCache<String, Bitmap>(maxSize){

				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
		}
	}

}
