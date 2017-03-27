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
				Bitmap picturecompress=getimage(imageFilePath);//图片压缩
				saveBitmapFile(picturecompress);//转换为路径
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
						Toast.makeText(FindQuestionActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
					
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
	 * 图片质量压缩方法 
	 * 
	 * @param image 
	 * @return 
	 */  
	public static Bitmap compressImage(Bitmap image) {  
	  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    image.compress(Bitmap.CompressFormat.JPEG, 10, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中     
	    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中  
	    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片  
	    return bitmap;  
	}  
	
	/** 
	 * 图片按比例大小压缩方法 
	 * 
	 * @param srcPath （根据路径获取图片并压缩） 
	 * @return 
	 */  
	public static Bitmap getimage(String srcPath) {  
	  
	    BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	    // 开始读入图片，此时把options.inJustDecodeBounds 设回true了  
	    newOpts.inJustDecodeBounds = true;  
	    Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空  
	  
	    newOpts.inJustDecodeBounds = false;  
	    int w = newOpts.outWidth;  
	    int h = newOpts.outHeight;  
	    // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
	    float hh = 800f;// 这里设置高度为800f  
	    float ww = 480f;// 这里设置宽度为480f  
	    // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
	    int be = 1;// be=1表示不缩放  
	    if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放  
	        be = (int) (newOpts.outWidth / ww);  
	    } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放  
	        be = (int) (newOpts.outHeight / hh);  
	    }  
	    if (be <= 0)  
	        be = 1;  
	    newOpts.inSampleSize = be;// 设置缩放比例  
	    // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
	    bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
	    return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩  
	}

	/** 
	 * bitmap转换成file 
	 * 
	 * @param bitmap 
	 * @return 
	 */  
	public void saveBitmapFile(Bitmap bitmap){ 
		File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/filename.jpg");//将要保存图片的路径 
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
