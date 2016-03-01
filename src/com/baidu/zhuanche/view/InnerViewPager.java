package com.baidu.zhuanche.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.view
 * @类名:	InnerViewPager
 * @创建者:	陈选文
 * @创建时间:	2015-12-28	下午2:13:11 
 * @描述:	TODO
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class InnerViewPager extends ViewPager {

	private float	mDownX;
	private float	mDownY;

	public InnerViewPager(Context context) {
		super(context);
	}

	public InnerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 左右滑动-->请求父容器不拦截-->自己处理滑动
	 */
	/**
	getParent(父容器). request(请求)	Disallow(不)	Intercept(拦截)	TouchEvent(Touch事件) (true(同意))
	请求父容器不拦截(true)-->自己处理
	请求父容器不拦截(false)-->父亲处理
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getRawX();
			mDownY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			float moveX = ev.getRawX();
			float moveY = ev.getRawY();

			int diffX = (int) (moveX - mDownX + .5f);
			int diffY = (int) (moveY - mDownY + .5f);
			if (Math.abs(diffX) > Math.abs(diffY)) {// 左右
				// 请求父容器不拦截-->自己处理滑动
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:

			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
}
