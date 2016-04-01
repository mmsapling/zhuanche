package com.baidu.zhuanche.ui.driver;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.DriverMsgRefreshAdapter;
import com.baidu.zhuanche.adapter.UserMsgAdapter;
import com.baidu.zhuanche.adapter.UserMsgRefreshAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.DriverMsgBean;
import com.baidu.zhuanche.bean.Msg;
import com.baidu.zhuanche.bean.UserMsgBean;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.zlist.widget.ZListView;
import com.baidu.zhuanche.zlist.widget.ZListView.IXListViewListener;
import com.loopj.android.http.RequestParams;


public class DriverMessageUI extends BaseActivity implements OnClickListener, OnItemClickListener, IXListViewListener
{
	private ZListView		mListView;
	private List<Msg>		mDatas	= new ArrayList<Msg>();
	private DriverMsgRefreshAdapter	mMsgAdapter;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_driver_message);
		mListView = (ZListView) findViewById(R.id.msg_listview);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("我的消息");
		mIvRightHeader.setImageResource(R.drawable.delete);
		mIvRightHeader.setVisibility(0);
		mMsgAdapter = new DriverMsgRefreshAdapter(this, mDatas);
		mListView.setAdapter(mMsgAdapter);
		setEmptyView(mListView, "沒有相關消息！");
		ToastUtils.showProgress(this);
		loadData();
	}

	
	private void loadData()
	{
		String url = URLS.BASESERVER + URLS.Driver.driverMessage;
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

	@Override
	public void onBackPressed()
	{
		finishActivity();         
	}
	protected void processJson(String json)
	{
		DriverMsgBean msgBean = mGson.fromJson(json, DriverMsgBean.class);
		if (!isListEmpty(msgBean.content.message))
		{
			mDatas.clear();
			mDatas.addAll(msgBean.content.message);
			mMsgAdapter.notifyDataSetChanged();
		}
		mListView.stopRefresh();
	}

	@Override
	public void initListener()
	{
		super.initListener();
		mIvLeftHeader.setOnClickListener(this);
		mIvRightHeader.setOnClickListener(this);
		//mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
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
			clearDatas();
		}
	}

	private void clearDatas()
	{
		String url = URLS.BASESERVER + URLS.Driver.cleanMessage;
		RequestParams params = new RequestParams();
		ToastUtils.showProgress(this);
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "消息清理成功！");
				mDatas.clear();
				mMsgAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
	{
		String url = URLS.BASESERVER + URLS.Driver.cleanMessage;
		RequestParams params = new RequestParams();
		final Msg msg = mDatas.get(position);
		ToastUtils.showProgress(this);
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		params.add("id", msg.id);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), msg.title + "消息已清理");
				mDatas.remove(position);
				mMsgAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onRefresh()
	{
		mListView.postDelayed(new RefreshData(), 1000);
	}

	@Override
	public void onLoadMore()
	{
		mListView.postDelayed(new RefreshData(), 1000);
	}
	private class RefreshData implements Runnable{

		@Override
		public void run()
		{
			loadData();
		}
		
	}
}
