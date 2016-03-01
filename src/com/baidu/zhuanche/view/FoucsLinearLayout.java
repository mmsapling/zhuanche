package com.baidu.zhuanche.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.view
 * @类名:	FoucsLinearLayout
 * @创建者:	陈选文
 * @创建时间:	2016-1-11	下午6:01:20 
 * @描述:	TODO
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class FoucsLinearLayout extends LinearLayout
{

	public FoucsLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FoucsLinearLayout(Context context) {
		super(context,null);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		return true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return true;
	}
}
