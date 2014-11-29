package com.hbi.step10datalayer2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.hbi.mylibrary.Util;

import java.util.List;

public class WearActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,DataApi.DataListener{


	ImageView imageView;
	EditText console;
	GoogleApiClient gClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wear);
		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				//필요한 객체의 참조값 얻어오기
				imageView = (ImageView)findViewById(R.id.imageView);
				console = (EditText)findViewById(R.id.console);
			}
		});

		gClient = new GoogleApiClient.Builder(this)
			.addApi(Wearable.API)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this)
			.build();
	}


	@Override
	protected void onResume() {
		super.onResume();
		gClient.connect();
	}

	@Override
	protected void onStop() {
		Wearable.DataApi.removeListener(gClient,this);
		gClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnected(Bundle bundle) {
		//Wearable.DataApi 리스너 객체를 등록한다.
		Wearable.DataApi.addListener(gClient,this);
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	/**
	 * DataApi를 통해서 들어온 데이터에 변화가 생겼을때 호출하는 메서드
	 * @param dataEvents
	 */
	@Override
	public void onDataChanged(DataEventBuffer dataEvents) {
		//Data 목록 객체를 얻어온다.
		List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
		dataEvents.close();

		//반복문 돌면서 DataEvent 객체를 하나씩 불러온다
		for(DataEvent event : events)
		{
			switch (event.getType()){
				case DataEvent.TYPE_CHANGED:
					//path를 얻어온다. "/sendPhoto"
					String path = event.getDataItem().getUri().getPath();
					if(path.equals("/sendPhoto"))
					{
						//DataMapItem 객체를 얻어온다.
						DataMapItem item = DataMapItem.fromDataItem(event.getDataItem());

						//"image" 라는 키값으로 담긴 Asset 객체를 얻어온다.
						Asset photo = item.getDataMap().getAsset("image");

						//Util 을 이용해서 Asset 을 Bitmap 으로 얻어온다.
						final Bitmap imageBitmap = Util.assetToBitmap(gClient, photo);
						//여기는 UI 스레드가 아니므로 View 에 출력하려면
						//UI 스레드에서 작업하도록 한다.
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								//ImageView 에 출력하기
								imageView.setImageBitmap(imageBitmap);
							}
						});
					}
					break;
				case DataEvent.TYPE_DELETED:

					break;

				default:
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}
}
