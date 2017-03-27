package com.example.municipalmanage;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final int SHOWERROR=1;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private CheckBox rememberPass;
    private Button button;
    private EditText edit1;
    private EditText edit2;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pref = getSharedPreferences("data", MODE_PRIVATE);
		button=(Button)findViewById(R.id.button1);
		edit1=(EditText)findViewById(R.id.username);
		edit2=(EditText)findViewById(R.id.password);
		rememberPass=(CheckBox)findViewById(R.id.checkBox1);
		boolean isRememeber = pref.getBoolean("remember_password", false);
		if (isRememeber) {
			String account = pref.getString("account", "");
			String password = pref.getString("password", "");
			edit1.setText(account);
			edit2.setText(password);
			rememberPass.setChecked(true);

		}
		final Handler handler=new Handler(){
			public void handleMessage(Message msg) {
				switch(msg.what){
				case SHOWERROR:
					Toast.makeText(MainActivity.this, "账号密码错误", Toast.LENGTH_SHORT).show();
					break;
			    default:
			    	break;
				}
			};
		};
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				edit1 = (EditText) findViewById(R.id.username);

				edit2 = (EditText) findViewById(R.id.password);
				// 验证
				if (validate()) {
					// 登录

					final String u = edit1.getText().toString();
					final String p = edit2.getText().toString();

					new Thread() {

						public void run() {
							try {// 192.168.16.107
								String urlStr = WebApi.URL_LOGIN;
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
								osw.write("user.phone=" + u + "&user.password=" + p);
								osw.flush();
								osw.close();
								// 读取响应数据
								BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
								String s = "";
								s = in.readLine();
								if (s.equals("{result:ok}")) {
									editor = pref.edit();
									if (rememberPass.isChecked()) {
										editor.putBoolean("remember_password", true);
										editor.putString("account", u);
										editor.putString("password", p);
									} else {
										editor.clear();
									}
									editor.commit();
									Intent intent = new Intent(MainActivity.this, LocationActivity.class);
									startActivity(intent);
									MainActivity.this.finish();
								} else{
									Message message=new Message();
									message.what=SHOWERROR;
									handler.sendMessage(message);
								}

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

				}
			}
		});
	}
	/* 对用户名和密码进行非空验证 */
	private boolean validate() {
		String name = edit1.getText().toString();
		if (name.equals("")) {
			Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		String pwd = edit2.getText().toString();
		if (pwd.equals("")) {
			Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

}
