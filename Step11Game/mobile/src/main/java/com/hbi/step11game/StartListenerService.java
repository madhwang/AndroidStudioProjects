package com.hbi.step11game;

import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Wearable Device 에서 보내는 메시지를 항상 리스닝할  Service 만들기
 */
public class StartListenerService extends WearableListenerService{

	//필요한 맴버필드 정의하기
	GoogleApiClient gClient;

	@Override
	public void onCreate() {
		super.onCreate();
		//GoogleApiClient 객체 얻어와서
		gClient=new GoogleApiClient.Builder(this)
			.addApi(Wearable.API)
			.build();
		//바로 연결하기
		gClient.connect();
	}
	//메세지가 도착했을때 호출되는 메소드
	@Override
	public void onMessageReceived(MessageEvent messageEvent) {
		String path = messageEvent.getPath();
		if(path.equals("/startMainActivity")){
			//Intent 객체를 이용해서 MainActivity 를 시작 시킨다.
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}else if(path.equals("/action1")){

		}else if(path.equals("/action2")){

		}
	}
}
