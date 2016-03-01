package com.baidu.zhuanche.service;

import org.apache.http.Header;

import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.bean.DriverBean;
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
public class DriverConnectService extends Service
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
		Log.d(TAG, "司机服务开始连接...");
		lianjieDriver();
	}
	@Override
	public void onDestroy()
	{
		Log.d(TAG, "销毁司机服务...");
	}
	public void lianjieDriver()
	{
		String url = URLS.BASESERVER + URLS.Driver.login;
		RequestParams params = new RequestParams();
		params.add("mobile", BaseApplication.getDriver().mobile);
		params.add("password", BaseApplication.getDriver().password);
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance(UIUtils.getContext());
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				String json = new String(arg2);
				Gson gson = new Gson();
				DriverBean driverBean = gson.fromJson(json, DriverBean.class);
				// 保存全局用戶信息
				String password = BaseApplication.getDriver().password;
				Driver driver = driverBean.content.driver_data;
				driver.password = password;
				driver.access_token = driverBean.content.access_token;
				BaseApplication.setDriver(driver);
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
