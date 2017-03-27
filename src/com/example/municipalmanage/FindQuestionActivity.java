package com.example.municipalmanage;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

public class FindQuestionActivity extends Activity {
	String RequestURL = WebApi.URL_uploadFind;
	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	private Button takePhoto;
	private Button upLoad;
	private EditText type;
	private EditText addr;
	private EditText time;
	private EditText disc;
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
		setContentView(R.layout.activity_find_question);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		Intent intent=getIntent();
		String address=intent.getStringExtra("address");
		type = (EditText) findViewById(R.id.type);
		addr = (EditText) findViewById(R.id.addr);
		time = (EditText) findViewById(R.id.time);
		disc = (EditText) findViewById(R.id.disc);
		takePhoto = (Button) findViewById(R.id.take_photo);
		upLoad = (Button) findViewById(R.id.upload);
		picture = (ImageView) findViewById(R.id.picture);
		time.setText(str);
		addr.setText(address);
		takePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/filename.jpg";
				outputImage = new File(imageFilePath);
				// ����File���󣬴������պ����Ƭ�����ڸ�Ŀ¼��
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
				startActivityForResult(intent, TAKE_PHOTO);// �����������
				

			}
		});

		upLoad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bitmap picturecompress=getimage(imageFilePath);//ͼƬѹ��
				saveBitmapFile(picturecompress);//ת��Ϊ·��
				String TYPE = type.getText().toString();
				String ADDR = addr.getText().toString();
				String DISC = disc.getText().toString();
				File file = new File(imageFilePath);
				final Map<String, String> params = new HashMap<String, String>();
				params.put("type", TYPE);
				params.put("address", ADDR);
				params.put("time", str);
				params.put("content", DISC);
				params.put("weChat", "qiuyudong");
				final Map<String, File> files = new HashMap<String, File>();
				files.put("uploadfile", file);

				 try {
					request = UploadUtil.post(RequestURL, params, files);			
						Toast.makeText(FindQuestionActivity.this, "�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			

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
	
	
	
	
	
	
	
	/** 
	 * ͼƬ����ѹ������ 
	 * 
	 * @param image 
	 * @return 
	 */  
	public static Bitmap compressImage(Bitmap image) {  
	  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    image.compress(Bitmap.CompressFormat.JPEG, 10, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��     
	    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��  
	    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ  
	    return bitmap;  
	}  
	
	/** 
	 * ͼƬ��������Сѹ������ 
	 * 
	 * @param srcPath ������·����ȡͼƬ��ѹ���� 
	 * @return 
	 */  
	public static Bitmap getimage(String srcPath) {  
	  
	    BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	    // ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��  
	    newOpts.inJustDecodeBounds = true;  
	    Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// ��ʱ����bmΪ��  
	  
	    newOpts.inJustDecodeBounds = false;  
	    int w = newOpts.outWidth;  
	    int h = newOpts.outHeight;  
	    // ���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ  
	    float hh = 800f;// �������ø߶�Ϊ800f  
	    float ww = 480f;// �������ÿ��Ϊ480f  
	    // ���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��  
	    int be = 1;// be=1��ʾ������  
	    if (w > h && w > ww) {// �����ȴ�Ļ����ݿ�ȹ̶���С����  
	        be = (int) (newOpts.outWidth / ww);  
	    } else if (w < h && h > hh) {// ����߶ȸߵĻ����ݿ�ȹ̶���С����  
	        be = (int) (newOpts.outHeight / hh);  
	    }  
	    if (be <= 0)  
	        be = 1;  
	    newOpts.inSampleSize = be;// �������ű���  
	    // ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��  
	    bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
	    return compressImage(bitmap);// ѹ���ñ�����С���ٽ�������ѹ��  
	}

	/** 
	 * bitmapת����file 
	 * 
	 * @param bitmap 
	 * @return 
	 */  
	public void saveBitmapFile(Bitmap bitmap){ 
		File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/filename.jpg");//��Ҫ����ͼƬ��·�� 
		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
		try { 
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file)); 
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); 
		bos.flush(); 
		bos.close(); 
		} catch (IOException e) { 
		e.printStackTrace(); 
		} 
		}
}
