package com.hbi.step08customview;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class TouchActivity extends Activity {

	//필요한 멤버필드 정의하기
	TextView text_name, text_x, text_y;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rect_activity_touch);


		//객체의 참조값을 얻어와서 멤버 필드에 저장한다.
		text_name = (TextView)findViewById(R.id.eventName);
		text_x  = (TextView)findViewById(R.id.x);
		text_y = (TextView)findViewById(R.id.y);
	}
	/**
	 * 터치 이벤트 받기
	 * @param event
	 * @return
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		String eventName = "";
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				eventName ="액션 다운!";
				break;

			case MotionEvent.ACTION_MOVE:
				eventName ="액션 무브!";
				break;

			case MotionEvent.ACTION_UP:
				eventName ="액션 업!";
				break;

		}
		//이벤트가 발생한 곳의 좌표
		int eventX  = (int)event.getX();
		int eventY = (int)event.getY();

		//정보 출력하기
		text_name.setText(eventName);
		text_x.setText("x : " + eventX);
		text_y.setText("y :" + eventY);
		/*
		return true 이면 action down, action move, action up 모두 받는다.
		return false 이면 action down 이벤트만 받는다.
		 */
		return true;
	}
}
