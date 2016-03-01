package com.baidu.zhuanche.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.UserMsgAdapter;
import com.baidu.zhuanche.adapter.UserMsgRefreshAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.Msg;
import com.baidu.zhuanche.bean.UserMsgBean;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.zlist.widget.ZListView;
import com.baidu.zhuanche.zlist.widget.ZListView.IXListViewListener;
import com.loopj.android.http.RequestParams;

/**
 * 
 * @项目名: ZhuanChe
 * @包名: com.baidu.zhuanche.activity
 * @类名: MyMessageActivity
 * @创建者: 陈选文
 * @创建时间: 2015-12-21 上午10:09:06
 * @描述: 我的消息界面
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class UserMessageUI extends BaseActivity implements OnClickListener, IXListViewListener
{
	private ZListView				mListView;
	private List<Msg>				mDatas	= new ArrayList<Msg>();
	 //private UserMsgAdapter mMsgAdapter;
	private UserMsgRefreshAdapter	mMsgAdapter;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_user_message);
		mListView = (ZListView) findViewById(R.id.msg_listview);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("我的消息");
		mIvRightHeader.setImageResource(R.drawable.delete);
		mIvRightHeader.setVisibility(0);
		// mMsgAdapter = new UserMsgAdapter(this, mDatas);
		mMsgAdapter = new UserMsgRefreshAdapter(this, mDatas);
		mListView.setAdapter(mMsgAdapter);
		setEmptyView(mListView, "没有相关消息！");
		ToastUtils.showProgress(this);
		loadData();

	}
	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
	public void loadData()
	{
		String url = URLS.BASESERVER + URLS.User.myMessage;
		RequestParams params = new RequestParams();
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getUser().access_token);
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
		UserMsgBean msgBean = mGson.fromJson(json, UserMsgBean.class);
		
		if (!isListEmpty(msgBean.content))
		{
			mDatas.clear();
			mDatas.addAll(msgBean.content);
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
		mListView.setXListViewListener(this);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);
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
		String url = URLS.BASESERVER + URLS.User.cleanMessage;
		RequestParams params = new RequestParams();
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getUser().access_token);
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
	public void onRefresh()
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
	@Override
	public void onLoadMore()
	{
		//mListView.stopLoadMore();
		mListView.postDelayed(new RefreshData(), 1000);
	}


	

}
