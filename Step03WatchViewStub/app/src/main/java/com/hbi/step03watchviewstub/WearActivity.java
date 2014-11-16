package com.hbi.step03watchviewstub;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WearActivity extends Activity {

    private TextView mTextView;

    //레이아웃의 참조값을 담은 멤버 필드
    RelativeLayout rectLayout;
    RelativeLayout roundLayout;

    // 액티비티가 활성화 될때 한번 호출되는 메서드
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  res / layoyt / activity_wear.xml 를 이용해서 화면구성하기
        setContentView(R.layout.activity_wear);

        //WatchView stub 객체의 참조값 얻어오기
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        /*
        WatchViewStub  객체가 디바이스의 모양을 감지해서 레이아웃을 전개한 직후 호출되는
        리스터 등록
         */
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            //레이아웃이 전개 되었을 때 호출되는 메서드
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                rectLayout = (RelativeLayout)findViewById(R.id.rect_layout);
                /*
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText("수정하였습니다.");
                */
            }
        });
    }


    //레이아웃을 클릭했을 때 호출되는 메소드
    public void onLayoutClicked(View v)
    {
        if(rectLayout != null) //사각형 레이아웃이 설정 되었으면.
        {
            ScaleAnimation ani = new ScaleAnimation(1.0f,0.7f,1.0f,0.7f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_PARENT,0.5f);

            ani.setDuration(300);
            ani.setRepeatCount(1);
            ani.setRepeatMode(Animation.REVERSE);
            rectLayout.startAnimation(ani);

        }

    }
}
