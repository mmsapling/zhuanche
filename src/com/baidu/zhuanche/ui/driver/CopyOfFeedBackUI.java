package com.baidu.zhuanche.ui.driver;

import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;


public class CopyOfFeedBackUI extends BaseActivity implements OnClickListener
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
		mIvLeftHeader.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
	}

	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
}
