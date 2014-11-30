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

import com.hbi.mylibrary.Util;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lee on 2014-11-26.
 */
public class DragonView extends View{
    //필요한 맴버필드 정의하기
    Context context;
    int viewWidth, viewHeight;
    Bitmap backImg; //배경이미지
    int back1Y=0, back2Y=0; //배경이미지의 y 좌표
    boolean isGamming = false; //현재 게임중인지 여부

    Bitmap[] ships=new Bitmap[2]; //드레곤의 이미지를 담을 Bitmap 배열
    int shipIndex=0;

    int shipX, shipY; //드레곤의 x, y 좌표
    int shipW, shipH; //드레곤의 폭과 높이

    int count=0; //카운트를 셀 변수
    int lastX; //action down 이 일어난곳의 x 좌표를 저장할 맴버필드

    //Missile 객체를 담을 가변 배열 객체 생성하기
    ArrayList<Missile> missList=new ArrayList<Missile>();
    Bitmap missImg; //미사일 이미지
    int missW, missH; //미사일의 폭과 높이

    int missSpeed; //미사일의 속도

    //적기 이미지 2개를 담을 배열
    Bitmap[] enemyImages=new Bitmap[2];
    //적기 객체를 담을 배열
    ArrayList<Enemy> enemyList=new ArrayList<Enemy>();
    //적기의 x 좌표를 담을 배열
    int[] enemyX = new int[5];
    //적기의 폭과 높이, 폭의반, 높이의반
    int enemyW, enemyH, enemyHalfW, enemyHalfH;
    //적기의 속도
    int enemySpeed;
    //랜덤한 수를 얻어내기 위한 객체
    Random ran=new Random();

    int scrollSpeed;

    //SoundManager 객체의 참조값
	Util.SoundManager sManager;


    //생성자
    public DragonView(Context context) {
        super(context);
        this.context=context;
    }

    public DragonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    //게임 시작하는 메소드
    public void startGame(){
        if(isGamming){
            return;
        }
        //핸들러에 메세지를 보내서 화면이 주기적으로 갱신되도록 한다.
        handler.sendEmptyMessage(0);
        //게임중인 상태로 바꿔준다.
        isGamming = true;
    }
    //게임 일시 정지하는 메소드
    public void pauseGame(){
        if(!isGamming){
            return;
        }
        handler.removeMessages(0);
        isGamming=false;
    }

