package com.baidu.zhuanche.ui.driver;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;


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
