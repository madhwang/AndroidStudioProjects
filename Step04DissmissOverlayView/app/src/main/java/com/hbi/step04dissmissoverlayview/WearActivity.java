package com.hbi.step04dissmissoverlayview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.wearable.view.DismissOverlayView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/*
WatchViewStub 을 쓰지 않기
 */
public class WearActivity extends Activity {

    private TextView mTextView;


    //제스쳐를 처리하기 위한 객채를 멤버 필드에 담기 위해
    GestureDetectorCompat gDetector;

	DismissOverlayView dismissView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // res  > layout > activity_wear2.xml  을 전개해서 화면 구성하기
        setContentView(R.layout.activity_wear2);


		//DismissOverlayView 의 참조값을 맴버필드에 저장한다.
		dismissView = (DismissOverlayView) findViewById(R.id.overlay);

        /*
        this 파라메터는 android.content.Context 타입으로서
        이 클래스의 부모 클래스인 Activity의 부모 클래스가 Context 이다.
         */
        gDetector = new GestureDetectorCompat(this,new LongPressListener());
    }

    //액티비티가 MotionEvent 객체를 받는다.
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //GestureDetectorCompat 객체에 MotionEvent 객체를 전달한다.
        return gDetector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }


    //버튼을 눌렀을 때 호출되는 메서드
    public void moveActivity(View v)
    {

    }

    //SimpleOneGestureListener 클래스를 상속 받아서 클래스를 정의한다.
    class LongPressListener extends GestureDetector.SimpleOnGestureListener
    {
        //ctrl + o 키를 누르면  오버라이드 메소드 목록이 나온다.

		//길게 화면을 눌렀다면
        @Override
        public void onLongPress(MotionEvent e) {
			//DismissOverlayView 를 보이도록 한다.
			dismissView.show();
        }
    }
}
