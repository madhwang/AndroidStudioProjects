package com.hbi.step07gridviewpager;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.ImageReference;
import android.view.GestureDetector;
import android.view.MotionEvent;


/*
    GridViewPager는  pager를 GridViewPagerAdapter로 부터 공급받는다.

    GridViewPager : 사용자 터치에 반응하여 어느 영역이 보여져야 하는지 파악
    GridViewPagerAdapter : 보여줘야할 데이터(Model)를 fragement 로 만들고
                                     GridViewPager 로 부터 요청 받은 영역에 해당하는
                                     데이터인 fragment를 GridViewPager에게 전달한다.
 */
public class WearActivity extends Activity {

	//컬럼과 로우의 개수
	int rows = 3;
	int cols = 3;

	GridViewPager mPager;
	MyGridAdapter mAdapter;

	GestureDetectorCompat gDector;

	DismissOverlayView dismissView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


/*
		// activity_wear 를 사용해서 화면을 구성하는 경우
		setContentView(R.layout.activity_wear);
		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		gDector = new GestureDetectorCompat(this,new LongPressListener());

		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {

				//필요한 객체의 참조값
				mPager = (GridViewPager) findViewById(R.id.fragment_container);

				//생성자의 인자로 FragmentManager 객체를 전달하면서 객체 생성하기
				mAdapter = new MyGridAdapter(getFragmentManager());

				//페이저와 어댑터 연결
				mPager.setAdapter(mAdapter);

				dismissView = (DismissOverlayView)findViewById(R.id.overlay);

			}
		});

*/

		/*
		rect_activity_wear 를 사용해서 화면을 구성하는 경우
		 */
		setContentView(R.layout.rect_activity_wear);
		gDector = new GestureDetectorCompat(this,new LongPressListener());

		//필요한 객체의 참조값
		mPager = (GridViewPager) findViewById(R.id.fragment_container);

		//생성자의 인자로 FragmentManager 객체를 전달하면서 객체 생성하기
		mAdapter = new MyGridAdapter(getFragmentManager());

		//페이저와 어댑터 연결
		mPager.setAdapter(mAdapter);

		dismissView = (DismissOverlayView)findViewById(R.id.overlay);
	}

	//커스텀 어댑터 객체를 만들 클래스 정의하기
	//FragmentGridPagerAdapter 추상클래스를 상속 받는다.
	class MyGridAdapter extends FragmentGridPagerAdapter{

		//생성자.
		public MyGridAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public ImageReference getBackground(int row, int column) {
			//return super.getBackground(row, column);
			switch (row) {
				case 0:
					return ImageReference.forDrawable(R.drawable.image1);
				case 1:
					return ImageReference.forDrawable(R.drawable.image2);
				case 2:
					return ImageReference.forDrawable(R.drawable.image3);
				default:
					return ImageReference.forDrawable(R.drawable.image1);
			}
		}

		//파라메터 값에 해당하는 행,열의 값을 Fragment 로 만들어 리턴한다.
		@Override
		public Fragment getFragment(int row, int col) {
			//해당 행과 열에 해당하는 Fragment 객체를 리턴해준다.
			return ContentFragment.newInstance(row,col);
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

	//CardFragment 클래스를 상속받은 클래스를 정의한다.
	 public static class ContentFragment extends CardFragment{
		//행과 열을 전달 받아서 해당 Fragment 객체를 만들어서 리턴해 주는 메서드
		public static ContentFragment newInstance(int row, int col)
		{
			//Fragment에 전달한 데이터를 Bundle 객체에 담는다.
			//Bundle 의 사용은 CardFragment 와 약속된 내용.
			Bundle args = new Bundle();
			args.putString(CardFragment.KEY_TITLE,"행 : " + row ); //CardFragment 의 제목
			args.putString(CardFragment.KEY_TEXT, " 열: " + col); //CardFragment 의 내용

			//Fragment 객체를 생성한다.
			ContentFragment frag = new ContentFragment();

			//인자를 전달한다.
			frag.setArguments(args);

			//생성한 Fragment 객체를 리턴해준다.
			return frag;
		}
	}


	//터치 이벤트를 잡는다.
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return gDector.onTouchEvent(ev) || super.dispatchTouchEvent(ev);
	}



	//길게 눌렀을 때 실행되는 클래스
	class LongPressListener extends GestureDetector.SimpleOnGestureListener{
		@Override
		public void onLongPress(MotionEvent e) {
			dismissView.show();
		}
	}
}
