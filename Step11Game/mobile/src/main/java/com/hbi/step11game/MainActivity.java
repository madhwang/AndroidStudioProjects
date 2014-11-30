package com.hbi.step11game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hbi.mylibrary.Util;


public class MainActivity extends Activity {

	DragonView dView;
	static final int SOUND_DIE=0;
	static final int SOUND_LASER=1;
	static final int SOUND_SHOOT =2;

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Util.keepScreenOn(this);

		//DragonView 객체 생성
		 dView = new DragonView(this);

		//화면을 DragonView로 구성한다.
		setContentView(dView);

		//SoundManager  의 참조값을 얻어온다.
		Util.SoundManager soundManager = Util.SoundManager.getInstance();

		//음원 사용할 준비
		soundManager.init(this);

		//음원 로딩하기
		soundManager.addSound(SOUND_DIE,R.raw.birddie);
		soundManager.addSound(SOUND_LASER,R.raw.laser1);
		soundManager.addSound(SOUND_SHOOT,R.raw.shoot1);

	}


	/**
	 * 디바이스의 옵션 메뉴
	 * @param menu
	 * @return
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId())
		{
			case R.id.startGame:

				//DragonView가 동작하도록 메소드를 호출한다.
				dView.startGame();
				break;

			case R.id.pauseGame:
				//DragonView를 일시정지 시킨다.
				dView.pauseGame();
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
