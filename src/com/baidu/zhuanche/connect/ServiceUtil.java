package com.baidu.zhuanche.connect;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.service.DriverConnectService;
import com.baidu.zhuanche.service.UserConnectService;
/**
 * Created by coder80 on 2014/10/31.
 */

public class ServiceUtil {
	/**
	 * 判段服务是否正在运行
	 * @param context
	 * @param className
	 * @return 在运行返回true  反之为false
	 */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = activityManager.getRunningServices(MyConstains.SERVICE_COUNT);

        if(null == serviceInfos || serviceInfos.size() < 1) {
            return false;
        }

        for(int i = 0; i < serviceInfos.size(); i++) {
            if(serviceInfos.get(i).service.getClassName().contains(className)) {
                isRunning = true;
                break;
            }
        }
        Log.i("ServiceUtil-AlarmManager", className + " isRunning =  " + isRunning);
        return isRunning;
    }
    /**执行用户alarm*/
    public static void invokeTimerUserService(Context context){
        Log.i("ServiceUtil-AlarmManager", "invokeTimerPOIService wac called.." );
        PendingIntent alarmSender = null;
        Intent startIntent = new Intent(context, UserConnectService.class);
        startIntent.setAction(MyConstains.ACTION_USER_NAME);
        try {
            alarmSender = PendingIntent.getService(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } catch (Exception e) {
            Log.i("ServiceUtil-AlarmManager", "服务开启失败 " + e.toString());
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), MyConstains.TIME_USER_PEROID, alarmSender);
    }
    /**执行司机alarm*/
    public static void invokeTimerDriverService(Context context){
        Log.i("ServiceUtil-AlarmManager", "invokeTimerDriverService wac called.." );
        PendingIntent alarmSender = null;
        Intent startIntent = new Intent(context, DriverConnectService.class);
        startIntent.setAction(MyConstains.ACTION_DRIVER_NAME);
        try {
            alarmSender = PendingIntent.getService(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } catch (Exception e) {
            Log.i("ServiceUtil-AlarmManager", "服务开启失败 " + e.toString());
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), MyConstains.TIME_DRIVER_PEROID, alarmSender);
    }
    /**
     * 关闭用户闹钟管理
     * @param context
     */
    public static void cancleUserAlarmManager(Context context){
        Log.i("tylz", "用户闹钟服务已经关闭...");
        Intent intent = new Intent(context,UserConnectService.class);
    	intent.setAction(MyConstains.ACTION_USER_NAME);
        PendingIntent pendingIntent=PendingIntent.getService(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm=(AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }
    /**
     * 关闭司机闹钟管理
     * @param context
     */
    public static void cancleDriverAlarmManager(Context context){
        Log.d("tylz", "司机闹钟服务已经关闭...");
        Intent intent = new Intent(context,DriverConnectService.class);
    	intent.setAction(MyConstains.ACTION_DRIVER_NAME);
        PendingIntent pendingIntent=PendingIntent.getService(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm=(AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }
}
