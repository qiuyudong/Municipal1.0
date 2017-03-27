package com.example.municipalmanage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StremTools {
	/**
	 * 读取输入流中的数据
	 * @param inputStream  输入流
	 * @return   二进制的流数据
	 * @throws Exception
	 */
	public static byte[] read(InputStream inputStream) throws Exception {
		ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int length=0;
		while((length=inputStream.read(buffer))!=-1){
			outputStream.write(buffer,0,length);
		}
		inputStream.close();
		return outputStream.toByteArray();
	}

}