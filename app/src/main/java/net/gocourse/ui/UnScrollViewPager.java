package net.gocourse.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 主要用于静止通过页面滑动来切换fragment
 * 
 * @author Lancy
 *
 */

public class UnScrollViewPager extends ViewPager {

	public UnScrollViewPager(Context context) {
		super(context);
	}

	public UnScrollViewPager(Context context, AttributeSet atts) {
		super(context, atts);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return false;
	}

}
