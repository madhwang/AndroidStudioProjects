package com.hbi.step07gridviewpager;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;

public class WearActivity extends Activity {

	//컬럼과 로우의 개수
	int rows = 3;
	int cols = 3;

	GridViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wear);
		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {

				//필요한 객체의 참조값
				mPager = (GridViewPager) findViewById(R.id.fragment_container);
			}
		});
	}

	//커스텀 어댑터 객체를 만들 클래스 정의하기
	//FragmentGridPagerAdapter 추상클래스를 상속 받는다.
	class MyGridAdapter extends FragmentGridPagerAdapter{

		//생성자.
		public MyGridAdapter(FragmentManager fm) {
			super(fm);
		}

		//파라메터 값에 해당하는 행,열의 값을 Fragment 로 만들어 리턴한다.
		@Override
		public Fragment getFragment(int row, int col) {
			return null;
		}


		//열의 개수를 리턴하는 메서드
		@Override
		public int getRowCount() {
			return rows;
		}


		//행의 개수를 리턴하는 메서드
		@Override
		public int getColumnCount(int i) {
			return cols;
		}
	}
}
