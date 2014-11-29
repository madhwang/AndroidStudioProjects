package com.hbi.step10datalayer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.hbi.mylibrary.Util;

public class WearActivity extends Activity
	implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,MessageApi.MessageListener
{

	//GoogleApiClient 객체
	GoogleApiClient gClient;
	EditText console;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wear);

		//화면이 켜진 상태로 유지하기
		Util.keepScreenOn(this);

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				//EditText 값 받아오기
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
		//액티비티가 비활성화 될때 마무리 작업을 한다.
		gClient.disconnect();
		Wearable.MessageApi.removeListener(gClient, this);

		super.onStop();
	}

	/**
	 * GoogleApiClient에 연결 되었을때 호출되는 메서드
	 * @param bundle
	 */
	@Override
	public void onConnected(Bundle bundle) {
		printLog("onConnected()");
		//메시지 도착 리스너를 등록한다.
		Wearable.MessageApi.addListener(gClient,this);
	}

	@Override
	public void onConnectionSuspended(int i) {
		printLog("onConnectionSuspended()");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		printLog("onConnectionFailed()");
	}

	/**
	 * Message가 도착했을때 호출되는 메서드
	 */
	@Override
	public void onMessageReceived(MessageEvent messageEvent) {
		//전달된 Path(메시지,문자열)을 읽어온다.
		String msg = messageEvent.getPath();
		Log.e("WearActivity","전달된 문자열 :"+msg);

		//EditText에 출력하기
		printLog(msg);
	}

	//로그를 찍는 메소드
	public void printLog(String msg){
		//출력할 문자열을 메세지 객체에 담는다.
		Message m=new Message();
		m.obj = msg;
		//핸들러에 메세지를 보낸다.
		handler.sendMessage(m);
	}

	//핸들러 객체 정의하기
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			//전달된 문자열을 얻어온다.
			String str = (String)msg.obj;
			//EditText 에 개행기호와 함께 출력하기
			console.append(str+"\n");
		}
	};

}
