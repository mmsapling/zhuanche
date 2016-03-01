package com.baidu.zhuanche.ui.driver;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.IdentityCheckBean;
import com.baidu.zhuanche.bean.IdentityCheckBean.Identity;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: IdentityError
 * @创建者: 陈选文
 * @创建时间: 2016-1-11 下午2:01:26
 * @描述: 審覈失敗
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class IdentityErrorUI extends BaseActivity implements OnClickListener
{
	private TextView mTvDes;
	private Button	mBtReEdit;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_identity_error);
		mBtReEdit = (Button) findViewById(R.id.error_bt_reedit);
		mTvDes = (TextView) findViewById(R.id.error_tv_des);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("審覈失敗");
		String url = URLS.BASESERVER + URLS.Driver.showVerifyInfo;
		RequestParams params = new RequestParams();
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		mClient.post(url, params, new MyAsyncResponseHandler() {
			
			@Override
			public void success(String json)
			{
				processJson(json);
			}
		});
	}

	protected void processJson(String json)
	{
		IdentityCheckBean identityCheckBean = mGson.fromJson(json, IdentityCheckBean.class);
		Identity identity = identityCheckBean.content;
		mTvDes.setText(identity.remark);
	}

	@Override
	public void initListener()
	{
		mIvLeftHeader.setOnClickListener(this);
		mBtReEdit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
		else if (v == mBtReEdit)
		{
			startActivityAndFinish(ReIdentityCheckUI.class);
		}
	}

	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
}
