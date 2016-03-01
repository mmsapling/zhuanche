package com.baidu.zhuanche.view;

import com.baidu.zhuanche.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.view
 * @类名: EmptyView
 * @创建者: 陈选文
 * @创建时间: 2016-1-8 下午5:56:33
 * @描述: 自定义空视图
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class EmptyView extends RelativeLayout
{
	private TextView	mTvContent;
	private ImageView	mIvImg;

	public EmptyView(Context context) {
		super(context, null);
	}

	public EmptyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(context, R.layout.view_empty, this);
		mTvContent = (TextView) view.findViewById(R.id.view_empty_tv_msg);
		mIvImg = (ImageView) view.findViewById(R.id.view_empty_iv_img);
	}
	public void setContent(String content){
		mTvContent.setText(content);
	}
	public void setImageResource(Integer id){
		mIvImg.setImageResource(id);
	}
}
