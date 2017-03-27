package com.example.municipalmanage;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
   
public class QuestionDetails extends Activity {
	
	private SharedPreferences pref;
	public static final int SHOWERROR=1;
	final Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case SHOWERROR:
				Toast.makeText(QuestionDetails.this, "接单成功", Toast.LENGTH_SHORT).show();
				break;
		    default:
		    	break;
			}
		};
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_details);
		pref = getSharedPreferences("data", MODE_PRIVATE);
		TextView id=(TextView)findViewById(R.id.id);
		TextView time=(TextView)findViewById(R.id.time);
		TextView type=(TextView)findViewById(R.id.type);
		TextView address=(TextView)findViewById(R.id.address);
		TextView content=(TextView)findViewById(R.id.content);
		Button button=(Button)findViewById(R.id.receive);
		ImageView picture = (ImageView) findViewById(R.id.picture);
		Intent intent=getIntent();
	    String id1=intent.getStringExtra("id");
		final int id2=Integer.valueOf(id1);
		String time1=intent.getStringExtra("time");
		String type1=intent.getStringExtra("type");
		String address1=intent.getStringExtra("address");
		String content1=intent.getStringExtra("content");
		String url=intent.getStringExtra("url");
		Bitmap bitmap=getBitmap(url);
		final String account=pref.getString("account", "");//获取用户名
		 Bundle bundle = new Bundle();
		 bundle = this.getIntent().getExtras();
		 final int state=bundle.getInt("state");
		id.setText(id1);
		time.setText(time1);
		type.setText(type1);
		address.setText(address1);
		content.setText(content1);
		picture.setImageBitmap(bitmap);
		
	    button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(state==0){
					new Thread() {

						public void run() {
							try {// 192.168.16.107
								String urlStr = WebApi.URL_receive;
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
								osw.write("project_id=" + id2 + "&user_id=" + account);
								osw.flush();
								osw.close();
								// 读取响应数据
								BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
							//	String s = "";
							//	s = in.readLine();
							//	if (s.equals("{result:ok}")) {
									Message message=new Message();
									message.what=SHOWERROR;
									handler.sendMessage(message);
									
							//	} 
								
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}.start();
					
				}else if(state==1){
					Toast.makeText(QuestionDetails.this, "已经被接单，你来迟了~", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
	}
	
	private Bitmap getBitmap(String url) {  
        Bitmap bm = null;  
        try {  
            URL iconUrl = new URL(url);  
            URLConnection conn = iconUrl.openConnection();  
            HttpURLConnection http = (HttpURLConnection) conn;         
            int length = http.getContentLength();       
            conn.connect();  
            // 获得图像的字符流  
            InputStream is = conn.getInputStream();  
            BufferedInputStream bis = new BufferedInputStream(is, length);  
            bm = BitmapFactory.decodeStream(bis);  
            bis.close();  
            is.close();// 关闭流  
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }  
        return bm;  
    }  
}
