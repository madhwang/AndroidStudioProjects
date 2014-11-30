package com.hbi.step11game;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

public class WearActivity extends Activity  implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {


	private TextView mTextView;
	GoogleApiClient gClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wear);

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
			}
		});

		//GoogleApiClient 객체값 가져오기
		gClient = new GoogleApiClient.Builder(this)
			.addApi(Wearable.API)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this)
			.build();

	}

	/**
	 * 왼쪽으로 이동시키는 버튼을 눌렀을 때
	 * @param v
	 */
	public void moveLeft(View v) {
		this.sendData("/left");
	}

	/**
	 * 오른쪽으로 이동시키는 버튼을 눌렀을 때
	 * @param v
	 */
	public void moveRight(View v){
		this.sendData("/right");
	}

	/**
	 * HandHeld 기기에 데이터를 전송하는 메서드
	 */
	public void sendData(String path)
	{
		//PutDataMapRequest객체 얻어오기
		PutDataMapRequest dataMap = PutDataMapRequest.create(path);
		//데이터에 항상 변화를 줄 수 있도록 현재 시간을 담는다. 넣어주지 않으면 여러번 눌러도 한번만 전송된다.
		dataMap.getDataMap().putLong("time",new Date().getTime());

		//PutDataRequest  객체를 얻어온다
		PutDataRequest request = dataMap.asPutDataRequest();

		//Wearable.DataApi 객체를 이용해서 전송한다
		Wearable.DataApi.putDataItem(gClient, request);
	}

	@Override
	protected void onResume() {
		super.onResume();
		gClient.connect();
	}

	@Override
	protected void onStop() {
		gClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnected(Bundle bundle) {

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}
}
