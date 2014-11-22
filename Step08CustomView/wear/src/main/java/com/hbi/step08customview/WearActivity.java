package com.hbi.step08customview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/*
나침반 만들기
 */
public class WearActivity extends Activity implements MyUtil.Orientation {
	private CompassView cView;
	private MyUtil.OrientationManager oriManager;
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//화면이 켜진 상태로 유지되도록
		MyUtil.keepScreenOn(this);


		//CompassView 객체 생성하기
		 cView = new CompassView(this);

		//CompassView 로 화면 구성하기
		setContentView(cView);

		//OrientationManager 의 참조값 얻어오기
		oriManager = MyUtil.OrientationManager.getInstance();

		//Context 객체와 Orientation 객체의 참조값 넣어주기
		oriManager.init(this,this);
	}

	//방향 센서값이 들어오는 메서드
	@Override
	public void orientationValue(float[] values) {

		//CompassView 에 값을 전달해 준다.
		int heading = (int)values[0];
		int pitch = (int)values[1];
		int rolling = (int)values[2];

	}

	@Override
	public void onStop()
	{
		//센서값 받아오는 작업 해제하기
		oriManager.release();
		super.onStop();
	}
}
