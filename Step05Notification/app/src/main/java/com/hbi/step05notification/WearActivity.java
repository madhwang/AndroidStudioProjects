package com.hbi.step05notification;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

/**
 * 알람 띄우기
 */
public class WearActivity extends Activity {

	private TextView mTextView;

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
	}


	//알람 띄우기 버튼을 눌렀을 때
	public void showNotification(View v)
	{
		//인텐트 전달자 객체 얻어 내기
		PendingIntent p = PendingIntent.getActivity(this,
				MyConstant.NOTI_DETAIL_VIEW,
				new Intent(this,TargetActivity.class), //TargetActivity 클래스를 실행할 인텐트
				PendingIntent.FLAG_UPDATE_CURRENT);

		//알람 객체 만들기
		Notification noti = new NotificationCompat.Builder(this)
				.setContentTitle("알림제목")
				.setContentText("내용내용내용내용내용내용내용내용내용내용내용")
				.setSmallIcon(R.drawable.forest)
				.addAction(R.drawable.forest,"자세히보기",p)
				.setAutoCancel(true)
				.build();

		NotificationManagerCompat.from(this).notify(MyConstant.NOTI_NEW_MESSAGE,noti);

		//종료
		finish();

	}
}
