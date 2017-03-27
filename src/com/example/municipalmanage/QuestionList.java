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
				String image = questions.getb1();// ��ȡͼƬURL
				MyBitmapUtils cache=new MyBitmapUtils();//ͼƬ���л���
				Bitmap bitmap=cache.disPlay(image);//ͼƬ���л���
				int state = questions.getState();
				String sta = null;
				switch (state) {
				case 0:
					sta = "δ�ӵ�";
					break;
				case 1:
					sta = "�ѽӵ� ";
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
		 * view��Ҫ�嶥���ݵ���ͼ data��Ҫ�󶨵���ͼ������
		 * textRepresentation��һ����ʾ��֧�����ݵİ�ȫ���ַ����������data.toString()����ַ�������������Null
		 * ����ֵ��������ݰ󶨵���ͼ�����棬���򷵻ؼ�
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
