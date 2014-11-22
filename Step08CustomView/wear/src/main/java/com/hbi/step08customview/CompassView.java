package com.hbi.step08customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * [ Custom View 만들기 ]
 * 1.  View 클래스를 상속 받는다.
 * 2. 생성자를 적절하게 정의한다.
 * 3. onDraw() 메소드를 오버라이딩해서 필요한 작업을 한다.
 *
 */
public class CompassView extends View {

	//나침반 이미지를 저장할 멤버 필드
	Bitmap compassImg;

	//화면의 폭과 높이를 저장할 멤버 필드
	int width,height;

	//생성자의 인자로 Context 객체를 받아서 부모 생성자에 넘겨주는 생성자 정의하기
	public CompassView(Context context) {
		super(context);
	}

	@Override
	protected  void onSizeChanged(int w,int h,int oldw, int oldh)
	{
		super.onSizeChanged(w,h,oldw,oldh);

		//멤버 필드에 초기값 전달
		width = w;
		height = h;
		//초기화 메서드 호출
		init();
	}

	//초기화 메서드
	public void init()
	{
		//이미지를 읽어 들인다.
		Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.compass);

		//이미지의 크기 스케일링 하기(화면의 폭과 높이에 맞도록)
		compassImg = Bitmap.createScaledBitmap(img,width,height, false);
	}

	//View 를 렌더링하는 메소드
	@Override
	protected void onDraw(Canvas canvas) {
		//인자로 전달되는 Canvas 객체를 이용해서 뷰를 구성한다.
		canvas.drawColor(Color.GREEN);
		canvas.drawBitmap(compassImg,0,0,null);
	}
}
