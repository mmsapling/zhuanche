package com.baidu.zhuanche.base;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import cn.jpush.android.api.JPushInterface;

import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.bean.User;


/**
 * @创建者 Administrator
 * @创时间 2015-11-10 下午2:38:41
 * @描述 全局的,单例,共用,常用
 * 
 * @版本 $Rev: 27 $
 * @更新者 $Author: admin $
 * @更新时间 $Date: 2015-11-14 10:28:04 +0800 (星期六, 14 十一月 2015) $
 * @更新描述 TODO
 */
public class BaseApplication extends Application
{

	private static Context		mContext;												// member
	private static long			mMainThreadId;
	private static Handler		mMainThreadHandler;
	private static User			mUser;
	private static Driver		mDriver;

	/*--------------- 放置协议内容  begin---------------*/
	private Map<String, String>	mCacheJsonStringMap	= new HashMap<String, String>();

	public Map<String, String> getCacheJsonStringMap()
	{
		return mCacheJsonStringMap;
	}

	/*--------------- 放置协议内容 end ---------------*/

	public static Context getContext()
	{
		return mContext;
	}

	public static long getMainThreadId()
	{
		return mMainThreadId;
	}

	public static Handler getMainThreadHandler()
	{
		return mMainThreadHandler;
	}

	public static User getUser()
	{
		return mUser;
	}

	public static void setUser(User User)
	{
		mUser = User;
	}
	
	public static Driver getDriver()
	{
		return mDriver;
	}

	public static void setDriver(Driver driver)
	{
		mDriver = driver;
	}

	@Override
	public void onCreate()
	{// 程序入口方法

		// 1.上下文
		mContext = getApplicationContext();

		// 2.主线程的Id
		/**
		 * Tid Thread Pid Process Uid User
		 */
		mMainThreadId = android.os.Process.myTid();

		// 3.创建一个主线程的handler
		mMainThreadHandler = new Handler();

		// 4.用户信息
		mUser = new User();
		//JPushInterface.setDebugMode(true);
	    JPushInterface.init(this);
		super.onCreate();
	}
}
