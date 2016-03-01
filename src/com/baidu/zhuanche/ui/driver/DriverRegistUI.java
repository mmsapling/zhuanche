package com.baidu.zhuanche.ui.driver;

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

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.SplashUI;
import com.baidu.zhuanche.adapter.QuhaoAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.ui.user.UserLoginUI;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.JsonUtils;
import com.baidu.zhuanche.utils.MD5Utils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.ui.driver
 * @类名:	DriverRegistUI
 * @创建者:	陈选文
 * @创建时间:	2015-12-29	上午9:51:32 
 * @描述:	TODO
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class DriverRegistUI extends BaseActivity implements OnClickListener
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
	public void initView()
	{
		setContentView(R.layout.ui_driver_regist);
		mContainerQuhao = (LinearLayout) findViewById(R.id.dr_container_quhao);
		mTvQuhao = (TextView) findViewById(R.id.dr_tv_quhao);
		mEtNumber = (EditText) findViewById(R.id.dr_et_number);
		mEtCode = (EditText) findViewById(R.id.dr_et_yanzhengma);
		mEtPassword = (EditText) findViewById(R.id.dr_et_password);
		mBtGetCode = (Button) findViewById(R.id.dr_bt_yanzhengma);
		mBtRegist = (Button) findViewById(R.id.dr_bt_regist);
	}
	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("手機註冊");
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
		}else if(v == mBtRegist){
			doClickRegist();
		}else if(v == mIvLeftHeader){
			finishActivity();
		}
	}
	/**注册*/
	private void doClickRegist()
	{
		String quhao = mTvQuhao.getText().toString(); //TODO 区号
		final String number = mEtNumber.getText().toString().trim();
		String code = mEtCode.getText().toString().trim();
		final String password = mEtPassword.getText().toString().trim();
		if(TextUtils.isEmpty(number) || TextUtils.isEmpty(code) || TextUtils.isEmpty(password) ){
			ToastUtils.makeShortText(this, "請完善信息！");
			return;
		}
		if(!code.equals(mVerifyCode)){
			ToastUtils.makeShortText(this, "驗證碼不對！");
			return;
		}
		String url = URLS.BASESERVER + URLS.Driver.regist;
		RequestParams params = new RequestParams();
		params.put(URLS.MOBILE, number);
		params.put(URLS.PASSWORD, password);
		params.put("area", quhao.substring(1));
		params.put(URLS.VERIFY_CODE, code);
		params.put("receive_id", MD5Utils.encode(number));
		mClient.post(url, params, new MyAsyncResponseHandler() {
			
			@Override
			public void success(String json)
			{
				ToastUtils.makeShortText(getApplicationContext(), "註冊成功，請填寫資料！");
				/*跳轉到資料審覈頁面*/
				Bundle bundle = new Bundle();
				bundle.putString("username", number);
				bundle.putString("password", password);
				startActivityAndFinish(DriverLoginUI.class,bundle);
			}
		});
	}

	/** 获取验证码 */
	private void doClickGetCode()
	{
		String number = mEtNumber.getText().toString().trim();
		if (TextUtils.isEmpty(number))
		{
			ToastUtils.makeShortText(this, "請輸入手機號碼！");
			return;
		}
		showTimeCountDown(mBtGetCode);
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.Driver.verify;
		RequestParams params = new RequestParams();
		params.put(URLS.MOBILE, number);
		ToastUtils.showProgress(this);
		client.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				/** 获取验证码,并未验证码赋值*/
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
