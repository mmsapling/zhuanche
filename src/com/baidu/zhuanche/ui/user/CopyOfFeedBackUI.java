package com.baidu.zhuanche.ui.user;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.utils.ToastUtils;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: FeedBackUI
 * @创建者: 陈选文
 * @创建时间: 2016-1-9 上午11:46:09
 * @描述: 司机端意见反馈
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
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
