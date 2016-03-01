package com.baidu.zhuanche.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @项目名: ZhuanChe
 * @包名: com.baidu.zhuanche.ui
 * @类名: NoScrollListView
 * @创建者: 陈选文
 * @创建时间: 2015-12-8 下午8:29:44
 * @描述: 自定义listview，直接将子类的高度测量出来
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class NoScrolledGridView extends GridView
{

	public NoScrolledGridView(Context context) {
		super(context,null);
	}

	public NoScrolledGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{

		int expandSpec = MeasureSpec.makeMeasureSpec(
														Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
