package com.baidu.zhuanche.ui.driver;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.DriverOrderListAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.bean.DriverCenterOrderListBean;
import com.baidu.zhuanche.bean.DriverCenterOrderListBean.OrderBean;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.ui.driver.CompleteUI.OnModifyListener;
import com.baidu.zhuanche.ui.driver.DriverCenterOrderDetailUI.OnChangeStatusListener;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.view.CircleImageView;
import com.baidu.zhuanche.view.NoScrollListView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: DriverUI
 * @创建者: 陈选文
 * @创建时间: 2015-12-31 下午3:36:29
 * @描述: 司机自己
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DriverUI extends BaseActivity implements OnClickListener, OnRefreshListener<ScrollView>, OnModifyListener, OnItemClickListener, OnChangeStatusListener
{
	private NoScrollListView		mListView;
	private PullToRefreshScrollView	mRefreshScrollView;
	private ScrollView				mScrollView;
	private CircleImageView			mCivPhoto;									// 头像
	private TextView				mTvName;									// 名称
	private RatingBar				mRatingBar;

	private TextView				mContainerIdentity;
	private TextView				mContainerMessage;
	private TextView				mContainerOrder;
	private TextView				mContainerAccount;
	private TextView				mContainerAssess;
	private List<OrderBean>			mDatas		= new ArrayList<OrderBean>();
	private DriverOrderListAdapter	mAdapter;
	private Driver					driver;
	private int						currentPage	= 1;

	@Override
	public void init()
	{
		super.init();
		driver = BaseApplication.getDriver();
	}
	@Override
	protected void onRestart()
	{
		super.onRestart();
		mListView.smoothScrollToPosition(0, 0);
	}
	@Override
	public void initView()
	{
		setContentView(R.layout.ui_driver);
		mListView = (NoScrollListView) findViewById(R.id.driver_listview);
		mRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.driver_scrollview);
		mCivPhoto = (CircleImageView) findViewById(R.id.driver_civ_photo);
		mTvName = (TextView) findViewById(R.id.driver_tv_name);
		mRatingBar = (RatingBar) findViewById(R.id.driver_ratingbar);

		mContainerIdentity = (TextView) findViewById(R.id.driver_tv_checkidentity);
		mContainerMessage = (TextView) findViewById(R.id.driver_tv_mymsg);
		mContainerOrder = (TextView) findViewById(R.id.driver_tv_allorder);
		mContainerAccount = (TextView) findViewById(R.id.driver_tv_myaccount);
		mContainerAssess = (TextView) findViewById(R.id.driver_tv_allassess);

	}

	@Override
	public void initData()
	{
		super.initData();
		CompleteUI.setOnModifyListener(this);
		DriverCenterOrderDetailUI.setOnChangeStatusListener(this);
		mTvTitle.setText("我");
		mIvRightHeader.setVisibility(0);
		mIvRightHeader.setImageResource(R.drawable.picture_19);
		mAdapter = new DriverOrderListAdapter(this, mDatas);
		mRefreshScrollView.setMode(Mode.PULL_FROM_END);
		mListView.setAdapter(mAdapter);
		setEmptyView(mListView, "沒有訂單列表數據");
		mImageUtils.display(mCivPhoto, URLS.BASE + driver.icon);
		mTvName.setText(driver.username);
		mRatingBar.setRating(Float.parseFloat(driver.star));
		ToastUtils.showProgress(this);
		// mRefreshScrollView.smoothScrollTo(0, 0);
		loadMore();
	}

	public void loadMore()
	{
		String url = URLS.BASESERVER + URLS.Driver.orderList;
		RequestParams params = new RequestParams();
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		params.add("limit", "" + 10);
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
		DriverCenterOrderListBean driverOrderListBean = gson.fromJson(json, DriverCenterOrderListBean.class);
		if (driverOrderListBean.content != null && driverOrderListBean.content.size() > 0)
		{
			mDatas.addAll(driverOrderListBean.content);
			mAdapter.notifyDataSetChanged();
			currentPage++;
		}
		mRefreshScrollView.onRefreshComplete();
	}

	@SuppressWarnings("deprecation")
	public void setEmptyView(ListView listview, String msg)
	{
		TextView emptyView = new TextView(this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		emptyView.setLayoutParams(params);
		emptyView.setText(msg);
		emptyView.setPadding(0, UIUtils.dip2Px(100), 0, 0);
		emptyView.setGravity(Gravity.CENTER);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listview.getParent()).addView(emptyView);
		listview.setEmptyView(emptyView);
	}

	@Override
	public void initListener()
	{
		super.initListener();
		mIvLeftHeader.setOnClickListener(this);
		mIvRightHeader.setOnClickListener(this);
		mContainerIdentity.setOnClickListener(this);
		mContainerMessage.setOnClickListener(this);
		mContainerOrder.setOnClickListener(this);
		mContainerAccount.setOnClickListener(this);
		mContainerAssess.setOnClickListener(this);
		mRefreshScrollView.setOnRefreshListener(this);
		mScrollView = mRefreshScrollView.getRefreshableView();
		mCivPhoto.setOnClickListener(this);
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
			startActivity(SettingUI.class);
		}
		else if (v == mContainerIdentity)
		{
			/*如果需求改變，需要調用下面doClickIdentity*/
			//startActivity(IdentityCheckUI.class);
			doClickIdentity();
		}
		else if (v == mContainerMessage)
		{
			// 我的消息
			startActivity(DriverMessageUI.class);
		}
		else if (v == mContainerOrder)
		{
			// 我的订单
			startActivity(DriverAllOrderUI.class);
		}
		else if (v == mContainerAccount)
		{
			// 我的账户
			startActivity(DriverAccount.class);
		}
		else if (v == mContainerAssess)
		{
			// 全部评价
			startActivity(DriverAllAssessUI.class);
		}else if(v == mCivPhoto){
			//完善資料
			startActivity(CompleteUI.class);
		}
	}

	private void doClickIdentity()
	{
		/*根據狀態是否審覈成功*/
		String status = BaseApplication.getDriver().status;
		if("4".equals(status)){
			startActivity(IdentityErrorUI.class);
		}else{
			startActivity(IdentityCheckUI.class);
		}
	}

	@Override
	public void onBackPressed()
	{
		finishActivity();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView)
	{
		String str = DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在加載");
		refreshView.getLoadingLayoutProxy().setPullLabel("上拉加載更多");
		refreshView.getLoadingLayoutProxy().setReleaseLabel("釋放開始加載");
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最後加載時間:" + str);
		mRefreshScrollView.postDelayed(new LoadMore(), 1000);
	}

	private class LoadMore implements Runnable
	{

		@Override
		public void run()
		{

			loadMore();
		}

	}

	@Override
	public void onModify(Bitmap bitmap,String name)
	{
		mCivPhoto.setImageBitmap(bitmap);
		mTvName.setText(name);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		OrderBean orderBean = mDatas.get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable(MyConstains.ITEMBEAN, orderBean);
		startActivity(DriverCenterOrderDetailUI.class,bundle);
	}
	@Override
	public void onChange(OrderBean orderBean)
	{
		int index = 0;
		for(int i = 0; i <mDatas.size(); i++){
			OrderBean bean = mDatas.get(i);
			if(bean.sn.equals(orderBean.sn)){
				index = i;
				break;
			}
		}
		mDatas.set(index, orderBean);
		mAdapter.notifyDataSetChanged();
	}
}
