package com.baidu.zhuanche.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.SplashUI;
import com.baidu.zhuanche.adapter.QuhaoAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.helper.CountDownButtonHelper;
import com.baidu.zhuanche.helper.CountDownButtonHelper.OnFinishListener;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.JsonUtils;
import com.baidu.zhuanche.utils.MD5Utils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui
 * @类名: UserRegistUI
 * @创建者: 陈选文
 * @创建时间: 2015-12-26 下午2:23:52
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class UserRegistUI extends BaseActivity implements OnClickListener
{
	private EditText		mEtNumber;
	private TextView		mTvQuhao;
	private EditText		mEtPassword;
	private EditText		mEtCode;

	private Button			mBtGetCode;
	private Button			mBtRegist;
	private List<String>	mDatas;

	private LinearLayout	mContainerQuhao;
	private QuhaoAdapter	mAdapter;
	protected String		mVerifyCode	= "";
	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
	@Override
	public void initView()
	{
		setContentView(R.layout.ui_user_regist);
		mContainerQuhao = (LinearLayout) findViewById(R.id.ur_container_quhao);
		mTvQuhao = (TextView) findViewById(R.id.ur_tv_quhao);
		mEtNumber = (EditText) findViewById(R.id.ur_et_number);
		mEtCode = (EditText) findViewById(R.id.ur_et_yanzhengma);
		mEtPassword = (EditText) findViewById(R.id.ur_et_password);
		mBtGetCode = (Button) findViewById(R.id.ur_bt_yanzhengma);
		mBtRegist = (Button) findViewById(R.id.ur_bt_regist);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("手机注册");
		mDatas = new ArrayList<String>();
		mDatas.add("+86");
		mDatas.add("+852");
		mAdapter = new QuhaoAdapter(this, mDatas);

	}

	@Override
	public void initListener()
	{
		super.initListener();
		mContainerQuhao.setOnClickListener(this);
		mBtGetCode.setOnClickListener(this);
		mBtRegist.setOnClickListener(this);
		mIvLeftHeader.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mContainerQuhao)
		{
			doClickQuhao();
		}
		else if (v == mBtGetCode)
		{
			doClickGetCode();
		}
		else if (v == mBtRegist)
		{
			doClickRegist();
		}
		else if (v == mIvLeftHeader)
		{
			finishActivity();
		}
	}

	/** 注册 */
	private void doClickRegist()
	{
		String quhao = mTvQuhao.getText().toString(); // TODO 区号
		final String number = mEtNumber.getText().toString().trim();
		String code = mEtCode.getText().toString().trim();
		final String password = mEtPassword.getText().toString().trim();
		if (TextUtils.isEmpty(number) || TextUtils.isEmpty(code) || TextUtils.isEmpty(password))
		{
			ToastUtils.makeShortText(this, "请完善信息！");
			return;
		}
		if (!code.equals(mVerifyCode))
		{
			ToastUtils.makeShortText(this, "验证码不对！");
			return;
		}
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.User.register;
		RequestParams params = new RequestParams();
		params.put(URLS.MOBILE, number);
		params.put(URLS.PASSWORD, password);
		params.put(URLS.VERIFY_CODE, code);
		params.put("receive_id", MD5Utils.encode(number));
		client.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				ToastUtils.makeShortText(getApplicationContext(), "注册成功！");
				/** 跳转到登陆 */
				Bundle bundle = new Bundle();
				bundle.putString("username", number);
				bundle.putString("password", password);
				startActivityAndFinish(UserLoginUI.class,bundle);
			}
		});
	}
	/** 获取验证码 */
	private void doClickGetCode()
	{
		String number = mEtNumber.getText().toString().trim();
		if (TextUtils.isEmpty(number))
		{
			ToastUtils.makeShortText(this, "请输入手机号码！");
			return;
		}
		showTimeCountDown(mBtGetCode);
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.User.verify;
		RequestParams params = new RequestParams();
		params.put(URLS.MOBILE, number);
		ToastUtils.showProgress(this);
		client.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{

			
				/** 获取验证码,并未验证码赋值 */
				try
				{
					JSONObject content = JsonUtils.getContent(json);
					mVerifyCode = content.getString("verify");
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					ToastUtils.makeShortText(UIUtils.getContext(), "Json解析出错！");
				}
			}
		});
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

}
