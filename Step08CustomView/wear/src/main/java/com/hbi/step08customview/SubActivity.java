package com.hbi.step08customview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubActivity extends Activity implements  MyUtil.Orientation {

	TextView text_heading, text_pitch, text_rolling;
	MyUtil.OrientationManager oriManager;
	private LinearLayout subLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//화면 구성하기
		setContentView(R.layout.rect_activity_sub);

		//참조값 얻어와서 멤버 필드에 저장하기
		text_heading = (TextView)findViewById(R.id.heading);
		text_pitch = (TextView)findViewById(R.id.pitch);
		text_rolling = (TextView)findViewById(R.id.rolling);
		subLayout = (LinearLayout)findViewById(R.id.subLayout);

		oriManager = MyUtil.OrientationManager.getInstance();

		oriManager.init(this,this);
	}

	@Override
	protected void onStop() {
		oriManager.release();
		super.onStop();
	}

	@Override
	public void orientationValue(float[] values) {

		int heading = (int)values[0];
		int pitch = (int)values[1];
		int rolling= (int)values[2];

		//TextView에 출력해보기
		text_heading.setText("heading : " + heading);
		text_pitch.setText("pitch : " + pitch);
		text_rolling.setText("rolling : " + rolling);

		//기기가 20도 이상 기울었을때 배경색을 빨강색으로
		if(pitch >= 20 || pitch <= -20 || rolling >=20  || rolling <= -20)
		{
			subLayout.setBackgroundColor(Color.RED);
		}
		else
		{
			subLayout.setBackgroundColor(Color.BLACK);
		}
	}
}
