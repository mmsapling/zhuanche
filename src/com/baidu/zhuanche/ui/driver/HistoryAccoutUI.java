package com.baidu.zhuanche.ui.driver;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.AccountAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.AccountBean;
import com.baidu.zhuanche.bean.AccountBean.Account;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: TodayAccoutUI
 * @创建者: 陈选文
 * @创建时间: 2016-1-6 上午10:32:53
 * @描述: 历史进帐
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class HistoryAccoutUI extends BaseActivity implements OnClickListener, OnRefreshListener<ListView>
{
	private TextView				mTvMoney;
	private PullToRefreshListView	mListView;
	private List<Account>			mDatas		= new ArrayList<AccountBean.Account>();
	private String					mHistoryAccount;
	private int						currentPage	= 1;
	private AccountAdapter			mAdapter;

	@Override
	public void init()
	{
		super.init();
		Bundle bundle = getIntent().getBundleExtra(VALUE_PASS);
		mHistoryAccount = bundle.getString("historyAccount");
	}

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_historyaccout);
		mTvMoney = (TextView) findViewById(R.id.historyaccout_tv_money);
		mListView = (PullToRefreshListView) findViewById(R.id.historyaccout_listview);

	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("歷史進帳");
		mListView.setMode(Mode.PULL_FROM_END);
		mAdapter = new AccountAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);
		setEmptyView(mListView, "沒有相關記錄！");
		mTvMoney.setText(TextUtils.isEmpty(mHistoryAccount) ? "0.00" : mHistoryAccount);
		String url = URLS.BASESERVER + URLS.Driver.historyMoneyList;
		RequestParams params = new RequestParams();
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		params.add(URLS.CURRENTPAGER, "" + currentPage);
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
		Gson gson = new Gson();
		AccountBean accountBean = gson.fromJson(json, AccountBean.class);
		if (!isListEmpty(accountBean.content))
		{
			mDatas.addAll(accountBean.content);
			mAdapter.notifyDataSetChanged();
			currentPage++;
		}
		mListView.onRefreshComplete();
	}

	@Override
	public void initListener()
	{
		mIvLeftHeader.setOnClickListener(this);
		mListView.setOnRefreshListener(this);
	}

	@Override
	public void onBackPressed()
	{
		finishActivity();
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView)
	{
		setPullRefreshListDriverLoadMoreData(refreshView);
		mListView.postDelayed(new LoadMore(), 1000);
	}

	private class LoadMore implements Runnable
	{

		@Override
		public void run()
		{
			String url = URLS.BASESERVER + URLS.Driver.historyMoneyList;
			RequestParams params = new RequestParams();
			params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
			params.add(URLS.CURRENTPAGER, "" + currentPage);
			mClient.post(url, params, new MyAsyncResponseHandler() {

				@Override
				public void success(String json)
				{
					processJson(json);
				}
			});
		}

	}
}
