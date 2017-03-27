package com.example.municipalmanage;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * ��������֮�ڴ滺��
 */
public class MemoryCacheUtils {

    // private HashMap<String,Bitmap> mMemoryCache=new HashMap<>();//1.��Ϊǿ����,��������ڴ���������Կ���ʹ�����������õķ���
    // private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<>();//2.��Ϊ��Android2.3+��,ϵͳ�����ȿ��ǻ��������ö���,�ٷ����ʹ��LruCache
    private LruCache<String,Bitmap> mMemoryCache;

    public MemoryCacheUtils(){
        long maxMemory = Runtime.getRuntime().maxMemory()/8;//�õ��ֻ���������ڴ��1/8,������ָ���ڴ�,��ʼ����
        //��Ҫ����������ڴ����ֵ,�����Ĭ���ڴ�16M,�����һ����ͬ
        mMemoryCache=new LruCache<String,Bitmap>((int) maxMemory){
            //���ڼ���ÿ����Ŀ�Ĵ�С
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getByteCount();
                return byteCount;
            }
        };

    }

    /**
     * ���ڴ��ж�ͼƬ
     * @param url
     */
    public Bitmap getBitmapFromMemory(String url) {
        //Bitmap bitmap = mMemoryCache.get(url);//1.ǿ���÷���
        /*2.�����÷���
        SoftReference<Bitmap> bitmapSoftReference = mMemoryCache.get(url);
        if (bitmapSoftReference != null) {
            Bitmap bitmap = bitmapSoftReference.get();
            return bitmap;
        }
        */
        Bitmap bitmap = mMemoryCache.get(url);
        return bitmap;

    }

    /**
     * ���ڴ���дͼƬ
     * @param url
     * @param bitmap
     */
    public void setBitmapToMemory(String url, Bitmap bitmap) {
        //mMemoryCache.put(url, bitmap);//1.ǿ���÷���
        /*2.�����÷���
        mMemoryCache.put(url, new SoftReference<>(bitmap));
        */
        mMemoryCache.put(url,bitmap);
    }
}