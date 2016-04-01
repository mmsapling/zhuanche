package com.baidu.zhuanche.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.SplashUI;
import com.baidu.zhuanche.adapter.QuhaoAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.bean.UserBean;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.connect.ServiceUtil;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.service.UserConnectService;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.MD5Utils;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;


public class UserLoginUI extends BaseActivity implements OnClickListener
{
	private TextView		mTvQuhao;
	private EditText		mEtNumber;
	private EditText		mEtPassword;
	private CheckBox		mCbJizhu;
	private Button			mBtLogin;
	private TextView		mTvWangji;
	private TextView		mTvRegist;
	private String			mPassword;
	private LinearLayout	mContainerQuhao;
	private List<String>	mDatas;
	private QuhaoAdapter	mAdapter;
	private String			mNumber;
	private String			mQuhao;
	private String			username;
	private String			password;

	@Override
	public void init()
	{
		super.init();
		Bundle bundle = getIntent().getBundleExtra(VALUE_PASS);
		username = bundle.getString("username");
		password = bundle.getString("password");
	}

	@Override
	public void onBackPressed()
	{
		finishActivity(SplashUI.class);
	}

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_user_login);
		mTvQuhao = (TextView) findViewById(R.id.ul_tv_quhao);
		mEtNumber = (EditText) findViewById(R.id.ul_et_number);
		mEtPassword = (EditText) findViewById(R.id.ul_et_password);
		mCbJizhu = (CheckBox) findViewById(R.id.ul_cb_jizhu);
		mBtLogin = (Button) findViewById(R.id.ul_bt_login);
		mTvWangji = (TextView) findViewById(R.id.ul_tv_wangji);
		mTvRegist = (TextView) findViewById(R.id.ul_tv_regist);
		mContainerQuhao = (LinearLayout) findViewById(R.id.ul_container_quhao);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("登陆");
		/** 区号适配器数据 */
		mDatas = new ArrayList<String>();
		mDatas.add("+86");
		mDatas.add("+852");
		mAdapter = new QuhaoAdapter(this, mDatas);
		/** 初始化界面数据 */
		String lastQuhao = mSpUtils.getString("user_quhao", mDatas.get(0));// 上次的区号
		String lastMobile = mSpUtils.getString("user_mobile", "");// 上次的手机号
		String lastPassword = mSpUtils.getString("user_password", ""); // 上次的密码
		mEtNumber.setText(lastMobile);
		mEtPassword.setText(lastPassword);
		if (!TextUtils.isEmpty(lastQuhao) && !TextUtils.isEmpty(lastMobile) && !TextUtils.isEmpty(lastPassword))
		{
			mCbJizhu.setChecked(true);
		}
		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password))
		{
			mEtNumber.setText(username);
			mEtPassword.setText(password);
		}
	}

	@Override
	public void initListener()
	{
		super.initListener();
		mIvLeftHeader.setOnClickListener(this);
		mBtLogin.setOnClickListener(this);
		mTvRegist.setOnClickListener(this);
		mContainerQuhao.setOnClickListener(this);
		mTvWangji.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
		else if (v == mBtLogin)
		{
			doClickLogin();
		}
		else if (v == mTvWangji)
		{
			startActivity(UserFindPwdUI.class);
		}
		else if (v == mTvRegist)
		{
			startActivity(UserRegistUI.class);
		}
		else if (v == mContainerQuhao)
		{
			doClickQuhao();
		}
	}

	/** 区号选择 */
	private void doClickQuhao()
	{
		AlertDialog.Builder builder = new Builder(this);
		builder.setAdapter(mAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String quhao = (String) mAdapter.getItem(which);
				mTvQuhao.setText(quhao);
			}
		});
		builder.show();
	}

	/** 登陆 */
	private void doClickLogin()
	{
		mQuhao = mTvQuhao.getText().toString();
		mNumber = mEtNumber.getText().toString();
		mPassword = mEtPassword.getText().toString();
		if (TextUtils.isEmpty(mNumber))
		{
			ToastUtils.makeShortText(this, "请输入手机号码！");
			return;
		}
		if (TextUtils.isEmpty(mPassword))
		{
			ToastUtils.makeShortText(this, "请输入密码！");
			return;
		}

		ToastUtils.showProgress(this);
		String url = URLS.BASESERVER + URLS.User.login;
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		RequestParams params = new RequestParams();
		params.add("mobile", mNumber);
		params.add("password", mPassword);
		if(TextUtils.isEmpty(getDeviceId())){
			params.add("receive_id", MD5Utils.encode(getDeviceId()));
			PrintUtils.println("设备号=" +MD5Utils.encode(getDeviceId()) );
		}else {
			params.add("receive_id", MD5Utils.encode(mNumber));
			PrintUtils.println("手机号=" +MD5Utils.encode(mNumber) );
		}
		
		
		client.post(url, params, new MyAsyncResponseHandler() {
			@Override
			public void success(String json)
			{
				/** 是否保存账户信息 */
				if (mCbJizhu.isChecked())
				{
					mSpUtils.putString("user_quhao", mQuhao);
					mSpUtils.putString("user_mobile", mNumber);
					mSpUtils.putString("user_password", mPassword);
					mSpUtils.putString("user_type", "user");
				}
				else
				{
					mSpUtils.removeKey("user_quhao");
					mSpUtils.removeKey("user_mobile");
					mSpUtils.removeKey("user_password");
					mSpUtils.removeKey("user_type");
				}
				processJson(json);
			}

		});

	}

	private void processJson(String json)
	{
		Gson gson = new Gson();
		UserBean userBean = gson.fromJson(json, UserBean.class);
		// 保存全局用戶信息
		User user = userBean.content.member_data;
		user.access_token = userBean.content.access_token;
		user.password = mPassword;
		BaseApplication.setUser(user);
		if (MyConstains.OPEN_SERVICE)
		{
			ServiceUtil.invokeTimerUserService(this);
		}
		else if (MyConstains.OPEN_LONG_SERVICE)
		{
			startService(new Intent(this, UserConnectService.class));
		}
		startActivityAndFinish(YuyueUI.class);
	}
}
