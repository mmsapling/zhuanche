package com.baidu.zhuanche.ui.user;

import android.app.DownloadManager.Request;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.ui.user.UserHomeUI;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.ToastUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: DriverEditPasswordUI
 * @创建者: mmsapling
 * @创建时间: 2016-3-17 上午9:25:48
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class UserEditPwdUI extends BaseActivity implements OnClickListener
{
	private EditText	mEtOldPwd;
	private EditText	mEtNewPwd;
	private EditText	mEtConfirmPwd;
	private Button		mBtConfirm;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_user_editpassword);
		mEtOldPwd = (EditText) findViewById(R.id.user_et_oldpassword);
		mEtNewPwd = (EditText) findViewById(R.id.user_et_newpassword);
		mEtConfirmPwd = (EditText) findViewById(R.id.user_et_confirmpassword);
		mBtConfirm = (Button) findViewById(R.id.user_bt_cofirm);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("修改密码");
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
			confirm();
		}
	}

	/**
	 * 修改密碼
	 */
	private void confirm()
	{
		User user = BaseApplication.getUser();
		String oldPwd = mEtOldPwd.getText().toString();
		String newPwd = mEtNewPwd.getText().toString();
		String confirmPwd = mEtConfirmPwd.getText().toString();
		if (TextUtils.isEmpty(user.password)) { return; }
		if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmPwd))
		{
			ToastUtils.makeShortText("请输入密码！");
			return;
		}
		if (!user.password.equals(oldPwd))
		{
			ToastUtils.makeShortText("你的原始密码不正确！");
			return;
		}
		if (!newPwd.equals(confirmPwd))
		{
			ToastUtils.makeShortText("两次输入的新密码不一致！");
			return;
		}
		AsyncHttpClient instance = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.User.editpassword;
		RequestParams params = new RequestParams();
		params.add(URLS.ACCESS_TOKEN, user.access_token);
		params.add("oldpassword", oldPwd);
		params.add("newpassword", newPwd);
		ToastUtils.showProgress(this);
		instance.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				mSpUtils.removeKey("user_quhao");
				mSpUtils.removeKey("user_mobile");
				mSpUtils.removeKey("user_password");
				mSpUtils.removeKey("user_type");
				BaseApplication.setUser(null);
				ToastUtils.makeShortText("修改密码成功！");
				startActivity(UserHomeUI.class);
			}
		});
	}

}
