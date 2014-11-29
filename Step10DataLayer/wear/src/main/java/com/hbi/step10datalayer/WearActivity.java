package com.hbi.step10datalayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

public class WearActivity extends Activity
	implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,MessageApi.MessageListener
{

	private TextView mTextView;

	//GoogleApiClient 객체
	GoogleApiClient gClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wear);
		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				mTextView = (TextView) stub.findViewById(R.id.text);
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
		super.onStop();
		gClient.disconnect();
	}

	/**
	 * GoogleApiClient에 연결 되었을때 호출되는 메서드
	 * @param bundle
	 */
	@Override
	public void onConnected(Bundle bundle) {
		//메시지 도착 리스너를 등록한다.
		Wearable.MessageApi.addListener(gClient,this);
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}

	/**
	 * Message가 도착했을때 호출되는 메서드
	 */
	@Override
	public void onMessageReceived(MessageEvent messageEvent) {
		//전달된 Path(메시지,문자열)을 읽어온다.
		String msg = messageEvent.getPath();
		Log.e("WearActivity","전달된 문자열 :"+msg);
	}
}
