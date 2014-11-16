package com.hbi.step04dissmissoverlayview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * [ Activity 클래스 만드는 방법 ]
 *
 * 1. Activity 클래스를 extends 한다.
 * 2. Activity  클래스의 onCreate() 메서드를 오버라이드 한다.
 * 3. setContentView()  메서드를 이용해서 화면 레이 아웃을 구성한다.
 *
 */
public class SubActivity extends Activity { // 1

	//2
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//3
		setContentView(R.layout.activity_wear3);
	}

	//버튼을 눌렀을 때 호출되는 메서드
	public void end(View v)
	{
		//현재 activity  종료하기
		finish();

	}

}
