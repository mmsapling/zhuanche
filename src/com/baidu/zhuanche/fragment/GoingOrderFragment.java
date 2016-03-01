package com.baidu.zhuanche.fragment;

import java.util.ArrayList;
import java.util.List;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.FragmentOrderListAdapter;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.base.BaseFragment;
import com.baidu.zhuanche.bean.DriverAllOrderBean;
import com.baidu.zhuanche.bean.DriverCenterOrderListBean.OrderBean;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.fragment
 * @类名: AllOrderFragment
 * @创建者: 陈选文
 * @创建时间: 2016-1-8 上午10:18:13
 * @描述: 待出发訂單
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class GoingOrderFragment extends BaseFragment implements OnRefreshListener<ListView>
{
	private PullToRefreshListView		mListView;
	private List<OrderBean>				mDatas		= new ArrayList<OrderBean>();
	private FragmentOrderListAdapter	mAdapter;
	private int							currentPage	= 1;

	@Override
	public View initView()
	{
		View view = View.inflate(getActivity(), R.layout.fragment_driver_order, null);
		mListView = (PullToRefreshListView) view.findViewById(R.id.order_listview);
		return view;
	}
	@Override
	public void onPause()
	{
		currentPage = 1;
		super.onPause();
	}
	@Override
	public void initData()
	{
		mListView.setMode(Mode.PULL_FROM_END);
		mDatas = new ArrayList<OrderBean>();
		mAdapter = new FragmentOrderListAdapter(getActivity(), mDatas);
		mListView.setAdapter(mAdapter);
		setEmptyView(mListView, "沒有訂單數據！");
		ToastUtils.showProgress(getActivity());
		loadMore();
	}

	protected void processJson(String json)
	{
		DriverAllOrderBean orderBean = mGson.fromJson(json, DriverAllOrderBean.class);
		if (orderBean.content != null && orderBean.content.size() > 0)
		{
			mDatas.addAll(orderBean.content);
			mAdapter.notifyDataSetChanged();
			currentPage++;
		}
		mListView.onRefreshComplete();
	}

	@Override
	public void initListener()
	{
		mListView.setOnRefreshListener(this);
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
			loadMore();
		}

	}

	public void loadMore()
	{
		String url = URLS.BASESERVER + URLS.Driver.orderList;
		RequestParams params = new RequestParams();
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		params.add(URLS.CURRENTPAGER, "" + currentPage);
		params.add("status", "1");
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				processJson(json);
			}
		});
	}
}
