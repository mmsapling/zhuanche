package com.baidu.zhuanche.ui.driver;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.JsonUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: DriverAccount
 * @创建者: 陈选文
 * @创建时间: 2016-1-5 下午2:36:22
 * @描述: 我的账户
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DriverAccount extends BaseActivity implements OnClickListener
{
	private TextView		mTvAccount;
	private TextView		mTvCanWithDraw;
	private TextView		mTvTodayAccount;
	private TextView		mTvHistoryAccount;
	private RelativeLayout	mContainerRecorder;
	private RelativeLayout	mContainerWithDraw;
	private AsyncHttpClient	mClient	= AsyncHttpClientUtil.getInstance();
	private Driver			mDriver	= BaseApplication.getDriver();
	@Override
	protected void onResume()
	{
		super.onResume();
		getAccount();
	}
	@Override
	public void initView()
	{
		setContentView(R.layout.ui_driver_account);
		mTvAccount = (TextView) findViewById(R.id.driver_account_tv_account);
		mTvCanWithDraw = (TextView) findViewById(R.id.driver_account_tv_canwithdraw);
		mTvTodayAccount = (TextView) findViewById(R.id.driver_account_tv_todayaccount);
		mTvHistoryAccount = (TextView) findViewById(R.id.driver_account_tv_historyaccount);
		mContainerRecorder = (RelativeLayout) findViewById(R.id.driveraccount_container_record);
		mContainerWithDraw = (RelativeLayout) findViewById(R.id.driveraccount_container_withdraw);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("我的賬戶");
		ToastUtils.showProgress(this);
		// 账户资金
		getAccount();
		// 今日进帐
		getTodayAccount();
		// 历史进帐
		getHistoryAccount();
	}

	public void getTodayAccount()
	{
		String urlToday = URLS.BASESERVER + URLS.Driver.todayMoney;
		RequestParams paramsToady = new RequestParams();
		paramsToady.add(URLS.ACCESS_TOKEN, mDriver.access_token);
		mClient.post(urlToday, paramsToady, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				try
				{
					JSONObject content = JsonUtils.getContent(json);
					String amount = content.getString("amount").trim();
					if (amount.equalsIgnoreCase("null"))
					{
						mTvTodayAccount.setText("0.00");
					}
					else
					{
						mTvTodayAccount.setText(amount);
					}
					// mTvTodayAccount.setText(TextUtils.isEmpty(amount) ?
					// "0.00" : amount);
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void getHistoryAccount()
	{
		String urlHistory = URLS.BASESERVER + URLS.Driver.historyMoney;
		RequestParams paramsHistory = new RequestParams();
		paramsHistory.add(URLS.ACCESS_TOKEN, mDriver.access_token);
		mClient.post(urlHistory, paramsHistory, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				try
				{
					JSONObject content = JsonUtils.getContent(json);
					String amount = content.getString("amount").trim();
					mTvHistoryAccount.setText(amount.equalsIgnoreCase("null") ? "0.00" : amount);
					// mTvTodayAccount.setText(TextUtils.isEmpty(amount) ?
					// "0.00" : amount);
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public void getAccount()
	{
		String urlAccount = URLS.BASESERVER + URLS.Driver.myBalance;
		RequestParams paramsAccount = new RequestParams();
		paramsAccount.add(URLS.ACCESS_TOKEN, mDriver.access_token);
		mClient.post(urlAccount, paramsAccount, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				try
				{
					JSONObject content = JsonUtils.getContent(json);
					String amount = content.getString("amount").trim();
					String withdraw = content.getString("withdraw").trim();
					mTvAccount.setText(amount);
					mTvCanWithDraw.setText(withdraw);
					// mTvTodayAccount.setText(TextUtils.isEmpty(amount) ?
					// "0.00" : amount);
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void initListener()
	{
		super.initListener();
		mIvLeftHeader.setOnClickListener(this);
		mContainerRecorder.setOnClickListener(this);
		mContainerWithDraw.setOnClickListener(this);
		mTvTodayAccount.setOnClickListener(this);
		mTvHistoryAccount.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
		else if (v == mContainerRecorder)
		{
			startActivity(WithDrawRecordUI.class);
		}
		else if (v == mContainerWithDraw)
		{
			String canCash = "0";
			canCash = mTvCanWithDraw.getText().toString().trim();
			Bundle bundle = new Bundle();
			bundle.putString("canCash", canCash);
			startActivity(ApplyCashUI.class, bundle);
		}
		else if (v == mTvTodayAccount)
		{
			String todayAccount = "0.00";
			todayAccount = mTvTodayAccount.getText().toString().trim();
			Bundle bundle = new Bundle();
			bundle.putString("todayAccount", todayAccount);
			startActivity(TodayAccoutUI.class, bundle);
		}
		else if (v == mTvHistoryAccount)
		{
			String historyAccount = "0.00";
			historyAccount = mTvHistoryAccount.getText().toString().trim();
			Bundle bundle = new Bundle();
			bundle.putString("historyAccount", historyAccount);
			startActivity(HistoryAccoutUI.class, bundle);
		}
	}

	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
}
