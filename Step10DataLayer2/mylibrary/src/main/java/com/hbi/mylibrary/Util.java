package com.hbi.mylibrary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 각종 유틸
 */
public class Util {

	/**
	 * 화면을 켜진 체로 유지해주는 메소드
	 * @param activity
	 */
	public static void keepScreenOn(Activity activity){
		activity.getWindow()
			.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	/**
	 * Bitmap 객체를 Asset 객체로 디코딩 해서 리턴해주는 메소드
	 * @param bitmap
	 * @return
	 */
	public static Asset bitmapToAsset(Bitmap bitmap) {
		ByteArrayOutputStream byteStream = null;

		try {
			byteStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
			return Asset.createFromBytes(byteStream.toByteArray());
		} finally {
			if (null != byteStream) {
				try {
					byteStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * Asset 객체를 Bitmap 객체로 바꿔주는 메소드
	 * @param gClient
	 * @param asset
	 * @return
	 */
	public static Bitmap assetToBitmap(GoogleApiClient gClient, Asset asset){
		if (asset == null) {
			throw new IllegalArgumentException("Asset must be non-null");
		}

		InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
			gClient, asset).await().getInputStream();

		if (assetInputStream == null) {
			return null;
		}
		return BitmapFactory.decodeStream(assetInputStream);
	}


	/**
	 * 겔러리에서 사진 선택하는 요청
	 * @param activity
	 * @param requestCode
	 */
	public static void startActivityForSelectPhoto	(Activity activity,int requestCode){
		//겔러리에서 사진을 선택하려고 하는 인텐트 객체 작성하기
		Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		//결과값을 받아올수 있는 액티비티를 시작 시킨다.
		activity.startActivityForResult(intent, requestCode);
	}


	/**
	 * 썸네일 사진을 가져오는 메서드
	 * @param activity
	 * @param requestCode
	 */
	public static void startActivityForThumbnailPhoto(Activity activity,int requestCode){
		Intent intent=new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

		//위의 인텐트를 처리할수 있는 app 이 존재한다면
		if(intent.resolveActivity(activity.getPackageManager())!=null){
			//결과를 받아올수 있는 액티비티를 시작 시킨다.
			activity.startActivityForResult(intent, requestCode);
		}
	}

	/**
	 * 사진을 찍어서 원본 크기의 이미지를 가지고 오는 메서드
	 * @param activity
	 * @param requestCode
	 * @return
	 */
	public static String startActivityForOriginalPhoto(Activity activity,int requestCode){
		Intent intent=new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		String absolutePath=null;

		if(intent.resolveActivity(activity.getPackageManager())!=null){

			//이미지를 저장할 파일 객체를 얻어온다.
			File photoFile=null;

			try{
				//이미지 파일의 이름을 얻어낸다.
				String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
				String imageFileName="JPEG_"+timeStamp+"_";

				//외부 저장장치에 저장할수 있는 File 객체(디렉토리)를 얻어온다.
				File storageDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

				//이미지 데이터를 저장할 임시파일 객체를 얻어온다.
				photoFile=File.createTempFile(imageFileName, ".jpg", storageDir);
			}catch(IOException e){
				Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			if(photoFile!=null){
				//파일을 저장할 Uri 객체를 얻어온다.
				Uri uri=Uri.fromFile(photoFile);

				//원본 이미지 파일을 저장할 uri 객체를 인텐트에전달
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

				//결과를 받을수 있는 액티비티 시작 시키기
				activity.startActivityForResult(intent, requestCode);
				absolutePath=photoFile.getAbsolutePath();
			}
		}
		return absolutePath;
	}

	/**
	 * 이미지뷰 크기에 맞게 이미지를 스케일링해서 출력하는 메서드
	 * @param imageView //이미지뷰 객채
	 * @param absolutePath //절대 경로
	 */
	public static void fitToImageView(ImageView imageView, String absolutePath){
		//출력할 이미지 뷰의 크기를 얻어온다.
		int targetW = imageView.getWidth();
		int targetH = imageView.getHeight();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(absolutePath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeFile(absolutePath, bmOptions);
		imageView.setImageBitmap(bitmap);
	}

	/**
	 * 이미지를 갤러리에서도 볼 수 있도록 하는 메서드
	 * @param activity
	 * @param absolutePath
	 */
	public static void galleryAddPic(Activity activity, String absolutePath){
		Intent intent=new Intent();

		//사진이 검색 될수 있도록 액션 설정
		intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f=new File(absolutePath);
		Uri uri=Uri.fromFile(f);
		intent.setData(uri);

		//인텐트 객체를 인자로 전달해서 방송하기
		activity.sendBroadcast(intent);
	}
}