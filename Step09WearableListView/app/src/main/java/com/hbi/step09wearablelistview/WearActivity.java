package com.hbi.step09wearablelistview;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.ViewGroup;

/**
 * Model(data)는 Adapter 에게 데이터를 전달하고
 * Adapter는 Mode(data) 정보를 이용해서 셀을 만들어서 WearableListView 에게 전달한다.
 */
public class WearActivity extends Activity {

	WearableListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wear);

		//WearableListView의 참조값 얻어와서 멤버 필드에 저장하기.
		listView = (WearableListView)findViewById(R.id.list_view);
	}

	/**
	 * 초기화 메서드
	 */
	public void init()
	{

	}

	/**
	 * 어댑터 객체를 생성할 클래스 정의하기
	 */
	class ListViewAdapter extends WearableListView.Adapter{

		@Override
		public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			return null;
		}

		@Override
		public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int i) {

		}

		@Override
		public int getItemCount() {
			return 0;
		}
	}
}
