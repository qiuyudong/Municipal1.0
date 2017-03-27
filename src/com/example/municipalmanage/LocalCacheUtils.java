package com.example.municipalmanage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * ��������֮���ػ���
 */
public class LocalCacheUtils {

    private static final String CACHE_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/WerbNews";

    /**
     * �ӱ��ض�ȡͼƬ
     * @param url
     */
    public Bitmap getBitmapFromLocal(String url){
        String fileName = null;//��ͼƬ��url�����ļ���,������MD5����
        try {
            fileName = MD5Encoder.encode(url);
            File file=new File(CACHE_PATH,fileName);

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * �������ȡͼƬ��,���������ػ���
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url,Bitmap bitmap){
        try {
            String fileName = MD5Encoder.encode(url);//��ͼƬ��url�����ļ���,������MD5����
            File file=new File(CACHE_PATH,fileName);

            //ͨ���õ��ļ��ĸ��ļ�,�жϸ��ļ��Ƿ����
            File parentFile = file.getParentFile();
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }

            //��ͼƬ����������
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}