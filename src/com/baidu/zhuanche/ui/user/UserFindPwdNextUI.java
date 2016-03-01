package com.baidu.zhuanche.ui.user;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.ToastUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.ui.user
 * @类名:	UserFindPasswordUI
 * @创建者:	陈选文
 * @创建时间:	2015-12-29	上午10:11:55 
 * @描述:	用户找回密码界面
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class UserFindPwdNextUI extends BaseActivity implements OnClickListener
{

	private EditText	mEtPassword;
	private EditText	mEtRePassword;
	private Button		mBtConfirm;
	private String  	mVerifyCode;
	private String 		mMobile;
	@Override
	public void init()
	{
		super.init();
		mVerifyCode = getIntent().getStringExtra("code");
		mMobile = getIntent().getStringExtra("mobile");
	}
	@Override
	public void initView()
	{
		setContentView(R.layout.ui_user_findpassword_next);
		mEtPassword = (EditText) findViewById(R.id.uf_next_et_password);
		mEtRePassword = (EditText) findViewById(R.id.uf_next_et_repassword);
		mBtConfirm = (Button) findViewById(R.id.uf_next_bt_cofirm);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("找回密码");
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
		}else if(v == mBtConfirm){
			doClickConfirm();
		}
	}
	/**找回密码*/
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
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.User.findPassword;
		RequestParams params = new RequestParams();
		params.add("verify", mVerifyCode);
		params.add("mobile", mMobile);
		params.add("password", pwd);
		ToastUtils.showProgress(this);
		client.post(url, params, new MyAsyncResponseHandler() {
			
			@Override
			public void success(String json)
			{
				//加密后的新密码，这里没处理TODO
				ToastUtils.makeShortText(getApplicationContext(), "修改成功！");
				//跳转到登陆
				startActivityAndFinish(UserLoginUI.class);
			}
		});
		
		
	}

}
