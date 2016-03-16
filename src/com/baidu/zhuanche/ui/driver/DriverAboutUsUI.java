package com.baidu.zhuanche.ui.driver;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;


public class DriverAboutUsUI extends BaseActivity
{

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_aboutus);
	}
	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("關於我們");
	}
	@Override
	public void initListener()
	{
		finishActivity();
	}
	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
}
