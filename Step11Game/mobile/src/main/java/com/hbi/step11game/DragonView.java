package com.hbi.step11game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * CustomView 만들기
 *
 * 1. View 클래스를 상속 받는다.
 * 2. 생성자를 정의한다.
 * 3. onDraw()  메소드를 재정의해서 화면을 구성한다
 */
public class DragonView extends View {

	int viewWidth, viewHeight; //화면의 폭과 높이
	Bitmap backImg; //배경 이미지

	int back1Y , back2Y; //배경 이미지들의 Y좌표

	//배경 이미지가 스크롤 되는 속도
	int scrollSpeed;

	Bitmap[] ships = new Bitmap[2]; //드래곤의 이미지 2개를 담을 배열
	int shipIndex; //드래곤의 이미지 인덱스를 담을 변수
	int shipX, shipY; //드래곤의 좌표
	int shipW,shipH; //드래곤의 폭과 높이

	//드래곤의 지난 X좌표
	int lastX ;

	int count; //카운트를 셀 변수

	//Missile 객체를 담을 가변 배열 객체 생성하기
	ArrayList<Missile> missList=new ArrayList<Missile>();
	Bitmap missImg; //미사일 이미지
	int missW, missH; //미사일의 폭과 높이
	int missSpeed; //미사일의 속도


	public DragonView(Context context) {
		super(context);
	}

	public DragonView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * View를 렌더링하는 메서드
	 * @param canvas
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		//draw 호출횟수
		count++;

		//인자로 전달되는 Canvas 객체를 이용해서 렌더링한다.
		canvas.drawBitmap(backImg,0,back1Y,null);
		canvas.drawBitmap(backImg,0,back2Y,null);

		//미사일 그리기
		for(Missile tmp: missList)
		{
			canvas.drawBitmap(missImg,tmp.getX(),tmp.getY() ,null);
		}

		//드래곤의 이미지를 보여준다.
		canvas.drawBitmap(ships[shipIndex],shipX,shipY,null);


		backScroll(); //배경 이미지 스크롤 하는 메서드 호출

		shipAnimation(); //드래곤 이미지 처리 메서드

		makeMissile(); //미사일 만드는 메서드

		moveMissile();//미사일 발사 메서드

