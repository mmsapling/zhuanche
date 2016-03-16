package com.baidu.zhuanche.ui.user;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.utils.ToastUtils;


public class CopyOfFeedBackUI extends BaseActivity implements OnClickListener
{

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_user_aboutus);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("关于我们");
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
