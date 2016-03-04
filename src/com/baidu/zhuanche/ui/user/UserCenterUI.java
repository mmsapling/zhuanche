package com.baidu.zhuanche.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.OrderAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.OrderListBean;
import com.baidu.zhuanche.bean.UserBean;
import com.baidu.zhuanche.bean.OrderListBean.OrderBean;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.connect.ServiceUtil;
import com.baidu.zhuanche.holder.InfoNameHolder;
import com.baidu.zhuanche.holder.InfoNameHolder.OnModifyNameListener;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.service.UserConnectService;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.AtoolsUtil;
import com.baidu.zhuanche.utils.MD5Utils;
import com.baidu.zhuanche.utils.OrderUtil;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.view.CircleImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserCenterUI extends BaseActivity implements OnClickListener, OnItemClickListener, OnRefreshListener2<ListView>, OnModifyNameListener
{

	private PullToRefreshListView	mListView;
	private RelativeLayout			mContainerSetting;	// 设置
	private RelativeLayout			mContainerMsg;		// 我的消息
	private CircleImageView			mCivPic;
	private User					mUser;
	private TextView				mTvNumber;
	private TextView				mTvName;
	private List<OrderBean>			mDatas;
	private OrderAdapter			mOrderAdapter;		// 订单适配器
	private int						currentpage	= 1;

	// private RelativeLayout mListEmptyView;
	@Override
	public void initView()
	{
		setContentView(R.layout.ui_user_center);
		// mListEmptyView = (RelativeLayout) findViewById(R.id.listview_empty);
		View headerView = View.inflate(this, R.layout.layout_uc_header, null);
		mContainerSetting = (RelativeLayout) headerView.findViewById(R.id.uc_container_setting);
		mContainerMsg = (RelativeLayout) headerView.findViewById(R.id.uc_container_msg);
		mCivPic = (CircleImageView) headerView.findViewById(R.id.uc_civ_pic);
		mTvNumber = (TextView) headerView.findViewById(R.id.uc_tv_number);
		mTvName = (TextView) headerView.findViewById(R.id.uc_tv_username);
		mListView = (PullToRefreshListView) findViewById(R.id.uc_listview);

		mListView.setMode(Mode.BOTH);
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
																			AbsListView.LayoutParams.WRAP_CONTENT);
		headerView.setLayoutParams(layoutParams);
		ListView lv = mListView.getRefreshableView();
		lv.addHeaderView(headerView);
		// mListView.scrollBy(0, -1);

	}
	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
	@Override
	protected void onRestart()
	{
		super.onRestart();
		mImageUtils.display(mCivPic, URLS.BASE + mUser.icon);
	}
	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("个人中心");
		mUser = BaseApplication.getUser();
		mImageUtils.display(mCivPic, URLS.BASE + mUser.icon);
		mTvNumber.setText(AtoolsUtil.mobile4(mUser.mobile));
		mTvName.setText(mUser.username);
		mDatas = new ArrayList<OrderListBean.OrderBean>();
		mOrderAdapter = new OrderAdapter(this, mDatas);
		mListView.setAdapter(mOrderAdapter);
		// mListEmptyView.setVisibility(0);
		// setEmptyView(mListView, "没有订单数据！");
		/*
		 * 去网络上加载订单列表数据,加载时显示进度条 加载完成后隐藏进度条
		 */
		ToastUtils.showProgress(this);
		loadMore();

	}

	public void loadMore()
	{
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.User.orderlist;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, mUser.access_token);
		params.put(URLS.CURRENTPAGER, "" + currentpage);
