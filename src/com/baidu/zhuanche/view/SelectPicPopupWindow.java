package com.baidu.zhuanche.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.baidu.zhuanche.R;

/**
 * @创建者 Administrator
 * @创时间 2015-8-27 上午11:16:59
 * @描述 TODO
 * 
 * @版本 $Rev$
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 TODO
 */
public class SelectPicPopupWindow extends PopupWindow
{
	public SelectPicPopupWindow(Context context, OnClickListener listener) {
		View contentView = View.inflate(context, R.layout.layout_pupup, null);
		this.setContentView(contentView);
		int width = LayoutParams.MATCH_PARENT;
		this.setWidth(width);
		int height = LayoutParams.WRAP_CONTENT;
		this.setHeight(height);

		this.setBackgroundDrawable(new BitmapDrawable());
		this.setOutsideTouchable(true);

		this.setAnimationStyle(R.style.PopupAnimation);

		// 找到孩子
		contentView.findViewById(R.id.btn_take_photo).setOnClickListener(listener);
		contentView.findViewById(R.id.btn_pick_photo).setOnClickListener(listener);
		contentView.findViewById(R.id.btn_cancel).setOnClickListener(listener);
	}
}
