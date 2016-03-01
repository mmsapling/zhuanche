package com.baidu.zhuanche.ui.driver;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.ui.driver
 * @类名:	DriverAboutUsUI
 * @创建者:	陈选文
 * @创建时间:	2016-1-9	下午2:00:44 
 * @描述:	司機端關於我們
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
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
