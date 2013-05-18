package com.neatocode.throughwalls.view;

import com.neatocode.throughwalls.R;
import com.neatocode.throughwalls.R.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class OffsetIndicatorView extends View {
	
	private static final String LOG_TAG = "ThroughWalls";
	
	private Float mOffsetPercent;
	
	private Drawable mIndicator;

	public OffsetIndicatorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public OffsetIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public OffsetIndicatorView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(final Context aContext) {
		mIndicator = aContext.getResources().getDrawable(R.drawable.marker_camera);
	}
	
	public void setIndicatorOffset(final Float aOffsetPercent) {
		mOffsetPercent = aOffsetPercent;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if ( null == mOffsetPercent ) {
			return;
		}
		
		final int width = getWidth();
		final int height = getHeight();
		final int indicatorWidth = mIndicator.getIntrinsicWidth();
		final int indicatorHeight = mIndicator.getIntrinsicHeight();
		
		final int offsetCenterX = (int) (width * mOffsetPercent);
		final int indicatorLeft = offsetCenterX - (indicatorWidth/2);
		final int indicatorTop = (height / 2) - (indicatorHeight/2);
		
		mIndicator.setBounds(indicatorLeft, indicatorTop, 
				indicatorLeft + indicatorWidth, indicatorTop + indicatorHeight);
		mIndicator.draw(canvas);
		
	}
	
}
