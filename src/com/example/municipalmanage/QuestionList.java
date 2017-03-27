package com.example.municipalmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class QuestionList extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_list);
		
		final ListView listView = (ListView) findViewById(R.id.listView);
		try {

			String xmlPath = WebApi.URL_LoadFinds;
			final List<Questions> ques = QuedtionService.getLastnews(xmlPath);
			Log.d("url", ques.size()+"");
			List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			for (Questions questions : ques) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				String image = questions.getb1();// 获取图片URL
				MyBitmapUtils cache=new MyBitmapUtils();//图片进行缓存
				Bitmap bitmap=cache.disPlay(image);//图片进行缓存
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
				item.put("picture", bitmap);
				item.put("time", questions.getTime());
				item.put("content", questions.getContent());
				item.put("type", questions.getType());
				item.put("state", sta);

				data.add(item);
			}
			/*
			 * SimpleAdapter adapter = new SimpleAdapter( this, getData(),
			 * R.layout.main, new String[] {"img","title","info"}, new int[] {
			 * R.id.img, R.id.title, R.id.info}); //setListAdapter(adapter);
			 * adapter.setViewBinder(new MyViewBinder());
			 * lv.setAdapter(adapter);
			 */
			SimpleAdapter adapter = new SimpleAdapter(QuestionList.this, data, R.layout.question_item,
					new String[] { "picture", "time", "content", "type", "state" }, new int[] { R.id.question_image,
							R.id.question_time, R.id.question_content, R.id.question_type, R.id.question_state });
			adapter.setViewBinder(new MyViewBinder());
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Questions data = ques.get(position);
					
					Intent intent = new Intent(QuestionList.this, QuestionDetails.class);
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

		} catch (Exception e) {

			e.printStackTrace();
		}
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
}
