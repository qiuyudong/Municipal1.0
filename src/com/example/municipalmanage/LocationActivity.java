package com.example.municipalmanage;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

/**
 * ��λ
 * 
 * @author ys
 *
 */
public class LocationActivity extends Activity implements OnClickListener {
	// tab�л�
	private TabHost tabhost;
	private List<Contact> contactList = new ArrayList<Contact>();

	private MapView mapview;
	private BaiduMap bdMap;

	private LocationClient locationClient;
	private BDLocationListener locationListener;
	private BDNotifyListener notifyListener;

	private double longitude;// ����
	private double latitude;// ά��
	private float radius;// ��λ���Ȱ뾶����λ����
	private String addrStr;// ���������
	private float direction;// �ֻ�������Ϣ

	private int locType;

	// ��λ��ť
	private Button locateBtn;
	private Button findQuestion;
	private Button questionList;
	//private Button uploadMaintenance;
	private Button myorder;
	// ��λģʽ ����ͨ-����-���̣�
	private MyLocationConfiguration.LocationMode currentMode;
	// ��λͼ������
	private BitmapDescriptor currentMarker = null;

	// �����豸
	private Vibrator mVibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		mapview = (MapView) findViewById(R.id.bd_mapview);
		mapview.showZoomControls(false);
		bdMap = mapview.getMap();
		locateBtn = (Button) findViewById(R.id.locate_btn);
		findQuestion = (Button) findViewById(R.id.locate_btn2);
		questionList = (Button) findViewById(R.id.locate_btn3);
	//	uploadMaintenance= (Button) findViewById(R.id.locate_btn4);
		myorder=(Button)findViewById(R.id.locate_btn5);
		locateBtn.setOnClickListener(this);
		currentMode = MyLocationConfiguration.LocationMode.NORMAL;
		locateBtn.setText("��ͨ��ͼ");
		mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		init();

