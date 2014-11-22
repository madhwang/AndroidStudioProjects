package com.hbi.step08customview;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by user on 2014-11-22.
 */
public class MyUtil {

	//화면 꺼지지 않고 유지 되도록 하는 메소드
	public static void keepScreenOn(Activity activity){
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	/*
	 *  [ 방향센서값을 얻어오기 위한 클래스 ]
	 *
	 *  1. 방향센서값을 받을 클래스에서 Util.Orientation 인터페이스를 구현한다.
	 *  2. 센서값을 받을 orientationValue() 메소드를 오버라이딩한다.
	 *  3. Util.OrientationManager.getInstance() 해서 참조값을 얻어온다.
	 *  4. init() 메소드 호출하면서 Context 객체와 Orientation 객체를 전달한다.
	 *  5. orientationValue() 메소드에 인자로 전달되는 float[] 객체에 방향센서값이
	 *     들어있다
	 *     -헤딩 : values[0]
	 *     -피치 : values[1]
	 *     -롤링 : values[2]
	 *  6. 더이상 센서값을 얻어올 필요가 없을때 relase() 메소드를 호출해준다.
	 */
	//방향 센서 값을 받아오기 위한 인터페이스
	public static interface Orientation{
		public void orientationValue(float[] values);
	}
	public static class OrientationManager implements SensorEventListener {

		private static OrientationManager oManager;

		private SensorManager mSensorManager;
		private Sensor mAccelerometer;
		private Sensor mMagnetometer;

		private float[] mLastAccelerometer = new float[3];
		private float[] mLastMagnetometer = new float[3];
		private boolean mLastAccelerometerSet = false;
		private boolean mLastMagnetometerSet = false;

		private float[] mR = new float[9];
		private float[] mOrientation = new float[3];

		private Orientation orientation;
		//생성자.
		private OrientationManager(){}
		//참조값 리턴하는 메소드
		public static OrientationManager getInstance(){

			oManager=new OrientationManager();

			return oManager;
		}
		//초기화 메소드
		public void init(Context context, Orientation orientation){
			this.orientation=orientation;
			mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
			mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
			mLastAccelerometerSet = false;
			mLastMagnetometerSet = false;
			mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
			mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
		}
		//리스너 해제 메소드
		public void release(){
			mSensorManager.unregisterListener(this);
		}
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor == mAccelerometer) {
				System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
				mLastAccelerometerSet = true;
			} else if (event.sensor == mMagnetometer) {
				System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
				mLastMagnetometerSet = true;
			}
			if (mLastAccelerometerSet && mLastMagnetometerSet) {
				SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
				SensorManager.getOrientation(mR, mOrientation);
				Log.i("OrientationTestActivity", String.format("Orientation: %f, %f, %f",
					mOrientation[0], mOrientation[1], mOrientation[2]));
				float heading=(float)(mOrientation[0]*(180/Math.PI));
				if(heading<0){
					heading=360+heading;
				}
				float pitch=(float)(mOrientation[1]*(180/Math.PI));
				float rolling=(float)(mOrientation[2]*(180/Math.PI));

				/*
				headinig - 방향값. 북쪽은 0 또는 359
				pitch - 장비의 상하 기울기
				rolling - 장비의 좌우 기울기
				 */
				float[] values={heading,pitch,rolling };

				orientation.orientationValue(values);
			}
		}

	}
}
