package com.example.municipalmanage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 三级缓存之网络缓存
 */
public class NetCacheUtils {

    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        mLocalCacheUtils = localCacheUtils;
        mMemoryCacheUtils = memoryCacheUtils;
    }

  
    /**
     * 网络下载图片
     * @param url
     * @return
     */
    public Bitmap downLoadBitmap(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                //图片压缩
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=2;//宽高压缩为原来的1/2
                options.inPreferredConfig=Bitmap.Config.ARGB_4444;
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(),null,options);

                //从网络获取图片后,保存至本地缓存
                mLocalCacheUtils.setBitmapToLocal(url, bitmap);
                //保存至内存中
                mMemoryCacheUtils.setBitmapToMemory(url, bitmap);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return null;
    }
}