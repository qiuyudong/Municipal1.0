package com.example.municipalmanage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//列表中的问题类
public class Questions {
	private URL url;
	private String uuu;
	private String time;
	private String content;
	private String type;
	private int state;
	private String address;
	private String id;

	public Questions() {
		// TODO Auto-generated constructor stub
	}

	public Questions(byte[] b, String time, String content, String type, int state, String address, int id) {

	}

	public Bitmap getBitmap(URL url) throws IOException{
	    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	    conn.setConnectTimeout(5000);
	    conn.setRequestMethod("GET");
	    if(conn.getResponseCode() == 200){
	    InputStream inputStream = conn.getInputStream();
	    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	    return bitmap;
	    }
	    return null;
	    }

	public URL getb() {
		return url;
	}
	public String getb1() {
		return uuu;
	}
	public URL setImage(String image) {
		String image1 = image.substring(93);
		image1=image1.replaceAll("\\\\", "/");
		String image2 = WebApi.URL_SERVICE + image1;
		uuu=image2;
		
		try {
			url = new URL(image2);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;

	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(String state) {
		this.state = Integer.valueOf(state);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
