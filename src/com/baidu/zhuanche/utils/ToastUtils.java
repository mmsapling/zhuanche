package com.baidu.zhuanche.utils;

import android.content.Context;
import android.widget.Toast;

import com.baidu.zhuanche.view.DProgressDialog;

/**
 * @项目名: ZhuanChe
 * @包名: com.baidu.zhuanche.utils
 * @类名: ToastUtils
 * @创建者: 陈选文
 * @创建时间: 2015-12-4 上午9:04:06
 * @描述: toast打印
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class ToastUtils
{
	private static DProgressDialog	mPd;

	public static void makeShortText(Context context, String text)
	{
	
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void makeShortText(String text)
	{
		Toast.makeText(UIUtils.getContext(), text, Toast.LENGTH_SHORT).show();
	}

	public static void makeLongText(Context context, String text)
	{
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static void showProgress(Context context)
	{
		mPd = new DProgressDialog(context);
		
		mPd.setCanceledOnTouchOutside(true);
		mPd.setCancelable(true);
		mPd.show();
	}
	public static void closeProgress()
	{
		if (mPd != null)
		{
			mPd.dismiss();
			mPd = null;
		}

	}
}
