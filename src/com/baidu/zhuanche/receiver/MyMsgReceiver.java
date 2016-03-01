package com.baidu.zhuanche.receiver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.utils.ExampleUtil;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.ToastUtils;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.receiver
 * @类名: MyMsgReceiver
 * @创建者: 陈选文
 * @创建时间: 2016-1-12 下午2:29:59
 * @描述: 极光推送消息接受者
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class MyMsgReceiver extends BroadcastReceiver
{

	private static SoundPool	mSoundPool;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// initSoundPool(context);
		String action = intent.getAction();
		Bundle bundle = intent.getExtras();
		// play(mSRC.get(1), 2);
		/* 收到了通知 */
		if (action.equals("cn.jpush.android.intent.NOTIFICATION_RECEIVED"))
		{
			PrintUtils.println("action = " + action);
			if (mSoundPool == null)
			{
				mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
			}
			int soundId = mSoundPool.load(context, R.raw.tip, 1);
			mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 0);

		}else if(action.equals("cn.jpush.android.intent.MESSAGE_RECEIVED")){ //自定义消息
			String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			PrintUtils.println("收到自定义消息 + " + msg);
		}
	}
}
