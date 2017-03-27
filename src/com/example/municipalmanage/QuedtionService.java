package com.example.municipalmanage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

public class QuedtionService {

	// ����XML����
	private static List<Questions> parseXML(InputStream ins) throws Exception {
		List<Questions> books = null;
		Questions book = null;

		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlPullParser parser = factory.newPullParser();

		XmlPullParser parser = Xml.newPullParser(); // ��android.util.Xml����һ��XmlPullParserʵ��
		parser.setInput(ins, "UTF-8"); // ���������� ��ָ�����뷽ʽ

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				
				books = new ArrayList<Questions>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("id")) {
					book = new Questions();
					eventType = parser.next();
					book.setId(parser.getText());
				} else if (parser.getName().equals("type")) {
					eventType = parser.next();
					book.setType(parser.getText());

				} else if (parser.getName().equals("address")) {
					eventType = parser.next();
					book.setAddress(parser.getText());
				} else if (parser.getName().equals("content")) {
					eventType = parser.next();
					book.setContent(parser.getText());
				} else if (parser.getName().equals("state")) {
					eventType = parser.next();
					book.setState(parser.getText());

				} else if (parser.getName().equals("weChat")) {
					eventType = parser.next();

				} else if (parser.getName().equals("time")) {
					eventType = parser.next();
					book.setTime(parser.getText());
				} else if (parser.getName().equals("picture")) {
					eventType = parser.next();
					book.setImage(parser.getText());
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("find")) {
					books.add(book);
					book = null;
				}
				break;
			}
			eventType = parser.next();
		}
		
		return books;

	}

	// ��ȡ���µ���Ѷ
	public static List<Questions> getLastnews(String Urlpath) throws Exception {
		URL url = new URL(Urlpath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		InputStream ins = conn.getInputStream();
		return parseXML(ins);

	}

}
