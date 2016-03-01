package com.baidu.zhuanche.ui.driver;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.ui.driver
 * @类名:	ShowImgUI
 * @创建者:	陈选文
 * @创建时间:	2016-2-24	上午10:34:49 
 * @描述:	TODO
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class ShowImgUI extends BaseActivity implements OnClickListener
{
	private ImageView mIvShow;
	@Override
	public void initView()
	{
		setContentView(R.layout.ui_showimg);
		mIvShow =(ImageView) findViewById(R.id.showing_iv);
	}
	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("示例");
	}
	@Override
	public void initListener()
	{
		super.initListener();
		mIvLeftHeader.setOnClickListener(this);
		mIvShow.setOnClickListener(this);
	}
	@Override
	public void onClick(View v)
	{
		if(v == mIvLeftHeader){
			finishActivity();
		}else if(v == mIvShow){
			finishActivity();
		}
	}
	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
}
