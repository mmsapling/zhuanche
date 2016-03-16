package com.baidu.zhuanche.ui.user;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;


public class UserLoginSelectUI extends BaseActivity implements OnClickListener
{
	private Button	mBtLogin;
	private Button	mBtRegist;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_user_login_select);
		mBtLogin = (Button) findViewById(R.id.uls_bt_login);
		mBtRegist = (Button) findViewById(R.id.uls_bt_regist);
	}
	@Override
	public void initListener()
	{
		super.initListener();
		mBtLogin.setOnClickListener(this);
		mBtRegist.setOnClickListener(this);
	}
	@Override
	public void onClick(View v)
	{
		if(v == mBtLogin){
			startActivity(UserLoginUI.class);
		}else if(v == mBtRegist){
			startActivity(UserRegistUI.class);
		}
	}
}
