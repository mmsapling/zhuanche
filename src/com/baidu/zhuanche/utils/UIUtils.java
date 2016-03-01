package com.baidu.zhuanche.utils;


import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.baidu.zhuanche.base.BaseApplication;

/**
 * @创建者	 Administrator
 * @创时间 	 2015-11-10 下午2:50:31
 * @描述	     处理和Ui相关的工具类
 *
 * @版本       $Rev: 45 $
 * @更新者     $Author: admin $
 * @更新时间    $Date: 2015-11-16 15:11:41 +0800 (星期一, 16 十一月 2015) $
 * @更新描述    TODO
 */
public class UIUtils {
	/**得到上下文*/
	public static Context getContext() {
		return BaseApplication.getContext();
	}

	/**得到Resource对象*/
	public static Resources getResources() {
		return getContext().getResources();
	}

	/**得到String.xml中的字符*/
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/**得到String.xml中的字符,带占位符*/
	public static String getString(int resId, Object... formatArgs) {
		return getResources().getString(resId, formatArgs);
	}

	/**得到String.xml中的字符数组*/
	public static String[] getStringArr(int resId) {
		return getResources().getStringArray(resId);
	}

	/**得到color.xml中的颜色值*/
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/**得到应用程序的包名*/
	public static String getPackageName() {
		return getContext().getPackageName();
	}

	/**得到主线程的id*/
	public static long getMainThreadId() {
		return BaseApplication.getMainThreadId();
	}

	/**得到主线程的hanlder*/
	public static Handler getMainThreadHandler() {
		return BaseApplication.getMainThreadHandler();
	}

	/** 安全的执行一个task*/
	public static void postTaskSafely(Runnable task) {
		// 如果当前线程是主线程==>直接执行
		// 如果当前线程是子线程==>通过消息机制发送到主线程
		long curThreadId = android.os.Process.myTid();
		long mainThreadId = getMainThreadId();
		if (curThreadId == mainThreadId) {// 在主线中
			task.run();
		} else {
			getMainThreadHandler().post(task);
		}
	}

	/**
	 * dip-->px
	 */
	public static int dip2Px(int dip) {
		// 1.px/(ppi/160)= dp
		// 2.px/(dpi/160)= dp
		// 3.px/dp = density

		// 320x480 ppi 160 1 1px=1dp
		// 480x800 ppi 240 1.5 1.5px = 1dp
		// 720x1280 ppi 320 2 2px = 1dp

		// 拿到px和dp的倍数关系
		int densityDpi = getResources().getDisplayMetrics().densityDpi;
		float density = getResources().getDisplayMetrics().density;

//		LogUtils.s("densityDpi:" + densityDpi + " density:" + density);

		int px = (int) (dip * density + .5f);

		return px;
	}

	/**
	 * px->dip
	 */
	public static int px2Dip(int px) {
		// px/dp = density
		float density = getResources().getDisplayMetrics().density;
		int dp = (int) (px / density + .5f);
		return dp;
	}

}