		findQuestion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LocationActivity.this, FindQuestionActivity.class);
				intent.putExtra("address", addrStr);
				startActivity(intent);// TODO Auto-generated method stub

			}
		});
		questionList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ProgressDialog progressDialog=new ProgressDialog(LocationActivity.this);
				progressDialog.setTitle("���ڼ����б�");
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(true);
				progressDialog.show();
				new Thread(){
					public void run(){
					try{
					sleep(1000);//���������һ��ͬ���̣߳��������ؽ���������
					Intent intent = new Intent(LocationActivity.this, QuestionList.class);
					startActivity(intent);
					}
					catch(Exception e)
					{ e.printStackTrace(); }
					progressDialog.dismiss();
					}
					}.start();
				 
			}
		});
		
		myorder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ProgressDialog progressDialog=new ProgressDialog(LocationActivity.this);
				progressDialog.setTitle("���ڼ����б�");
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(true);
				progressDialog.show();
				new Thread(){
					public void run(){
					try{
					sleep(1000);//���������һ��ͬ���̣߳��������ؽ���������
					Intent intent = new Intent(LocationActivity.this, MyOrder.class);
					startActivity(intent);
					}
					catch(Exception e)
					{ e.printStackTrace(); }
					progressDialog.dismiss();
					}
					}.start();
				
			}
		});
		initContacts();//��ʼ������
		ContactAdapter adapter = new ContactAdapter(LocationActivity.this, R.layout.contact_item, contactList);
		ListView listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Contact contact=contactList.get(position);
				String number=contact.getPhone_numeber();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number.trim()));  
                startActivity(intent);  
				
			}
		});

		tabhost = (TabHost) findViewById(android.R.id.tabhost);

		LocalActivityManager lam = new

		LocalActivityManager(LocationActivity.this, false);

		lam.dispatchCreate(savedInstanceState);

		
		tabhost.setup(lam);

		TabHost.TabSpec tab1 = tabhost.newTabSpec("one");
		tab1.setIndicator("��ͼ");
		tab1.setContent(R.id.map);

		TabHost.TabSpec tab2 = tabhost.newTabSpec("two");
		tab2.setIndicator("��ϵ��");
		tab2.setContent(R.id.contact);

		tabhost.addTab(tab1);
		tabhost.addTab(tab2);
		
	}
	

	/**
	 * 
	 */
	private void init() {
		bdMap.setMyLocationEnabled(true);
		// 1. ��ʼ��LocationClient��
		locationClient = new LocationClient(getApplicationContext());
		// 2. ����LocationListener��
		locationListener = new MyLocationListener();
		// 3. ע���������
		locationClient.registerLocationListener(locationListener);
		// 4. ���ò���
		LocationClientOption locOption = new LocationClientOption();
		locOption.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ
		locOption.setCoorType("bd09ll");// ���ö�λ�������
		locOption.setScanSpan(5000);// ���÷���λ����ļ��ʱ��,ms
		locOption.setIsNeedAddress(true);// ���صĶ�λ���������ַ��Ϣ
		locOption.setNeedDeviceDirect(true);// ���÷��ؽ�������ֻ��ķ���

		locationClient.setLocOption(locOption);
		// 5. ע��λ�����Ѽ����¼�
		notifyListener = new MyNotifyListener();
		notifyListener.SetNotifyLocation(longitude, latitude, 3000, "bd09ll");// ���ȣ�ά�ȣ���Χ����������
		locationClient.registerNotify(notifyListener);
		// 6. ����/�ر� ��λSDK
		locationClient.start();
		// locationClient.stop();
		// ����λ���첽��ȡ��ǰλ�ã���Ϊ���첽�ģ������������أ�������������
		// ��λ�Ľ����ReceiveListener�ķ���onReceive�����Ĳ����з��ء�
		// ����λSDK�Ӷ�λ�����ж���λ�ú���һ��û�����仯��������һ�ζ�λ�������ʱ���򲻻ᷢ���������󣬶��Ƿ�����һ�εĶ�λ�����
		// ����ֵ��0�����������˶�λ 1��serviceû������ 2��û�м�������
		// 6����������ʱ��̫�̣�ǰ����������λʱ��������С��1000ms��
		/*
		 * if (locationClient != null && locationClient.isStarted()) {
		 * requestResult = locationClient.requestLocation(); } else {
		 * Log.d("LocSDK5", "locClient is null or not started"); }
		 */

	}

	/**
	 * 
	 * @author 
	 *
	 */
	class MyLocationListener implements BDLocationListener {
		// �첽���صĶ�λ���
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			if (location.hasRadius()) {// �ж��Ƿ��ж�λ���Ȱ뾶
				radius = location.getRadius();
			}
             addrStr=location.getAddrStr();
			// ���춨λ����
			MyLocationData locData = new MyLocationData.Builder().accuracy(radius)//
					.direction(direction)// ����
					.latitude(latitude)//
					.longitude(longitude)//
					.build();
			// ���ö�λ����
			bdMap.setMyLocationData(locData);
			LatLng ll = new LatLng(latitude, longitude);
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
			bdMap.animateMapStatus(msu);
         
		}
	}

	/**
	 * λ�����Ѽ�����
	 * 
	 * @author
	 *
	 */
	class MyNotifyListener extends BDNotifyListener {
		@Override
		public void onNotify(BDLocation bdLocation, float distance) {
			super.onNotify(bdLocation, distance);
			mVibrator.vibrate(1000);// �������ѵ��趨λ�ø���
			Toast.makeText(LocationActivity.this, "������", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.locate_btn:// ��λ
			switch (currentMode) {
			case NORMAL:
				locateBtn.setText("������ͼ");
				currentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
				break;
			case FOLLOWING:
				locateBtn.setText("������ͼ");
				currentMode = MyLocationConfiguration.LocationMode.COMPASS;
				break;
			case COMPASS:
				locateBtn.setText("��ͨ��ͼ");
				currentMode = MyLocationConfiguration.LocationMode.NORMAL;
				break;
			}
			bdMap.setMyLocationConfigeration(new MyLocationConfiguration(currentMode, true, currentMarker));
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapview.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapview.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapview.onDestroy();
		locationClient.unRegisterLocationListener(locationListener);
		// ȡ��λ������
		locationClient.removeNotifyEvent(notifyListener);
		locationClient.stop();
	}

	public void initContacts() {
		Contact Bob = new Contact("Bob", "123456789");
		Contact Echo = new Contact("Echo", "123456789");
		contactList.add(Bob);
		contactList.add(Echo);

	}

}
