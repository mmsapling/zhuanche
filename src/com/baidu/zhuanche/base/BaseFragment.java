package com.baidu.zhuanche.base;

import java.util.Collection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;

/**
 * 
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.base
 * @类名: BaseFragmentCommon
 * @创建者: 陈选文
 * @创建时间: 2016-1-8 上午10:21:42
 * @描述: BaseFrament常规的抽取
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public abstract class BaseFragment extends Fragment
{
	public AsyncHttpClient	mClient	= AsyncHttpClientUtil.getInstance();
	public Gson				mGson	= new Gson();

	/**
	 * 1.不用关心相关的生命周期方法,只需要关心自己定义的方法即可 2.可以决定哪些方法是必须实现,哪些方法是选择性实现
	 * 3.放置共有的属性或者方法-->减少代码的书写
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		init();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return initView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		initData();
		initListener();
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * @des 初始化
	 * @call 选择性覆写
	 */
	public void init()
	{

	}

	/**
	 * @des 初始化视图
	 * @des 必须要实现,但是不知道具体实现,定义成为一个抽象方法,交给子类具体实现
	 * @call 强制性的覆写
	 */
	public abstract View initView();

	/**
	 * @des 初始化数据
	 * @call 子类选择性覆写
	 */
	public void initData()
	{

	}

	/**
	 * @des 初始化监听器
	 * @call 子类选择性覆写
	 */
	public void initListener()
	{

	}
	/**
	 * 判段集合是否为空
	 * @param collection
	 * @return
	 *   	 true :为空；false 不为空
	 */
	public boolean isListEmpty(Collection collection){
		if(collection.size() > 0 && collection != null){
			return false;
		}
		return true;
	}
	/***
	 * 設置空視圖
	 * 
	 * @param listView
	 * @param msg
	 *            說明文字
	 */
	public void setEmptyView(PullToRefreshListView listView, String msg)
	{
		TextView emptyView = new TextView(getActivity());
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		emptyView.setText(msg);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
	}
	/**
	 * 上拉加載更多數據設置
	 * @param refreshView
	 */
	public void setPullRefreshListDriverLoadMoreData(PullToRefreshBase<ListView> refreshView)
	{
		String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在加載");
		refreshView.getLoadingLayoutProxy().setPullLabel("上拉加載更多");
		refreshView.getLoadingLayoutProxy().setReleaseLabel("釋放開始加載");
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最後加載時間:" + str);
	}
	
}