    //View 의 폭과 높이가 인자로 전달되는 메소드
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        init(); //초기화 메소드 호출하기
    }
    //초기화 메소드
    public void init(){
        //배경이미지 읽어들이기
        backImg = BitmapFactory
                .decodeResource(getResources(), R.drawable.backbg);
        //배경이미지를 View 의 폭과 높이에 일치하도록 이미지 스케일링하기
        backImg=Bitmap.createScaledBitmap
                        (backImg, viewWidth, viewHeight, false);

        //배경이미지의 초기 y 좌표를 설정한다.
        back1Y = 0;
        back2Y = -viewHeight;

        //드레곤의 초기 위치 지정
        shipX = viewWidth/2;
        shipY = viewHeight-viewHeight/5;
        //드레곤의 폭과 높이 지정
        shipW = viewWidth/5;
        shipH = viewWidth/5;

        //드레곤 이미지 읽어들이기
        Bitmap ship1=BitmapFactory
                .decodeResource(getResources(), R.drawable.unit1);
        Bitmap ship2=BitmapFactory
                .decodeResource(getResources(), R.drawable.unit2);
        //원하는 크기로 스케일링하기
        ship1=Bitmap.createScaledBitmap(ship1, shipW, shipH, false);
        ship2=Bitmap.createScaledBitmap(ship2, shipW, shipH, false);
        //Bitmap 배열에 저장하기
        ships[0]=ship1;
        ships[1]=ship2;

        //미사일의 폭과 높이 결정하기
        missW = shipW;
        missH = shipH;

        //미사일 이미지 읽어들이고 스케일링하기
        missImg = BitmapFactory
                .decodeResource(getResources(), R.drawable.mi1);
        missImg = Bitmap.createScaledBitmap(missImg, missW, missH, false);

        missSpeed = viewHeight / 150;

        //화면의 폭을 5등분한 크기를 적기의 폭으로 지정한다.
        enemyW = viewWidth/5;
        enemyH = enemyW; //높이도 폭과 같이 부여
        //반지름 계산
        enemyHalfW = enemyW/2;
        enemyHalfH = enemyH/2;
        //적기를 배치하기 위한 x 좌표 정하기
        for(int i=0; i<5 ; i++){
            int x = enemyHalfW + i*enemyW;
            enemyX[i]=x;
        }
        //적기 이미지 읽어들이기
        Bitmap yellowE=BitmapFactory.decodeResource(getResources(), R.drawable.juck1);
        Bitmap whiteE=BitmapFactory.decodeResource(getResources(), R.drawable.juck2);
        //크기를 스케일링해서 배열에 저장하기
        yellowE = Bitmap.createScaledBitmap(yellowE, enemyW, enemyH, false);
        whiteE = Bitmap.createScaledBitmap(whiteE, enemyW, enemyH, false);
        enemyImages[0]=whiteE;
        enemyImages[1]=yellowE;
        //적기의 속도 지정하기
        enemySpeed = viewHeight / 150;

        scrollSpeed = viewHeight / 300;

	    //SoundManager 객체의 참조값 얻어오기
	    sManager = Util.SoundManager.getInstance();

	    //아래 라인을 주석처리하면 처음부터 시작되지 않는다.
        //handler.sendEmptyMessageDelayed(0, 50);
    }

    //View 를 렌더링하는 메소드
    @Override
    protected void onDraw(Canvas canvas) {
        count++; //카운트 증가 시키기
        //배경이미지 그리기
        canvas.drawBitmap(backImg, 0, back1Y, null);
        canvas.drawBitmap(backImg, 0, back2Y, null);
        //반복문  돌면서 미사일 그리기
        for(Missile tmp : missList){
            //tmp 객체에 담긴 정보를 바탕으로 미사일을 그려준다.
            canvas.drawBitmap
                    (missImg, tmp.getX()-missW/2, tmp.getY()-missH/2, null);
        }

        //반복문 돌면서 적기 그리기
        for(Enemy tmp : enemyList){
            //tmp 객체에 담긴 정보를 바탕으로 적기를 그려준다.
            canvas.drawBitmap(enemyImages[tmp.getImgIndex()],
                    tmp.getX()-enemyHalfW,
                    tmp.getY()-enemyHalfH,
                    null);
        }

        //드레곤 이미지 그리기
        canvas.drawBitmap(ships[shipIndex], shipX-shipW/2, shipY-shipH/2, null);

        backScroll(); //배경이미지 스크롤
        shipAnimation(); //드레곤 애니메이션
        makeMissile(); //미사일 만드는 메소드 호출
        moveMissile(); //미사일 움직이는 메소드
        checkMissile(); //미사일 체크하는 메소드
        makeEnemy();
        moveEnemy();
        checkEnemy();
        checkMissEnemyCollusion(); // 적기와 충돌검사 하기
        checkDragonEnemyCollusion(); // 드레곤과 적기의 충돌 검사
    }

    //드레곤과 적기의 충돌을 검사하는 메소드
    public void checkDragonEnemyCollusion(){
        for(int i=0; i<enemyList.size() ; i++){
            //i번째 적기 객체를 불러온다.
            Enemy e = enemyList.get(i);
            //드레곤이 죽었는지 여부
            boolean isDragonDie =
                    shipX > e.getX() - enemyHalfW &&
                    shipX < e.getX() + enemyHalfW &&
                    shipY > e.getY() - enemyHalfH &&
                    shipY < e.getY() + enemyHalfH ;

            if(isDragonDie){
	            sManager.play(MainActivity.SOUND_DIE);
                //핸들러 메세지 제거
                handler.removeMessages(0);
            }

        }
    }

    //미사일과 적기의 충돌을 검사하는 메소드
    public void checkMissEnemyCollusion(){
        for(int i=0; i<missList.size(); i++){
            //i번째 미사일 객체를 불러와서
            Missile m = missList.get(i);
            //반복문 돌면서 모든 적기 객체를 불러와서 위치를 비교한다.
            for(int j=0 ; j<enemyList.size(); j++){
                //j번째 적기 객체를 불러온다.
                Enemy e = enemyList.get(j);
                //충돌했는지 판정한다.
                boolean isShooted =
                        m.getX() > e.getX() - enemyHalfW &&
                        m.getX() < e.getX() + enemyHalfW &&
                        m.getY() > e.getY() - enemyHalfH &&
                        m.getY() < e.getY() + enemyHalfH ;

                if(isShooted){
                    //여기가 수행된다면 i 번째 미사일은 j번째 적기와 충돌한것이다.
                    m.setDead(true); //미사일 제거
                    //50 감소한 에너지 값을 얻어낸다음
                    int currentEnergy = e.getEnergy() - 50;
                    //적기의 에너지에 부여한다.
                    e.setEnergy( currentEnergy );

                    if(currentEnergy <= 0){ //적기의 에너지가 백피라면
                        e.setDead(true); //적기 제거
                    }
					sManager.play(MainActivity.SOUND_SHOOT);
                }
            }

        }
    }


    //제거할 적기는 제거하는 메소드
    public void checkEnemy(){
        //반복문 역으로 돌면서
        for(int i=enemyList.size()-1 ; i>=0 ; i--){
            // i 번째 적기 객체를 불러와서
            Enemy tmp = enemyList.get(i);
            //제거해야할 적기라면
            if(tmp.isDead()){
                //i번째 적기를 배열에서 제거한다.
                enemyList.remove(i);
            }
        }
    }

    //적기 움직이는 메소드
    public void moveEnemy(){
        for (Enemy tmp : enemyList) {
            //적기의 현재위치에 속도값을 더한 결과값을 얻어낸다.
            int resultY = tmp.getY() + enemySpeed;
            //적기의 위치에 반영한다.
            tmp.setY(resultY);
            //아래쪽 화면을 벗어 났다면
            if(resultY > viewHeight+enemyHalfH){
                //배열에서 제거될수 있도록 표시한다.
                tmp.setDead(true);
            }
        }
    }

    //적기 5개 만드는 메소드
    public void makeEnemy(){
        //0~49 사이의렌덤한 정수를 얻어낸다.
        int ranNum = ran.nextInt(50);
        //그 수가 우연히 10이 나오지 않았다면
        if( ranNum != 10 ){
            //메소드를 종료해라
            return;
        }

        //반복문 돌면서 5개의 적기 객체를 만들어서 배열에 저장하기
        for(int i=0; i<5 ; i++){
            //이미지 인덱스를 0 혹은 1이 랜덤하게 부여되도록 한다.
            int imgIndex = ran.nextInt(2);
            int energy = 0;

            if(imgIndex == 0){ //흰색 적기라면
                energy = 50; //에너지를 50으로 부여
            }else if(imgIndex == 1){//노란색 적기라면
                energy = 100; //에너지를 100 으로 부여
            }

            //적기 객체를 생성해서
            Enemy e=new Enemy();
            e.setImgIndex(imgIndex); //적기의 종류를 결정하고
            e.setX(enemyX[i]); //x좌표를 결정하고
            e.setEnergy(energy); //에너지를 결정하고
            //배열에 저장한다.
            enemyList.add(e);
        }
    }

    //제거할 미사일은 제거하는 메소드
    public void checkMissile(){
        //반복문 역으로 돌면서 제거할 Missile 객체는 제거한다.
        for(int i=missList.size()-1 ; i>=0 ; i--){
            //i번째 미사일 객체 불러오기
            Missile tmp = missList.get(i);
            if(tmp.isDead()){ //제거해야할 미사일 객체라면
                //배열에서 i번째(현재 인덱스)를 제거한다.
                missList.remove(i);
            }
        }
    }

    //미사일을 움직이는 메소드
    public void moveMissile(){

        for(Missile tmp : missList){
            //월래 저장된 y 좌표를 불러와서 missSpeed을 뺀값을 얻어낸다.
            int resultY = tmp.getY() - missSpeed;
            //10을 뺀 결과값을 다시 넣어준다.
            tmp.setY(resultY);
            //y 좌표가 위쪽화면을 벗어 났을때
            if(resultY < - missH/2){
                //배열에서 제거될수 있도록 표시한다.
                tmp.setDead(true);
            }
        }
    }

    //미사일을 만드는 메소드
    public void makeMissile(){
        if(count%10 != 0){
            return;
        }
        //미사일 객체 생성하기
        Missile m = new Missile();
        m.setX(shipX); //드레곤의 x 좌표를 미사일의 x 좌표로 결정한다.
        m.setY(shipY); //드레곤의 y 좌표를 미사일의 y 좌표로 결정한다.
        //배열에 저장한다.
        missList.add(m);
    }

    //드레곤 애니메이션을 주는 메소드
    public void shipAnimation(){
        if(count%10 != 0 ){
            return;
        }
        shipIndex++; //인덱스를 1 증가 시킨다.
        if(shipIndex==2){ //만일 존재하지 않는 인덱스라면
            //다시 인덱스를 처음으로 되돌린다.
            shipIndex=0;
        }
    }


    //배경이미지를 스크롤하는 메소드
    public void backScroll(){
        back1Y += scrollSpeed;
        back2Y += scrollSpeed;
        //배경1의 y좌표가 한계점에 다다랏을때
        if(back1Y >= viewHeight){
            back1Y = -viewHeight;
            back2Y = 0;
        }
        //배경2의 y좌표가 한계점에 다다랏을때
        if(back2Y >= viewHeight){
            back2Y = -viewHeight;
            back1Y = 0;
        }
    }
    //터치 이벤트 처리하기
    @Override
    public boolean onTouchEvent(MotionEvent event) {

	    if(isGamming == false) //게임 중이 아니라면 리턴한다.
	    {
		    return  false;
	    }

        //이벤트가 일어난곳의 x 좌표 얻어오기
        int eventX = (int)event.getX();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //맴버필드에 이벤트가 일어난곳의 x 좌표를 저장한다.
                lastX = eventX;
                break;
            case MotionEvent.ACTION_MOVE:
                //현재 x 좌표와 저장된 x 좌표의 차이를 구한다.
                int deltaX = lastX - eventX;
                //드레곤의 x 좌표에 반영한다.
                shipX = shipX - deltaX;
                //현재의 x 좌표는 다음번 action move 될때 과거 좌표가 된다.
                lastX = eventX;
                //왼쪽으로 벗어났다면
                if(shipX < 0){
                    shipX = 0;
                }

                //오른쪽으로 벗어났다면
                if(shipX > viewWidth){
                    shipX = viewWidth;
                }

                break;
        }


        return true;
    }

    //화면을 주기적으로 갱신해주기 위한 핸들러 객체 정의하기
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           invalidate();//화면 갱신하기
           handler.sendEmptyMessageDelayed(0, 10);
           
        }
    };
}







