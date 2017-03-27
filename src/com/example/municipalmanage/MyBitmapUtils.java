package com.example.municipalmanage;

import android.graphics.Bitmap;

/**
 * �Զ����BitmapUtils,ʵ����������
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
        //�ڴ滺��
        bitmap=mMemoryCacheUtils.getBitmapFromMemory(url);
        if (bitmap!=null){    
            return bitmap;
        }

        //���ػ���
        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if(bitmap !=null){
            mMemoryCacheUtils.setBitmapToMemory(url,bitmap);
            return bitmap;
        }
        bitmap=mNetCacheUtils.downLoadBitmap(url);
        return bitmap;
    }
}

