package com.example.municipalmanage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class MyOrder extends Activity {
	private SharedPreferences pref;
	List<Questions> ques=new ArrayList<Questions>();
	List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	String s = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_list);
		pref = getSharedPreferences("data", MODE_PRIVATE);
		final String account=pref.getString("account", "");
		 Log.d("url", account);
		final ListView listView = (ListView) findViewById(R.id.listView);				
				try {// 192.168.16.107
					String urlStr = WebApi. URL_MyOrder;
					URL url = new URL(urlStr);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true); // 可以发送数据
					conn.setDoInput(true); // 可以接收数据
					conn.setRequestMethod("POST"); // POST方法
					// 必须注意此处需要设置UserAgent，否则google会返回403
					conn.setRequestProperty("User-Agent",
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.connect();
					// 写入的POST数据
					OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
					osw.write("phone="+account);
					osw.flush();
					osw.close();
					// 读取响应数据
					InputStream ins = conn.getInputStream();
				     ques = parseXML(ins);
					 Log.d("url", ques.size()+"");
					 
						for (Questions questions : ques) {
							HashMap<String, Object> item = new HashMap<String, Object>();
							URL image = questions.getb();// 获取图片URL
							Bitmap bm=questions.getBitmap(image);
							 Log.d("url", image+"");
							int state = questions.getState();
							String sta = null;
							switch (state) {
							case 0:
								sta = "未接单";
								break;
							case 1:
								sta = "已接单 ";
								break;
							default:
								break;
							}
						
							item.put("picture", bm);		
							item.put("time", questions.getTime());
							 Log.d("url", questions.getTime());
							item.put("content", questions.getContent());
							item.put("type", questions.getType());
							item.put("state", sta);
							 Log.d("url", sta);
							data.add(item);
						}
						/*
						 * SimpleAdapter adapter = new SimpleAdapter( this, getData(),
						 * R.layout.main, new String[] {"img","title","info"}, new int[] {
						 * R.id.img, R.id.title, R.id.info}); //setListAdapter(adapter);
						 * adapter.setViewBinder(new MyViewBinder());
						 * lv.setAdapter(adapter);
						 */
						Log.d("url", data.size()+"g");
						
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
				} catch (ProtocolException e) {
					
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			
		
		SimpleAdapter adapter = new SimpleAdapter(MyOrder.this, data, R.layout.question_item,
				new String[] { "picture", "time", "content", "type", "state" }, new int[] { R.id.question_image,
						R.id.question_time, R.id.question_content, R.id.question_type, R.id.question_state });
		adapter.setViewBinder(new MyViewBinder());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Questions data = ques.get(position);
				
				Intent intent = new Intent(MyOrder.this, MyOrderdetail.class);
				intent.putExtra("id", data.getId());
				intent.putExtra("time", data.getTime());
				intent.putExtra("type", data.getType());
				intent.putExtra("address", data.getAddress());
				intent.putExtra("content", data.getContent());
				intent.putExtra("url", data.getb1());
				Bundle bundle = new Bundle();
				bundle.putInt("state", data.getState());
				intent.putExtras(bundle);
				// intent.putExtra("bitmap", bit);
				startActivity(intent);

			}
		});


		
	}

	class MyViewBinder implements ViewBinder {
		/**
		 * view：要板顶数据的视图 data：要绑定到视图的数据
		 * textRepresentation：一个表示所支持数据的安全的字符串，结果是data.toString()或空字符串，但不能是Null
		 * 返回值：如果数据绑定到视图返回真，否则返回假
		 */
		@Override
		public boolean setViewValue(View view, Object data, String textRepresentation) {
			if ((view instanceof ImageView) & (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bmp = (Bitmap) data;
				iv.setImageBitmap(bmp);
				return true;
			}

			return false;
		}
	}
	private static List<Questions> parseXML(InputStream ins) throws Exception {
		List<Questions> books = null;
		Questions book = null;

		// XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		// XmlPullParser parser = factory.newPullParser();

		XmlPullParser parser = Xml.newPullParser(); // 由android.util.Xml创建一个XmlPullParser实例
		parser.setInput(ins, "UTF-8"); // 设置输入流 并指明编码方式

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

	

}
