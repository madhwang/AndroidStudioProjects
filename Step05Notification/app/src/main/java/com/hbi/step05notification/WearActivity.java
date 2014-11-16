package com.hbi.step05notification;

import android.app.Activity;
import android.app.Notification;
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
		Notification noti = new NotificationCompat.Builder(this)
				.setContentTitle("알림제목")
				.setContentText("내용...")
				.setSmallIcon(R.drawable.ic_launcher)
				.setAutoCancel(true)
				.build();

		NotificationManagerCompat.from(this).notify(1,noti);

		finish();

	}
}
