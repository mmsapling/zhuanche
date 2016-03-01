package com.baidu.zhuanche.ui.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.JsonUtils;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.loopj.android.http.RequestParams;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.ui.user
 * @类名:	NewsDetailUI
 * @创建者:	陈选文
 * @创建时间:	2016-1-7	下午6:13:37 
 * @描述:	新闻详情
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class NewsDetailUI extends BaseActivity implements OnClickListener
{
	private WebView mWebView;
	private String	id;
	@Override
	public void init()
	{
		Bundle bundle = getIntent().getBundleExtra(VALUE_PASS);
		id = bundle.getString("id");
		super.init();
	}
	@Override
	public void initView()
	{
		setContentView(R.layout.ui_newsdetail);
		mWebView = (WebView) findViewById(R.id.newsdetail_webview);
	}
	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("新闻详情");
		String url = URLS.BASESERVER + URLS.User.article_detail;
		RequestParams params = new RequestParams();
		params.put("article_id", id);
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
		try
		{
			JSONObject object = new JSONObject(json);
			String data = object.getString("content");
			PrintUtils.print(data);
			mWebView.loadDataWithBaseURL(URLS.BASE, data, "text/html", "utf-8", null);
			//mWebView.loadData(data, "text/html", "UTF-8");
			mWebView.setWebViewClient(new WebViewClient(){
				
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon)
				{
					ToastUtils.showProgress(NewsDetailUI.this);
				}

				@Override
				public void onPageFinished(WebView view, String url)
				{
					ToastUtils.closeProgress();
				}
				
			});
			
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
	}
	@Override
	public void initListener()
	{
		mIvLeftHeader.setOnClickListener(this);
	}
	@Override
	public void onClick(View v)
	{
		if(v == mIvLeftHeader){
			finishActivity();
		}
	}
	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
}
