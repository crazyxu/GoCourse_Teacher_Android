package net.gocourse.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * User: xucan
 * Date: 2015-07-22
 * Time: 14:06
 * FIXME
 */
public class BitmapCache implements ImageLoader.ImageCache {
    //单一实例
    private static LruCache<String, Bitmap> mCache;
    private BitmapCacheBack cacheBack;

    public BitmapCache(BitmapCacheBack cacheBack) {
        this.cacheBack=cacheBack;
        //用于缓存图片的存储大小
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024)/8;
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String s) {
        return mCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        //缓存在内存中
        mCache.put(s,bitmap);
        //其他操作
        if (cacheBack!=null)
            cacheBack.dealWithBitmap(bitmap);

    }

    //回调接口，使用ImageLoader设置ImageVIew的同时可以处理bitmap数据
    interface BitmapCacheBack{
        void dealWithBitmap(Bitmap bitmap);
    }
}
