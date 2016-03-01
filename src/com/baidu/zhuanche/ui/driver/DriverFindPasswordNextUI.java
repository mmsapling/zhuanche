package com.baidu.zhuanche.ui.driver;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: DriverFindPasswordUI
 * @创建者: 陈选文
 * @创建时间: 2015-12-29 上午10:13:30
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DriverFindPasswordNextUI extends BaseActivity implements OnClickListener
{
	private EditText	mEtPassword;
	private EditText	mEtRePassword;
	private Button		mBtConfirm;
	private String		mCode;
	private String		mMobile;

	@Override
	public void init()
	{
		super.init();
		Intent intent = getIntent();
		mCode = intent.getStringExtra("code");
		mMobile = intent.getStringExtra("mobile");
	}

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_driver_findpassword_next);
		mEtPassword = (EditText) findViewById(R.id.df_next_et_password);
		mEtRePassword = (EditText) findViewById(R.id.df_next_et_repassword);
		mBtConfirm = (Button) findViewById(R.id.df_next_bt_cofirm);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("找回密碼");
	}

	@Override
	public void initListener()
	{
		super.initListener();
		mIvLeftHeader.setOnClickListener(this);
		mBtConfirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
		else if (v == mBtConfirm)
		{
			doClickConfirm();
		}
	}

	/** 找回密码 */
	private void doClickConfirm()
	{
		String pwd = mEtPassword.getText().toString();
		String repwd = mEtRePassword.getText().toString();
		if(TextUtils.isEmpty(pwd) || TextUtils.isEmpty(repwd)){
			ToastUtils.makeShortText(this, "请完善好信息！");
			return;
		}
		if(!pwd.equals(repwd)){
			ToastUtils.makeShortText(this, "两次输入密码不一致！");
			return;
		}
		String url = URLS.BASESERVER +URLS.Driver.findPassword;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		params.put(URLS.VERIFY_CODE,mCode);
		params.put(URLS.MOBILE, mMobile);
		params.put(URLS.PASSWORD, pwd);
		mClient.post(url, params, new MyAsyncResponseHandler() {
			
			@Override
			public void success(String json)
			{
				startActivity(DriverLoginUI.class);
			}
		});
	}
}
