package com.hbi.step09wearablelistview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WearActivity extends Activity {
	public static final int MODEL_COUNTS = 10;
	public static final String TAG = "WearActivity";
	WearableListView listView;
	//리스트뷰 셀에 출력할 모델 객체를 담은 배열객체
	private ListViewItem[] models = new ListViewItem[MODEL_COUNTS];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wear);
		init();
	}
	//초기화 메소드
	public void init(){
		//셈플 모델객체 넣어두기
		models[0]=new ListViewItem("김구라1", 1000);
		models[1]=new ListViewItem("김구라2", 2000);
		models[2]=new ListViewItem("김구라3", 3000);
		models[3]=new ListViewItem("김구라4", 4000);
		models[4]=new ListViewItem("김구라5", 5000);
		models[5]=new ListViewItem("김구라6", 6000);
		models[6]=new ListViewItem("김구라7", 7000);
		models[7]=new ListViewItem("김구라8", 8000);
		models[8]=new ListViewItem("김구라9", 9000);
		models[9]=new ListViewItem("김구라10", 10000);
		listView = (WearableListView)findViewById(R.id.list_view);
		listView.setAdapter(new ListViewAdapter(this));
	}

	//셀에 출력할 모델객체를 얻어낼 클래스
	static class ListViewItem{
		long duration;
		String label;
		public ListViewItem(String label, long duration) {
			this.label = label;
			this.duration = duration;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	//ListViewAdapter 객체를 생성할 클래스

	final class ListViewAdapter extends WearableListView.Adapter{
		Context context;
		LayoutInflater inflater;

		//생성자
		public ListViewAdapter(Context context){
			this.context=context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			//레이아웃전개후
			View v = inflater.inflate(R.layout.list_item, null);
			//뷰홀더객체를 만들고
			WearableListView.ViewHolder holder = new WearableListView.ViewHolder(v);
			//홀더 객체를 리턴해준다.
			return holder;
		}

		//해당 뷰홀더 객체에 출력할 내용을 바인딩해준다.
		@Override
		public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int i) {
			ImageView img = (ImageView)viewHolder.itemView.findViewById(R.id.circle); //
			TextView view = (TextView)viewHolder.itemView.findViewById(R.id.time_text);
			view.setText(models[i].label);
			viewHolder.itemView.setTag(i);

		}

		@Override
		public int getItemCount() {
			return MODEL_COUNTS;
		}
	}
}