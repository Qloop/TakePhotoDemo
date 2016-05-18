package com.upc.takephoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Explorer on 2016/5/18.
 */
public class TakePhotoActivity extends Activity {

	private ImageView mThumbnail;
	private TextView mPath;
	private Button mBtnTakePhoto;
	private static final int CAMERA_REQUEST_CODE = 1;
	private File img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		mThumbnail = (ImageView) findViewById(R.id.iv_thumbil);
		mPath = (TextView) findViewById(R.id.tv_path);
		mBtnTakePhoto = (Button) findViewById(R.id.btn_take_photo);

		mBtnTakePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA_REQUEST_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST_CODE) {
			if (data == null) {
				return;
			} else {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap bm = extras.getParcelable("data");
					mThumbnail.setImageBitmap(bm);
					Uri uri = saveBitmap(bm);
					mPath.setText(img.getPath());
//					startImageZoom(uri);
				}
			}
		}
	}

	private Uri saveBitmap(Bitmap bm) {
		File tmpDir;
		if (hasSD()) {
			tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.upc.avatar");
		} else {
			tmpDir = new File(Environment.getDataDirectory() + "/com.upc.avatar");
		}

		if (!tmpDir.exists()) {
			tmpDir.mkdir();
		}
		img = new File(tmpDir.getAbsolutePath() + "avater.png");
		try {
			FileOutputStream fos = new FileOutputStream(img);
			bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			return Uri.fromFile(img);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

//	private Uri convertUri(Uri uri) {
//		InputStream is = null;
//		try {
//			is = getContentResolver().openInputStream(uri);
//			Bitmap bitmap = BitmapFactory.decodeStream(is);
//			is.close();
//			return saveBitmap(bitmap);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

//	private void startImageZoom(Uri uri) {
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		intent.putExtra("crop", "true");
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		intent.putExtra("outputX", 150);
//		intent.putExtra("outputY", 150);
//		intent.putExtra("return-data", true);
//		startActivityForResult(intent, CROP_REQUEST_CODE);
//	}

	/**
	 * 是否有SD卡
	 */
	private boolean hasSD() {
		//如果有SD卡 则下载到SD卡中
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;

		} else {
			//如果没有SD卡
			return false;
		}
	}
}
