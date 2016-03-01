package com.baidu.zhuanche.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.HotAskAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.bean.NewListBean;
import com.baidu.zhuanche.bean.UserIndexBean.Article;
import com.baidu.zhuanche.bean.UserIndexBean.Cate;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.user
 * @类名: NewsListUI
 * @创建者: 陈选文
 * @创建时间: 2016-1-7 上午9:49:05
 * @描述: 新闻列表
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class NewsListUI extends BaseActivity implements OnClickListener, OnRefreshListener<ListView>, OnItemClickListener
{
	private Cate					mCate;
	private PullToRefreshListView	mListView;
	private List<Article>			mDatas		= new ArrayList<Article>();
	private HotAskAdapter			mAdapter;
	public int						currentPage	= 1;

	@Override
	public void init()
	{
		super.init();
		Bundle bundle = getIntent().getBundleExtra(VALUE_PASS);
		mCate = (Cate) bundle.getSerializable("cate");
	}

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_newslist);
		mListView = (PullToRefreshListView) findViewById(R.id.newslist_listview);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText(mCate.name);
		mListView.setMode(Mode.PULL_FROM_END);
		mAdapter = new HotAskAdapter(this, mDatas);
		setEmptyView(mListView, "没有新闻数据！");
		ToastUtils.showProgress(this);
		mListView.setAdapter(mAdapter);
		loadMore();
	}

	public void loadMore()
	{
		String url = URLS.BASESERVER + URLS.User.artilce_list;
		RequestParams params = new RequestParams();
		params.add("cate_id", mCate.id);
		params.add(URLS.CURRENTPAGER, "" + currentPage);
		mClient.get(url, params, new MyAsyncResponseHandler() {

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
		NewListBean newListBean = gson.fromJson(json, NewListBean.class);
		if (newListBean.content != null && newListBean.content.size() > 0)
		{
			mDatas.addAll(newListBean.content);
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
		mListView.setOnItemClickListener(this);
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
	public void onBackPressed()
	{
		finishActivity();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView)
	{
		setPullRefreshListUserLoadMoreData(refreshView);

		mListView.postDelayed(new LoadMore(), 1000);
	}

	class LoadMore implements Runnable
	{

		@Override
		public void run()
		{
			loadMore();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Bundle bundle = new Bundle();
		position = position - 1;
		if(position < 0){
			return;
		}
		Article article = mDatas.get(position);
		bundle.putString("id", article.id);
		startActivity(NewsDetailUI.class,bundle);
	}
}
