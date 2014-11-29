package com.hbi.step10datalayer2;

import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * HandHeld Device 에서 보내는 메시지를 항상 리스닝할  Service 만들기
 * - WearableListenerService 클래스를 상속 받아서 만든다.
 */
public class DataLayerListenerService extends WearableListenerService {

	//필요한 멤버 필드 정의
	GoogleApiClient gClient;

	@Override
	public void onCreate() {
		super.onCreate();

		gClient = new GoogleApiClient.Builder(this)
					.addApi(Wearable.API)
					.build();

		//바로 연결하기
		gClient.connect();
	}

	/**
	 * 메시지가 도착 했을 때 호출되는 메서드
	 * @param messageEvent
	 */
	@Override
	public void onMessageReceived(MessageEvent messageEvent) {
		String path = messageEvent.getPath();
		if(path.equals("/startWearActivity")) {

			//Intent 객체를 이용해서 WearActivity 를 시작시킨다
			Intent intent = new Intent(this,WearActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}else if(path.equals("/action1"))
		{

		} else if(path.equals("/action2"))
		{

		}
	}
}
