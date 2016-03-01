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
import com.baidu.zhuanche.adapter.HomeGridAdapter;
import com.baidu.zhuanche.adapter.HotAskAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.bean.UserIndexBean;
import com.baidu.zhuanche.bean.UserIndexBean.Article;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.holder.UserHomePicHolder;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.ToastUtils;
import com.google.gson.Gson;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.user
 * @类名: MoreNewsUI
 * @创建者: 陈选文
 * @创建时间: 2016-2-19 上午8:35:02
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class MoreNewsUI extends BaseActivity implements OnItemClickListener
{
	public ListView			mListView;
	private List<Article>	mListDatas	= new ArrayList<Article>();
	private HotAskAdapter		mAskAdapter;
	@Override
	public void initView()
	{
		setContentView(R.layout.ui_morenews);
		mListView = (ListView) findViewById(R.id.morenews_listview);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("更多");
		mAskAdapter = new HotAskAdapter(this, mListDatas);
		mListView.setAdapter(mAskAdapter);
		setEmptyView(mListView, "没有更多的数据！");
		ToastUtils.showProgress(this);
		/** 数据 */
		String url = URLS.BASESERVER + URLS.User.index;
		mClient.post(url, new MyAsyncResponseHandler() {

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
	
		UserIndexBean userIndexBean = gson.fromJson(json, UserIndexBean.class);

		/** 新闻资讯数据 */
		if (userIndexBean.content.article != null && userIndexBean.content.article.size() > 0)
		{
			mListDatas.addAll(userIndexBean.content.article);
			mAskAdapter.notifyDataSetChanged();
		}
		// mListView.onRefreshComplete();
	}
	@Override
	public void initListener()
	{
		super.initListener();
		mListView.setOnItemClickListener(this);
		mIvLeftHeader.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				finishActivity();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Bundle bundle = new Bundle();
		Article article = mListDatas.get(position);
		bundle.putString("id", article.id);
		startActivity(NewsDetailUI.class, bundle);
	}
	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
}
