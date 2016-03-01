package com.baidu.zhuanche.utils;

import android.util.Log;

public class PrintUtils {
	private static String TAG = "zhuanche";
	private static String TAG1 = "tylz";
	private static boolean isOut = true;
	public static void print(String mess) {
		if (isOut)
			Log.d(TAG, mess);
	}
	public static void println(String msg) {
		if (isOut)
			Log.d(TAG1, msg);
	}
}
