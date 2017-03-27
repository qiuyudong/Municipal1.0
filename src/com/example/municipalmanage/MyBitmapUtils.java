package com.example.municipalmanage;

import android.graphics.Bitmap;

/**
 * 自定义的BitmapUtils,实现三级缓存
 */
public class MyBitmapUtils {

    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public MyBitmapUtils(){
        mMemoryCacheUtils=new MemoryCacheUtils();
        mLocalCacheUtils=new LocalCacheUtils();
        mNetCacheUtils=new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
    }

    public   Bitmap  disPlay(String url) {
        
        Bitmap bitmap;
        //内存缓存
        bitmap=mMemoryCacheUtils.getBitmapFromMemory(url);
        if (bitmap!=null){    
            return bitmap;
        }

        //本地缓存
        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if(bitmap !=null){
            mMemoryCacheUtils.setBitmapToMemory(url,bitmap);
            return bitmap;
        }
        bitmap=mNetCacheUtils.downLoadBitmap(url);
        return bitmap;
    }
}

