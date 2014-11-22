package com.hbi.step09wearablelistview;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by user on 2014-11-22.
 */
public class ListItemLayout  extends LinearLayout implements WearableListView.Item{

	//현재 배율을 저장할 멤버 필드
	private float mScale;

	float mFadedTextAlpha;
	int mFadedCircleColor;
	int mChosenCircleColor;
	ImageView mCircle;
	TextView mName;

	public ListItemLayout(Context context) {
		super(context);
	}

	public ListItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mFadedTextAlpha =  getResources().getInteger(R.integer.faded_alpha)/100f;
		mFadedCircleColor = getResources().getColor(R.color.wl_gray);
		mChosenCircleColor = getResources().getColor(R.color.wl_blue);
	}


	/**
	 * 레이아웃(각각의 아이템) 전개가 완료 되었을 때 호출되는 메서드
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mCircle = (ImageView) findViewById(R.id.circle);
		mName = (TextView) findViewById(R.id.time_text);
	}

	/**
	 * 최소 크기 배율
	 * @return
	 */
	@Override
	public float getProximityMinValue() {
		return 1f; //원본크기
	}

	/**
	 * 최대 크기 배율
	 * @return
	 */
	@Override
	public float getProximityMaxValue() {
		return 1.5f;
	}

	/**
	 * 현재 스케일 값을 리턴해주는 메서드
	 * @return
	 */
	@Override
	public float getCurrentProximityValue() {
		return 0;
	}

	/**
	 * 현재 스케일 value 값이 인자로 전달되는 메소드
	 * @param scale
	 */
	@Override
	public void setScalingAnimatorValue(float scale) {
		//인자로 전달되는 현재 배율값을 멤버 필드에 저장한다.
		this.mScale = scale;
		this.mCircle.setScaleX(scale);
		this.mCircle.setScaleY(scale);
	}

	/**
	 * 확대가 시작될때 호출되는 메서드
	 */
	@Override
	public void onScaleUpStart() {
		mName.setAlpha(1f);
		((GradientDrawable) mCircle.getDrawable())
			.setColor(mChosenCircleColor);
	}

	/**
	 * 축소가 시작될때 호출되는 메서드
	 */
	@Override
	public void onScaleDownStart() {
		((GradientDrawable) mCircle.getDrawable())
			.setColor(mFadedCircleColor);
		mName.setAlpha(mFadedTextAlpha);
	}
}


