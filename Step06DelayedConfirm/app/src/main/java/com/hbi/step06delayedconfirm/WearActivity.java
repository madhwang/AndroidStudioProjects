package com.hbi.step06delayedconfirm;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DelayedConfirmationView;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

public class WearActivity extends Activity implements DelayedConfirmationView.DelayedConfirmationListener {

	private TextView mTextView;
	DelayedConfirmationView confirmView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wear);
		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				mTextView = (TextView) stub.findViewById(R.id.text);
				confirmView = (DelayedConfirmationView)findViewById(R.id.delayed_confirm);
				confirmView.setListener(WearActivity.this);
			}
		});
	}



	//버튼을 눌렀을 때 호출되는 메서드
	public void showConfirm(View v)
	{
		//2초 지연시키기
		confirmView.setTotalTimeMs(2000);

		//DelayedConfirmView 활성화 시키기
		confirmView.start();

	}

	//타이머가 그냥 종료 되었을때 호출되는 메서드
	@Override
	public void onTimerFinished(View view) {
		mTextView.setText("타이머 종료");
	}


	//타이머를 클릭했을때 호출되는 메소드
	@Override
	public void onTimerSelected(View view) {
		mTextView.setText("타이머 클릭");
	}
}
