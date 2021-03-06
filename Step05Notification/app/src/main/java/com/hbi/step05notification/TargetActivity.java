package com.hbi.step05notification;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

public class TargetActivity extends Activity {

	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_target);
		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				mTextView = (TextView) stub.findViewById(R.id.text);
			}
		});

		// id가 MyConstant.NOTI_NEW_MESSAGE 인 노티를 제거한다.
		NotificationManagerCompat.from(this).cancel(MyConstant.NOTI_NEW_MESSAGE);
	}
}
