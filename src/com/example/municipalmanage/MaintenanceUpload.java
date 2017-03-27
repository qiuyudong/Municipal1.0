package com.example.municipalmanage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MaintenanceUpload extends Activity {

	String RequestURL = WebApi.URL_uploadFinish;
	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	private Button takePhoto;
	private Button upLoad;
	private EditText time;
	private EditText id;
	private ImageView picture;
	private Uri imageUri;
	private File outputImage;
	private String imageFilePath;
	private String request;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String str = format.format(new java.util.Date());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintenance_upload);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}	
		Intent intent=getIntent();
		String idChuanzhi=intent.getStringExtra("id");
		time = (EditText) findViewById(R.id.time);	
		id=(EditText)findViewById(R.id.id);
		takePhoto = (Button) findViewById(R.id.take_photo);
		upLoad = (Button) findViewById(R.id.upload);
		picture = (ImageView) findViewById(R.id.picture);
		id.setText(idChuanzhi);
		time.setText(str);
		takePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/filename.jpg";
				outputImage = new File(imageFilePath);
				// 创建File对象，储存拍照后的照片，存在根目录下
				try {
					if (outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();// TODO: handle exception
				}
				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, TAKE_PHOTO);// 启动相机程序

			}
		});

		upLoad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String id2 = id.getText().toString();			
				File file = new File(imageFilePath);
				final Map<String, String> params = new HashMap<String, String>();				
				params.put("time", str);
				params.put("project_id", id2);
				final Map<String, File> files = new HashMap<String, File>();
				files.put("uploadfile", file);

				 try {
					request = UploadUtil.post(RequestURL, params, files);					
						Toast.makeText(MaintenanceUpload.this, "上传成功", Toast.LENGTH_SHORT).show();
						finish();
				} catch (IOException e) {
					Toast.makeText(MaintenanceUpload.this, "上传失败，重新上传", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				
	//			if(request.equals("{result:ok}")){
	//			Toast.makeText(MaintenanceUpload.this, "上传成功", Toast.LENGTH_SHORT).show();}

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				try {
					Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
					picture.setImageBitmap(bitmap);

				} catch (Exception e) {
					e.printStackTrace();// TODO: handle exception
				}
			}
			break;
		default:
			break;
		}
	}
}
