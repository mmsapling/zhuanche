package com.baidu.zhuanche.service;

import org.apache.http.Header;

import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.bean.UserBean;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

/**
 * @项目名: LongConnectionDemo
 * @包名: com.cxw.longconnectiondemo.service
 * @类名: UserConnectService
 * @创建者: 陈选文
 * @创建时间: 2016-1-27 下午2:53:02
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class UserConnectService extends Service
{

	private static final String	TAG	= "tylz";

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		Log.d(TAG, "用户服务开始连接...");
		lianjieUser();
	}
	@Override
	public void onDestroy()
	{
		Log.d(TAG, "销毁用户服务...");
	}
	/**
	 * 用户模块
	 */
	public void lianjieUser()
	{
		String url = URLS.BASESERVER + URLS.User.login;
		RequestParams params = new RequestParams();
		params.add("mobile", BaseApplication.getUser().mobile);
		params.add("password", BaseApplication.getUser().password);
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance(UIUtils.getContext());
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				String json = new String(arg2);
				Gson gson = new Gson();
				UserBean userBean = gson.fromJson(json, UserBean.class);
				// 保存全局用戶信息
				String password = BaseApplication.getUser().password;
				User user = userBean.content.member_data;
				user.password = password;
				user.access_token = userBean.content.access_token;
				
				BaseApplication.setUser(user);
				stopSelf();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
				stopSelf();
			}
		});
	}
}
