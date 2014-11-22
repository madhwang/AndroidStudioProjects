package com.hbi.step09wearablelistview;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lee on 2014-11-22.
 */
public class ListItemLayout extends LinearLayout
	implements WearableListView.Item{

	//현재 배율을 저장할 맴버필드
	float mScale;
	float mFadedTextAlpha;
	int mFadedCircleColor;
	int mChosenCircleColor;
	ImageView mCircle;
	TextView mName;

	public ListItemLayout(Context context) {
		super(context, null);
	}

	public ListItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public ListItemLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}
	//레이아웃(각각의 아이템) 전개가 완료 되었을때 호출되는 메소드
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mFadedTextAlpha = getResources().getInteger(R.integer.faded_alpha)/100f;
		mFadedCircleColor = getResources().getColor(R.color.wl_gray);
		mChosenCircleColor = getResources().getColor(R.color.wl_blue);


		mCircle = (ImageView) findViewById(R.id.circle);
		mName = (TextView) findViewById(R.id.time_text);
	}
	//최소 크기 배율
	@Override
	public float getProximityMinValue() {
		return 1f; //원본크기
	}
	//최대 크기 배율
	@Override
	public float getProximityMaxValue() {
		return 1.5f; //원본 크기의 1.5배
	}
	//현재 스케일 값을 리턴해주는 메소드
	@Override
	public float getCurrentProximityValue() {
		return mScale;
	}
	//현재 스케일 value 값이 인자로 전달되는 메소드
	@Override
	public void setScalingAnimatorValue(float scale) {
		//인자로 전달되는 현재 배율값을 맴버필드에 저장한다.
		mScale=scale;
		mCircle.setScaleX(scale);
		mCircle.setScaleY(scale);
	}
	//확대가 시작되었을때 호출되는 메소드
	@Override
	public void onScaleUpStart() {
		mName.setAlpha(1f);
		((GradientDrawable) mCircle.getDrawable())
			.setColor(mChosenCircleColor);
	}
	//축소가 시작되었을때 호출되는 메소드
	@Override
	public void onScaleDownStart() {
		((GradientDrawable) mCircle.getDrawable())
			.setColor(mFadedCircleColor);
		mName.setAlpha(mFadedTextAlpha);
	}
}