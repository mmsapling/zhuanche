package com.baidu.zhuanche.utils;


import org.apache.http.Header;

import android.content.Context;
import android.os.AsyncTask;

import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.bean.DriverBean;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.bean.UserBean;
import com.baidu.zhuanche.conf.URLS;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.utils
 * @类名: AsyncHttpCilentUtil
 * @创建者: 陈选文
 * @创建时间: 2015-12-24 下午3:08:56
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class HttpClientUtil
{
	private static AsyncHttpClient	client;

	public synchronized static AsyncHttpClient getInstance(Context paramContext)
	{
		if (client == null)
		{
			synchronized (HttpClientUtil.class)
			{
				client = new AsyncHttpClient();
				PersistentCookieStore myCookieStore = new PersistentCookieStore(paramContext);
				client.setCookieStore(myCookieStore);
			}

		}
		return client;
	}

	public static AsyncHttpClient getInstance()
	{
		return getInstance(UIUtils.getContext());
	}
}
