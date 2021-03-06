package com.hbi.step10datalayer2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.hbi.mylibrary.Util;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;


public class MainActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

	EditText console;
	GoogleApiClient gClient;
	ImageView resultImage;

	//사진찍기 요청에 대한 식별값을 정의한다.
	static final int REQUEST_IMAGE_CAPTURE = 1; /* 썸네일 촬영 */
	static final int REQUEST_IMAGE_CAPTURE2 = 2; /* 원본 촬영 */

	//불러온 Thumbnail  이미지를 저장할  Bitmap
	Bitmap imageBitmap;

	//원본 이미지가 저장된 절대 경로
	String absolutePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		console = (EditText) findViewById(R.id.console);
		resultImage = (ImageView)findViewById(R.id.imageView);

		//GoogleApiClient 객체값 가져오기
		gClient = new GoogleApiClient.Builder(this)
					.addApi(Wearable.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();

	}

	/**
	 * Start Wear 버튼을 눌렀을 때 호출되는 메서드
	 * @param v
	 */
	public void startWear(View v)
	{
		//비동기로 작업을 새로운 스레드로 시작 시킨다.
		new StartWearTask().execute();
	}

	/**
	 * 썸네일 촬영  버튼을 눌렀을 때
	 * @param v
	 */
	public void takePhoto(View v)
	{
		Util.startActivityForThumbnailPhoto(this,REQUEST_IMAGE_CAPTURE);
	}

	/**
	 * 원본촬영 버튼을 눌렀을때
	 * @param v
	 */
	public void takePhoto2(View v)
	{
		/*	원본 이미지가 저장된 경로. 외부 저장 장치를 쓰는 것으로서 퍼미션을 얻어야 한다.
			AndroidManifest.xml 의 수정이 필요하다.
		 */
		absolutePath = Util.startActivityForOriginalPhoto(this,REQUEST_IMAGE_CAPTURE2);
	}

	/**
	 * 사진 전송 버튼을 눌렀을때
	 * - 내부적으로 AsyncTask를 이용하여 전송한다.
	 */
	public void sendPhoto(View v){
		if(resultImage == null || gClient == null){
			//전송할 준비가 되지 않았다면 메소드를 종료한다.
			return ;
		}

		//DrawingCache 사용가능하게 설정
		resultImage.setDrawingCacheEnabled(true);

		//View의 현재 내용을 Bitmap Type으로 읽어온다.
		Bitmap img = resultImage.getDrawingCache();

		//크기를 스케일링 해서 멤버 필드에 담는다.
		imageBitmap = Bitmap.createScaledBitmap(img,320,320,false);

		// bitmap 으로는 전송이 안되기 때문에 Asset Type 으로 변경해야 한다.
		Asset asset = Util.bitmapToAsset(imageBitmap);

		//PutDataMapRequest객체 얻어오기 (path 를 "/sendPhoto"로 지정)
		PutDataMapRequest dataMap = PutDataMapRequest.create("/sendPhoto");

		//전송할 데이터를 담는다.
		dataMap.getDataMap().putAsset("image",asset);

		//데이터에 항상 변화를 줄 수 있도록 현재 시간을 담는다.
		dataMap.getDataMap().putLong("time",new Date().getTime());

		//PutDataRequest  객체를 얻어온다
		PutDataRequest request = dataMap.asPutDataRequest();

		//Wearable.DataApi 객체를 이용해서 전송하고 결과값을 받아 처리한다.
		Wearable.DataApi.putDataItem(gClient,request).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
			@Override
			public void onResult(DataApi.DataItemResult dataItemResult) {
				if (dataItemResult.getStatus().isSuccess()) {
					printLog("이미지 전송 성공!");
				} else {
					printLog("이미지 전송 실패!");
				}
			}
		});
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode == RESULT_OK) {
			if (requestCode == REQUEST_IMAGE_CAPTURE) {
				//성공적으로 작업을 끝마치면, Thumbnail 이미지 데이터를 불러온다.
				Bundle extras = data.getExtras();
				imageBitmap = (Bitmap) extras.get("data");

				//ImageView 에 Bitmap 이미지 출력하기
				resultImage.setImageBitmap(imageBitmap);
			} else if (requestCode == REQUEST_IMAGE_CAPTURE2) {
				//원본 이미지 가져오기 작업 성공적으로 끝났다면,absolute 에는 저장된 jpg 이미지가 있는 절대 경로가 들어있다

				//Util을 이용해서 이미지뷰의 크기에 맞게 사진 이미지를 출력한다.
				Util.fitToImageView(resultImage,absolutePath);
			}
		}
	}


	/**
	 * WearActivity 를 시작 시키는 메시지를 보낼 태스크
	 */
	class StartWearTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			Collection<String> nodes = getNodes();
			for(String node:nodes)
			{
				sendStartWearMessage(node);
			}
			return null;
		}
	};

	/**
	 * WearActitvity 시작 메세지를 보내는 메서드
	 * @param node
	 */
	public void sendStartWearMessage(String node)
	{
		Wearable.MessageApi.sendMessage(gClient,node,"/startWearActivity",new byte[0]).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
			@Override
			public void onResult(MessageApi.SendMessageResult sendMessageResult) {
				if(sendMessageResult.getStatus().isSuccess())
				{
					printLog("/startWearActivity  전송 성공!!");
				}
				else
				{
					printLog("/startWearActivity  전송 실패!!");
				}
			}
		});
	}


	/**
	 * 로그 출력하는 메서드
	 * @param msg
	 */
	public void printLog(String msg)
	{
		Message m = new Message();
		m.obj = msg;
		handler.sendMessage(m);
	}



	public Collection<String> getNodes(){
		HashSet<String> results = new HashSet<String>();
		NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(gClient).await();

		for(Node node:nodes.getNodes())
		{
			results.add(node.getId());
		}
		return results;
	}

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String str = (String)msg.obj;
			console.append(str +"\n");
		}
	};


	@Override
	public void onConnected(Bundle bundle) {
		printLog("onConnected()");
	}

	@Override
	public void onConnectionSuspended(int i) {
		printLog("onConnectionSuspended()");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		printLog("onConnectionFailed()");
	}


	@Override
	protected void onResume() {
		super.onResume();
		gClient.connect();
	}

	@Override
	protected void onStop() {
		gClient.disconnect();
		super.onStop();
	}
}
