package com.baidu.zhuanche.ui.user;

import java.io.File;

import android.content.DialogInterface;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.utils.DataCleanManager;
import com.baidu.zhuanche.utils.FileUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.view.DAlertDialog;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

public class SettingUI extends BaseActivity implements OnClickListener, OnToggleChanged
{
	private RelativeLayout	mContainerPassword;
	private RelativeLayout	mContainerCache;
	private RelativeLayout	mContainerMsg;
	private RelativeLayout	mContainerCheck;
	private RelativeLayout	mContainerUs;
	private RelativeLayout	mContainerFanKui;
	private Button			mBtLogout;
	private TextView		mTvCache;
	private ToggleButton	mTbToggle;
	private TextView		mTvSet07;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_user_setting);
		mContainerPassword = (RelativeLayout) findViewById(R.id.setting_xgpassword);
		mContainerCache = (RelativeLayout) findViewById(R.id.setting_container_cache);
		mContainerCheck = (RelativeLayout) findViewById(R.id.setting_container_check);
		mContainerFanKui = (RelativeLayout) findViewById(R.id.setting_container_fankui);
		mContainerMsg = (RelativeLayout) findViewById(R.id.setting_container_msg);
		mContainerUs = (RelativeLayout) findViewById(R.id.setting_container_us);
		mTvCache = (TextView) findViewById(R.id.setting_tv_cache);
		mTbToggle = (ToggleButton) findViewById(R.id.setting_tb_toggle);
		mBtLogout = (Button) findViewById(R.id.setting_bt_logout);
		mTvSet07 = (TextView) findViewById(R.id.set_text_07);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("设置");
		if (isReceiveUser)
		{
			mTbToggle.toggleOn();
			JPushInterface.resumePush(getApplicationContext());
		}
		else
		{
			mTbToggle.toggleOff();
			JPushInterface.stopPush(getApplicationContext());
		}
		mTvCache.setText(Formatter.formatFileSize(this, getCache()));
	}

	/**
	 * 获取缓存大小
	 * 
	 * @return
	 */
	private long getCache()
	{
		long cache = 0;
		try
		{
			cache = DataCleanManager.getFolderSize(new File(FileUtils.getCacheDir()));
			cache += DataCleanManager.getFolderSize(new File(FileUtils.getIconDir()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return cache;
	}
	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
	@Override
	public void initListener()
	{
		super.initListener();
		mIvLeftHeader.setOnClickListener(this);
		mContainerPassword.setOnClickListener(this);
		mContainerCache.setOnClickListener(this);
		mBtLogout.setOnClickListener(this);
		mTbToggle.setOnToggleChanged(this);
		mContainerFanKui.setOnClickListener(this);
		mContainerUs.setOnClickListener(this);
		mContainerCheck.setOnClickListener(this);
		mTvSet07.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
		else if (v == mContainerPassword)
		{
			startActivity(UserFindPasswordUI.class);
		}
		else if (v == mContainerCache)
		{
			clearCache();
		}
		else if (v == mBtLogout)
		{
			userLogout1();
		}
		else if (v == mContainerUs)
		{
			startActivity(CopyOfFeedBackUI.class);	
		}
		else if (v == mContainerFanKui)
		{
			startActivity(FeedBackUI.class);
		}
		
		else if (v == mContainerCheck)
		{
			ToastUtils.makeShortText(this, "当前已经是最新版本！");
		}
	}

	public void clearCache()
	{
		DAlertDialog dialog = new DAlertDialog(this);
		dialog.setMessage("是否清空缓存！");
		dialog.addConfirmListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(which == 1){
					if (getCache() == 0)
					{
						ToastUtils.makeShortText(SettingUI.this, "没有缓存！");
					}
					else
					{
						ToastUtils.makeShortText(SettingUI.this, "清理" + Formatter.formatFileSize(SettingUI.this, getCache()) + "缓存！");
						DataCleanManager.cleanCustomCache(FileUtils.getIconDir());
						DataCleanManager.cleanCustomCache(FileUtils.getCacheDir());
						mTvCache.setText(Formatter.formatFileSize(SettingUI.this, getCache()));
					}
				}
			}
		});
		dialog.show();
		
	}

	@Override
	public void onToggle(boolean on)
	{
		isReceiveUser = on;
		if (on)
		{
			JPushInterface.resumePush(getApplicationContext());
		}
		else
		{
			JPushInterface.stopPush(getApplicationContext());
		}
	}

	
}
