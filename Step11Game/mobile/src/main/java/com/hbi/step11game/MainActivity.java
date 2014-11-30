package com.hbi.step11game;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.hbi.mylibrary.Util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends Activity  implements  GoogleApiClient.OnConnectionFailedListener
																		, GoogleApiClient.ConnectionCallbacks ,DataApi.DataListener{

	DragonView dView;
	static final int SOUND_DIE=0;
	static final int SOUND_LASER=1;
	static final int SOUND_SHOOT =2;

	GoogleApiClient gClient;

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Util.keepScreenOn(this);

		//GoogleApiClient 객체값 가져오기
		gClient = new GoogleApiClient.Builder(this)
			.addApi(Wearable.API)
			.addConnectionCallbacks(this)
			.addOnConnectionFailedListener(this)
			.build();

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


	/**
	 * 드래곤의 움직임에 대한 데이터가 들어오는 메서드
	 * @param dataEvents
	 */
	@Override
	public void onDataChanged(DataEventBuffer dataEvents) {
		List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
		dataEvents.close();

		for(DataEvent event: events)
		{
			if(event.getType() == DataEvent.TYPE_CHANGED)
			{
				//path를 읽어온다.
				String path = event.getDataItem().getUri().getPath();
			}
		}
	}


	/**
	 * 연결된 node 의 아이디 리스트를 리턴하는 메소드
	 * @return
	 */
	public Collection<String> getNodes(){
		HashSet<String> results = new HashSet<String>();
		NodeApi.GetConnectedNodesResult nodes=
			Wearable.NodeApi.getConnectedNodes(gClient).await();
		for(Node node : nodes.getNodes()){
			results.add(node.getId());
		}
		return  results;
	}


	/**
	 * WearActivity 를 시작 시키는 메세지를 보낼 테스크
	 */
	class StartWearTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Collection<String> nodes = getNodes();
			for(String node : nodes){
				sendStartWearMessage(node);
			}
			return null;
		}
	};

	/**
	 * WearActivity 시작 메세지를 보내는 메소드
	 * @param node
	 */
	public void sendStartWearMessage(String node){
		Wearable.MessageApi.sendMessage(gClient,node,"/startWearActivity",new byte[0])
			.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
				@Override
				public void onResult(MessageApi.SendMessageResult sendMessageResult) {
					if(sendMessageResult.getStatus().isSuccess()){
					}else{
					}
				}
			});
	}

	/**
	 * GoogleApiClient 에 연결되었을때
	 * @param bundle
	 */
	@Override
	public void onConnected(Bundle bundle) {
		//WearActivity 시작 요청을 보낸다.
		new StartWearTask().execute();
	}


	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
	}


	@Override
	protected void onResume() {
		super.onResume();
		gClient.connect();
		/*
		DataApi 리스너 객체를 등록한다.
		리스너를 등록해야 Wearable 디바이스에서 메시지를 보냈을때
		onDataChanged() 메서드가 실행된다.
		 */
		Wearable.DataApi.addListener(gClient,this);
	}

	@Override
	protected void onStop() {
		gClient.disconnect();
		super.onStop();
	}
}
