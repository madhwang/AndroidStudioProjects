package com.hbi.mylibrary;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by user on 2014-11-29.
 */
public class Util {

	//화면을 켜진채로 유지하는 메서드
	public static void keepScreenOn(Activity activity)
	{
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
}
