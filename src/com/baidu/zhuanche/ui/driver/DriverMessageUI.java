package com.baidu.zhuanche.ui.driver;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.UserMsgAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.DriverMsgBean;
import com.baidu.zhuanche.bean.Msg;
import com.baidu.zhuanche.bean.UserMsgBean;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.loopj.android.http.RequestParams;

/**
 * 
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.ui.driver
 * @类名:	DriverMessageUI
 * @创建者:	陈选文
 * @创建时间:	2016-1-9	上午9:56:07 
 * @描述:	TODO
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class DriverMessageUI extends BaseActivity implements OnClickListener, OnItemClickListener
{
	private ListView		mListView;
	private List<Msg>		mDatas	= new ArrayList<Msg>();
	private UserMsgAdapter	mMsgAdapter;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_driver_message);
		mListView = (ListView) findViewById(R.id.msg_listview);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("我的消息");
		mIvRightHeader.setImageResource(R.drawable.delete);
		mIvRightHeader.setVisibility(0);
		mMsgAdapter = new UserMsgAdapter(this, mDatas);
		mListView.setAdapter(mMsgAdapter);
		setEmptyView(mListView, "沒有相關消息！");
		ToastUtils.showProgress(this);
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
			mDatas.addAll(msgBean.content.message);
			mMsgAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void initListener()
	{
		super.initListener();
		mIvLeftHeader.setOnClickListener(this);
		mIvRightHeader.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
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

}
