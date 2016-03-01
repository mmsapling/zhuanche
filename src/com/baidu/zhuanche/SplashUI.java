package com.baidu.zhuanche;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import cn.jpush.android.api.JPushInterface;

import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.ui.driver.DriverLoginUI;
import com.baidu.zhuanche.ui.user.UserHomeUI;
import com.baidu.zhuanche.ui.user.UserLoginUI;

public class SplashUI extends BaseActivity implements OnClickListener
{
	private Button	mBtUser;
	private Button	mBtDriver;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_splash);
		mBtDriver = (Button) findViewById(R.id.splash_button_driver);
		mBtUser = (Button) findViewById(R.id.splash_button_user);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("角色选择");
	}

	@Override
	public void initListener()
	{
		super.initListener();
		mBtDriver.setOnClickListener(this);
		mBtUser.setOnClickListener(this);
		mIvLeftHeader.setOnClickListener(this);
	}
	@Override
	public void onBackPressed()
	{
		finishActivity(UserHomeUI.class);
	}
	@Override
	public void onClick(View v)
	{
		if (v == mBtDriver)
		{
			Bundle bundle = new Bundle();
			bundle.putString("username", "");
			bundle.putString("password", "");
			startActivityAndFinish(DriverLoginUI.class,bundle);
		}
		else if (v == mBtUser)
		{
			Bundle bundle = new Bundle();
			bundle.putString("username", "");
			bundle.putString("password", "");
			startActivityAndFinish(UserLoginUI.class,bundle);
		}
		else if (v == mIvLeftHeader)
		{
			finishActivity(UserHomeUI.class);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		JPushInterface.onResume(getApplicationContext());
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		JPushInterface.onPause(getApplicationContext());
	}
}
