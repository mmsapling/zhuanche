package com.baidu.zhuanche.ui.user;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui
 * @类名: UserLoginSelectUI
 * @创建者: 陈选文
 * @创建时间: 2015-12-23 下午4:51:31
 * @描述: 用户登陆 注册选择页面
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
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