		checkMissile();//미사일 체크
	}

	/**
	 * 배경 이미지 스크롤 하는 메서드
	 */
	public void backScroll(){
		//증가할 픽셀단위
		back1Y += scrollSpeed ;
		back2Y += scrollSpeed;

		//배경 1의 좌표가 한계점에 도달했을 때 배경 1을 맨위로 보낸다
		if(back1Y >= viewHeight)
		{
			back1Y = -viewHeight;
			back2Y = 0;
		}

		//배경 2의 좌표가 한계점에 도달했을 때 배경 2을 맨위로 보낸다
		if(back2Y >= viewHeight)
		{
			back2Y = -viewHeight;
			back1Y = 0;
		}
	}

	/**
	 *  드래곤 이미지 애니메이션 처리
	 */
	public void shipAnimation() {
		//10번에 1번 날개짓
		if ( count % 10 == 0) {
			shipIndex = (shipIndex + 1) % ships.length;
		}
	}

	/**
	 * 미사일 만드는 메서드
	 */
	public void makeMissile()
	{
		if(count % 5 == 0) {
			//미사일 객체 생성
			Missile m = new Missile();
			m.setX(shipX);
			m.setY(shipY);

			//배열에 저장한다.
			missList.add(m);
		}
	}

	/**
	 * 미사일 애니메이션
	 */
	public void moveMissile()
	{
		for(Missile tmp:missList)
		{
			//원래의 Y 좌표에서 미사일 속도만큼 빼준다.
			int resultY = tmp.getY() - missSpeed;

			//미사일 객체에 반영하고
			tmp.setY(resultY);

			// Y좌표가 위쪽 화면을 벗어났을 때
			if(resultY < -missH)
			{
				//배열에서 제거될 수 있도록 표시한다.
				tmp.setDead(true);
			}
		}
	}

	public void checkMissile()
	{
		//list 가 가변배열이므로 뒤에서 부터 처리
		for(int i = missList.size() - 1 ; i>=0 ; i--)
		{
			Missile m = missList.get(i);
			if(m.isDead()  == true)
			{
				missList.remove(i);
			}
		}
	}
	/**
	 * 터치이벤트 처리
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		//이벤트가 일어난 곳의  x 좌표 얻어오기
		int eventX = (int)event.getX();
		int eventY = (int)event.getY();

		if(( eventX >= shipX && eventX<=shipX+shipW) &&
			( eventY >= shipY && eventY<=shipY+shipH)) {

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = eventX;
					break;

				case MotionEvent.ACTION_MOVE:
					//현재 X좌표와 저장된 X좌표의 차이를 구한다.
					int deltaX = lastX - eventX;

					//드래곤의 x좌표에 반영한다.
					shipX = shipX - deltaX;

					//현재의 x좌표는 다음번 action move가 될때 과거 좌표가 된다.
					lastX = eventX;

					if(shipX <=0)
					{
						shipX = 0;
					} else if( (shipX+ shipW) >= viewWidth ) //shipX+ shipW 는 드래곤 이미지 우측의 X 좌표 이다.
					{
						shipX  = viewWidth - shipW;
					}
					break;

				case MotionEvent.ACTION_UP:
					break;

				default:
					break;
			}
		}

		return true;
	}

	/**
	 * View 의 폭과 높이가 전달되는 메서드
	 * @param w
	 * @param h
	 * @param oldw
	 * @param oldh
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		//화면의 폭과 높이를 멤버 변수에 저장한다.
		viewWidth = w ;
		viewHeight = h;

		//초기화 메서드 호출
		init();
	}

	/**
	 * 초기화 메서드
	 */
	public void init(){
		//배경 이미지 로딩하기
		backImg  = BitmapFactory.decodeResource(getResources(),R.drawable.backbg2);

		//배경 이미지를 View의 크기와 같게 스케일링하기
		backImg = Bitmap.createScaledBitmap(backImg,viewWidth,viewHeight,false);

		//비경 이미지의 y좌표를 지정한다.
		back1Y = 0;
		back2Y = -viewHeight;

		//배경 이미지의 스크롤 속도를 지정한다.
		scrollSpeed = viewHeight / 300;

		//드래곤의 폭과 높이 결정
		shipW = viewWidth / 5; //View의 폭을 5등한 크기
		shipH = shipW; //높이는 폭과 같게


		//드래곤의 초기 위치 지정. 드래곤의 폭과 높이의 절반만큼 빼줘야 원하는 위치에 드래곤이 정 가운데 위치하게 된다.
		shipX = viewWidth / 2 - shipW/2;
		shipY = viewHeight - viewHeight/5 - shipH/2;


		//드래곤의 이미지 읽어 들이고 스케일링해서 배열에 담는다.
		Bitmap ship1 = BitmapFactory.decodeResource(getResources(),R.drawable.unit1);
		Bitmap ship2 = BitmapFactory.decodeResource(getResources(),R.drawable.unit2);

		ships[0] = Bitmap.createScaledBitmap(ship1,shipW,shipH,false);
		ships[1] = Bitmap.createScaledBitmap(ship2,shipW,shipH,false);


		//미사일의 폭과 높이 결정하기
		missW = shipW;
		missH = shipH;

		//미사일 이미지 읽어들이고 스케일링하기
		missImg = BitmapFactory	.decodeResource(getResources(), R.drawable.mi1);
		missImg = Bitmap.createScaledBitmap(missImg, missW, missH, false);

		missSpeed = viewHeight/300;

		//핸들러에 메시지를 보내서 화면이 주기적으로 갱신되는 구조로 만든다.
		handler.sendEmptyMessageDelayed(0,20);
	}


	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			//	화면 갱신하기. 	view를 무효화 하고 onDraw 를 다시 호출한다.
			invalidate();

			//20 밀리초 이후에 자신이 있는 handler를 호출한다.
			handler.sendEmptyMessageDelayed(0,20);
		}
	};
}
