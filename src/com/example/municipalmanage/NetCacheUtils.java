package com.example.municipalmanage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * ��������֮���绺��
 */
public class NetCacheUtils {

    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        mLocalCacheUtils = localCacheUtils;
        mMemoryCacheUtils = memoryCacheUtils;
    }

  
    /**
     * ��������ͼƬ
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
                //ͼƬѹ��
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=2;//���ѹ��Ϊԭ����1/2
                options.inPreferredConfig=Bitmap.Config.ARGB_4444;
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(),null,options);

                //�������ȡͼƬ��,���������ػ���
                mLocalCacheUtils.setBitmapToLocal(url, bitmap);
                //�������ڴ���
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