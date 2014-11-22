package com.hbi.step08customview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

/*
나침반 만들기
 */
public class WearActivity extends Activity implements MyUtil.Orientation {

	private CompassView cView;
	private MyUtil.OrientationManager oriManager;

	private GestureDetectorCompat gDector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//화면이 켜진 상태로 유지되도록
		MyUtil.keepScreenOn(this);


		gDector = new GestureDetectorCompat(this,new LongPressListener());

		//CompassView 객체 생성하기
		 cView = new CompassView(this);

		//CompassView 로 화면 구성하기
		setContentView(cView);

		//OrientationManager 의 참조값 얻어오기
		oriManager = MyUtil.OrientationManager.getInstance();


	}

	@Override
	protected void onStart() {
		super.onStart();
		//Context 객체와 Orientation 객체의 참조값 넣어주기
		oriManager.init(this,this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//이벤트 전달하기
		return gDector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
	}

	//방향 센서값이 들어오는 메서드
	@Override
	public void orientationValue(float[] values) {

		//CompassView 에 값을 전달해 준다.
		int heading = (int)values[0];
		int pitch = (int)values[1];
		int rolling = (int)values[2];

		//Compassview에 heading  값을 전달한다.
		cView.setHeading(heading);

		/*
		또는
		//CompassView 에 값을 전달해준다.
		final int heading = (int)values[0];
		int pitch = (int)values[1];
		int rolling = (int)values[2];
		//CompassView 에 heading 값을 전달한다.

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				cView.setHeading(heading);
			}
		});
		 */
	}

	@Override
	public void onStop()
	{
		//센서값 받아오는 작업 해제하기
		oriManager.release();

		//CompassView 의 핸들러 메시지 제거하기
		cView.handler.removeMessages(0);
		super.onStop();
	}

	private class LongPressListener extends GestureDetector.SimpleOnGestureListener{
		@Override
		public void onLongPress(MotionEvent e) {

			//엑티비티를 이동할 인덴트 객체 생성하기
			Intent intent = new Intent(WearActivity.this,SubActivity.class);
			startActivity(intent);
		}
	}
}