//		client.post(url, params, new AsyncHttpResponseHandler() {
//			
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
//			{
//				ToastUtils.closeProgress();
//				String json = new String(arg2);
//				/** 得到数据 */
//				processJson(json);
//			}
//			
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
//			{
//				ToastUtils.makeShortText("连接网络失败，请检查网络！");
//			}
//		});
		client.post(url, params, new MyAsyncResponseHandler() {
			
			@Override
			public void success(String json)
			{
				processJson(json);
			}
		});
				

	}

	/**
	 * 获得订单列表数据
	 * 
	 * @param json
	 */
	protected void processJson(String json)
	{
		Gson gson = new Gson();
		OrderListBean bean = gson.fromJson(json, OrderListBean.class);
		if (bean != null && bean.content.size() > 0 && bean.content !=null)
		{
			mDatas.addAll(bean.content);
			OrderUtil.sortOrder(mDatas);
			currentpage++;
			mOrderAdapter.notifyDataSetChanged();

		}
		mListView.onRefreshComplete();
		// mOrderAdapter = new OrderAdapter(this, mDatas);
		// mListView.setAdapter(mOrderAdapter);
	}

	@Override
	public void initListener()
	{
		super.initListener();
		mContainerMsg.setOnClickListener(this);
		mContainerSetting.setOnClickListener(this);
		mCivPic.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mIvLeftHeader.setOnClickListener(this);
		mListView.setOnRefreshListener(this);
		InfoNameHolder.setOnModifyNameListener(this);
		// mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
		//
		// @Override
		// public void onRefresh(PullToRefreshBase<ListView> refreshView)
		// {
		// setPullRefreshListUserLoadMoreData(refreshView);
		// mListView.postDelayed(new LoadMoreTask(), 1000);
		// }
		// });
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
		else if (v == mContainerMsg)
		{
			startActivity(UserMessageUI.class);
		}
		else if (v == mContainerSetting)
		{
			startActivity(SettingUI.class);
		}
		else if (v == mCivPic)
		{
			startActivity(UserInfoUI.class);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		if (position == 0) { return; }
		int index = position - 2;
		if(index == -1){
			return;
		}
		OrderBean orderBean = mDatas.get(index);
		Bundle bundle = new Bundle();
		bundle.putInt("position", index);
		bundle.putSerializable(MyConstains.ITEMBEAN, orderBean);
		startActivity(YuYueDetailUI.class, bundle);
	}

	private class LoadMoreTask implements Runnable
	{

		@Override
		public void run()
		{
			loadMore();
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
	{
		setPullRefreshListUserRefresh(refreshView);
		mDatas.clear();
		currentpage = 1;
		//mListView.postDelayed(new LoadMoreTask(), 1000);
		// String url = URLS.BASESERVER + URLS.User.login;
		// AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		// RequestParams params = new RequestParams();
		// params.add("mobile", mUser.mobile);
		// params.add("password", mUser.password);
		// params.add("receive_id", MD5Utils.encode(mUser.mobile));
		// client.post(url, params, new MyAsyncResponseHandler() {
		// @Override
		// public void success(String json)
		// {
		// processLoginJson(json);
		// }
		// });
		loadMore();
	}
		private void processLoginJson(String json)
		{
			Gson gson = new Gson();
			UserBean userBean = gson.fromJson(json, UserBean.class);
			// 保存全局用戶信息
			User user = userBean.content.member_data;
			user.access_token = userBean.content.access_token;
			user.password = mUser.password;
			BaseApplication.setUser(user);
			loadMore();
		}
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
	{
		setPullRefreshListUserLoadMoreData(refreshView);
		//mListView.postDelayed(new LoadMoreTask(), 1000);
		// String url = URLS.BASESERVER + URLS.User.login;
		// AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		// RequestParams params = new RequestParams();
		// params.add("mobile", mUser.mobile);
		// params.add("password", mUser.password);
		// params.add("receive_id", MD5Utils.encode(mUser.mobile));
		// client.post(url, params, new MyAsyncResponseHandler() {
		// @Override
		// public void success(String json)
		// {
		// processLoginJson(json);
		// }
		// });
		loadMore();
	}
	public void lianjie()
	{
		String url = URLS.BASESERVER + URLS.User.login;
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		RequestParams params = new RequestParams();
		params.add("mobile", mUser.mobile);
		params.add("password", mUser.password);
		params.add("receive_id", MD5Utils.encode(mUser.mobile));
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				String json = new String(arg2);
				Gson gson = new Gson();
				UserBean userBean = gson.fromJson(json, UserBean.class);
				// 保存全局用戶信息
				User user = userBean.content.member_data;
				user.access_token = userBean.content.access_token;
				user.password = mUser.password;
				BaseApplication.setUser(user);
				Log.d("tylz1", "login");
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
				ToastUtils.makeShortText("连接服务器失败！");
			}

		});
	}
	@Override
	public void onModifyName(String name)
	{
		mTvName.setText(name);
	}

}
