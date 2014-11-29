package com.hbi.step10datalayer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.os.Handler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

	EditText console;

	//GoogleApiClient 객체를 멤버필드에 담기 위해
	GoogleApiClient gClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//EditText의 참조값을 얻어와서 멤버 필드에 저장하기
		console = (EditText)findViewById(R.id.inputText);

		//GoogleApiClient 객체를 생성해서 멤버필드에 담기
		gClient = new GoogleApiClient.Builder(this)
							.addApi(Wearable.API)
							.addConnectionCallbacks(this)
							.addOnConnectionFailedListener(this)
							.build();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//GoogleApiClient에 연결 요청을 한다.
		gClient.connect();
	}

	@Override
	protected void onStop() {
		//연결 해제
		gClient.disconnect();
		super.onStop();
	}

	/**
	 * 전송 버튼을 눌렀을때 호출되는 메서드
	 * @param v
	 */
	public void send(View v)
	{
	}


	public void printLog(String msg)
	{
		//출력할 문자열을 메시지 객체에 담는다
		Message m = new Message();
		m.obj = msg;

		//핸들러에 메시지를 보낸다
		handler.sendMessage(m);

	}

	/**
	 * 핸들러 객체 정의
	 */
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			// 전달된 문자열을 읽어온다.
			 String str = (String)msg.obj;
			//EditText 에 개행기호와 함께 출력하기
			console.append(str+'\n');
		}
	};


	/**
	 * GoogleApiClient에 연결이 되었을때 호출되는 메서드
	 * @param bundle
	 */
	@Override
	public void onConnected(Bundle bundle) {
		printLog("onConnected()");
	}

	/**
	 * GoogleApiClient에 연결이 연기 혹은 지연되었을때 호출되는 메서드
	 * @param i
	 */
	@Override
	public void onConnectionSuspended(int i) {
		printLog("onConnectionSuspended()");
	}

	/**
	 * GoogleApiClient에 연결이 실패 되었을때 호출되는 메서드
	 * @param connectionResult
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		printLog("onConnectionFailed()");
	}
}
