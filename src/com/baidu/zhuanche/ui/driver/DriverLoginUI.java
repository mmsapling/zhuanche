package com.baidu.zhuanche.ui.driver;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.bean.DriverBean;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.connect.ServiceUtil;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.service.DriverConnectService;
import com.baidu.zhuanche.service.UserConnectService;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.MD5Utils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: DriverLoginUI
 * @创建者: 陈选文
 * @创建时间: 2015-12-29 上午9:37:22
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DriverLoginUI extends BaseActivity implements OnClickListener
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
	public void initView()
	{
		setContentView(R.layout.ui_driver_login);
		mTvQuhao = (TextView) findViewById(R.id.dl_tv_quhao);
		mEtNumber = (EditText) findViewById(R.id.dl_et_number);
		mEtPassword = (EditText) findViewById(R.id.dl_et_password);
		mCbJizhu = (CheckBox) findViewById(R.id.dl_cb_jizhu);
		mBtLogin = (Button) findViewById(R.id.dl_bt_login);
		mTvWangji = (TextView) findViewById(R.id.dl_tv_wangji);
		mTvRegist = (TextView) findViewById(R.id.dl_tv_regist);
		mContainerQuhao = (LinearLayout) findViewById(R.id.dl_container_quhao);
	}

	@Override
	public void onBackPressed()
	{
		finishActivity(SplashUI.class);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("登陸");
		/** 区号适配器数据 */
		mDatas = new ArrayList<String>();
		mDatas.add("+86");
		mDatas.add("+852");
		mAdapter = new QuhaoAdapter(this, mDatas);
		/** 初始化界面数据 */
		String lastQuhao = mSpUtils.getString("driver_quhao", mDatas.get(0));// 上次的区号
		String lastMobile = mSpUtils.getString("driver_mobile", "");// 上次的手机号
		String lastPassword = mSpUtils.getString("driver_password", ""); // 上次的密码
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
			finishActivity(SplashUI.class);
		}
		else if (v == mBtLogin)
		{
			// startActivityAndFinish(DriverHomeUI.class);
			doClickLogin();
		}
		else if (v == mTvWangji)
		{
			startActivity(DriverFindPwdUI.class);
		}
		else if (v == mTvRegist)
		{
			startActivity(DriverRegistUI.class);
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
			ToastUtils.makeShortText(this, "請輸入手機號碼！");
			return;
		}
		if (TextUtils.isEmpty(mPassword))
		{
			ToastUtils.makeShortText(this, "請輸入密碼！");
			return;
		}
		ToastUtils.showProgress(this);
		String url = URLS.BASESERVER + URLS.Driver.login;
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		RequestParams params = new RequestParams();
		params.add("receive_id", MD5Utils.encode(mNumber));
		params.add("mobile", mNumber);
		params.add("password", mPassword);
		client.post(url, params, new MyAsyncResponseHandler() {
			@Override
			public void success(String json)
			{
				/** 是否保存账户信息 */
				if (mCbJizhu.isChecked())
				{
					mSpUtils.putString("driver_quhao", mQuhao);
					mSpUtils.putString("driver_mobile", mNumber);
					mSpUtils.putString("driver_password", mPassword);
					mSpUtils.putString("user_type", "driver");
				}
				else
				{
					mSpUtils.removeKey("driver_quhao");
					mSpUtils.removeKey("driver_mobile");
					mSpUtils.removeKey("driver_password");
					mSpUtils.removeKey("user_type");
				}
				processJson(json);
			}

		});

	}

	private void processJson(String json)
	{
		Gson gson = new Gson();
		DriverBean driverBean = gson.fromJson(json, DriverBean.class);
		Driver driver = BaseApplication.getDriver();
		driver = driverBean.content.driver_data;
		driver.access_token = driverBean.content.access_token;
		driver.password = mPassword;
		BaseApplication.setDriver(driver);

		/* 判段賬號是否禁用 */
		if ("0".equals(driver.status))
		{
			ToastUtils.makeShortText(this, "此帳號被禁用！");
			return;
		}
		else if ("1".equals(driver.status))
		{
			startActivity(ReIdentityCheckUI.class);
		}
		else if ("2".equals(driver.status))
		{
			startActivity(IdentityCheckUI.class);
		}
		else if ("4".equals(driver.status))
		{
			startActivity(IdentityErrorUI.class);
		}
		else
		{
			if (MyConstains.OPEN_SERVICE)
			{
				ServiceUtil.invokeTimerDriverService(this);
			}
			else if (MyConstains.OPEN_LONG_SERVICE)
			{
				startService(new Intent(this, DriverConnectService.class));
			}
			startActivityAndFinish(DriverHomeUI.class);
		}

	}
}
