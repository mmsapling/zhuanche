package com.baidu.zhuanche.ui.driver;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.WidthDrawRecordAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.WithDrawRecordBean;
import com.baidu.zhuanche.bean.WithDrawRecordBean.WithDraw;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: WithDrawRecordUI
 * @创建者: 陈选文
 * @创建时间: 2016-1-5 下午5:01:50
 * @描述: 提现记录
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class WithDrawRecordUI extends BaseActivity implements OnClickListener, OnRefreshListener<ListView>
{
	private PullToRefreshListView	mListView;
	// private List<String> mDatas;
	private List<WithDraw>			mDatas		= new ArrayList<WithDrawRecordBean.WithDraw>();
	private WidthDrawRecordAdapter	mAdapter;
	private int						currentPage	= 1;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_withdraw_record);
		mListView = (PullToRefreshListView) findViewById(R.id.wdr_listview);
	}

	@Override
	public void initData()
	{
		super.initData();
		mListView.setMode(Mode.PULL_FROM_END);
		mTvTitle.setText("提現記錄");
		mIvRightHeader.setVisibility(0);
		mIvRightHeader.setImageResource(R.drawable.page_22);
		mAdapter = new WidthDrawRecordAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);
		setEmptyView(mListView, "沒有相關記錄！");
		AsyncHttpClient mClient = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.Driver.myWithdraw;
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
		WithDrawRecordBean withDrawRecordBean = gson.fromJson(json, WithDrawRecordBean.class);
		if (withDrawRecordBean.content != null && withDrawRecordBean.content.size() > 0)
		{
			mDatas.addAll(withDrawRecordBean.content);
			mAdapter.notifyDataSetChanged();
			currentPage++;
		}
		mListView.onRefreshComplete();
	}

	@Override
	public void initListener()
	{
		mIvLeftHeader.setOnClickListener(this);
		mIvRightHeader.setOnClickListener(this);
		mListView.setMode(Mode.PULL_FROM_END);
		mListView.setOnRefreshListener(this);
		mIvRightHeader.setOnClickListener(this);
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
		else if (v == mIvRightHeader)
		{
			doClickRightMenu();
		}
		else if (v == mIvRightHeader)
		{
			deleteAllData();
		}
	}

	private void deleteAllData()
	{
		String url = URLS.BASESERVER + URLS.Driver.cleanWithdraw;
		RequestParams params = new RequestParams();
		params.add("id", "" + 0);
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		mClient.post(url, params, new MyAsyncResponseHandler() {
			
			@Override
			public void success(String json)
			{
				ToastUtils.makeShortText("清除成功！");
				mDatas.clear();
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	/** 清空记录 */
	private void doClickRightMenu()
	{
		mDatas.clear();
		currentPage = 1;
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView)
	{
		/** 上拉加载数据设置 */
		setPullRefreshListDriverLoadMoreData(refreshView);
		mListView.postDelayed(new MyTask(), 1000);

	}

	class MyTask implements Runnable
	{

		@Override
		public void run()
		{
			String url = URLS.BASESERVER + URLS.Driver.myWithdraw;
			RequestParams p = new RequestParams();
			p.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
			p.add(URLS.CURRENTPAGER, "" + currentPage);
			mClient.post(url, p, new MyAsyncResponseHandler() {

				@Override
				public void success(String json)
				{
					processJson(json);
				}
			});

		}

	}
}
