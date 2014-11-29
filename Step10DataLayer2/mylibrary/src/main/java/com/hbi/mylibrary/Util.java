package com.hbi.mylibrary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lee on 2014-11-29.
 */
public class Util {
	//화면을 켜진 체로 유지해주는 메소드
	public static void keepScreenOn(Activity activity){
		activity.getWindow()
			.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	//Bitmap 객체를 Asset 객체로 디코딩 해서 리턴해주는 메소드
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
	//Asset 객체를 Bitmap 객체로 바꿔주는 메소드
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
}