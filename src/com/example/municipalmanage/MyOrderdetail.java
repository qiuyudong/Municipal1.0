package com.example.municipalmanage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
   
public class MyOrderdetail extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_orderdetail);
		TextView id=(TextView)findViewById(R.id.id);
		TextView time=(TextView)findViewById(R.id.time);
		TextView type=(TextView)findViewById(R.id.type);
		TextView address=(TextView)findViewById(R.id.address);
		TextView content=(TextView)findViewById(R.id.content);
		Button button=(Button)findViewById(R.id.receive);
		Intent intent=getIntent();
	    final String id1=intent.getStringExtra("id");
		String time1=intent.getStringExtra("time");
		String type1=intent.getStringExtra("type");
		String address1=intent.getStringExtra("address");
		String content1=intent.getStringExtra("content");
		 Bundle bundle = new Bundle();
		 bundle = this.getIntent().getExtras();
		 final int state=bundle.getInt("state");
		id.setText(id1);
		time.setText(time1);
		type.setText(type1);
		address.setText(address1);
		content.setText(content1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MyOrderdetail.this,MaintenanceUpload.class);
				intent.putExtra("id", id1);
				startActivity(intent);
			}
		});
	}
	
}
