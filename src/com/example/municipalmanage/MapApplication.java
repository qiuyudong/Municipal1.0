package com.example.municipalmanage;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class MapApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// ��ʹ�� SDK �����֮ǰ��ʼ�� context ��Ϣ������ ApplicationContext
		SDKInitializer.initialize(this);
	}

}