package com.baidu.zhuanche.base;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewDebug.FlagToString;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.SplashUI;
import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.bean.DriverBean;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.bean.UserBean;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.connect.ServiceUtil;
import com.baidu.zhuanche.helper.CountDownButtonHelper;
import com.baidu.zhuanche.helper.CountDownButtonHelper.OnFinishListener;
import com.baidu.zhuanche.service.DriverConnectService;
import com.baidu.zhuanche.service.UserConnectService;
import com.baidu.zhuanche.ui.driver.DriverLoginUI;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.ImageUtils;
import com.baidu.zhuanche.utils.MD5Utils;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.SPUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.view.DAlertDialog;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @创建者 Administrator
 * @创时间 2015-11-17 下午4:42:45
 * @描述 TODO
 * 
 * @版本 $Rev: 64 $
 * @更新者 $Author: admin $
 * @更新时间 $Date: 2015-11-17 16:56:48 +0800 (星期二, 17 十一月 2015) $
 * @更新描述 TODO
 */
public abstract class BaseActivity extends Activity
{
	/**
	 * 1.不用关心相关的生命周期方法,只需要关心自己定义的方法即可 2.可以决定哪些方法是必须实现,哪些方法是选择性实现
	 * 3.放置共有的属性或者方法-->减少代码的书写
	 */
	// activity的完全退出
	public static LinkedList<BaseActivity>	allActivitys	= new LinkedList<BaseActivity>();

	// 再按一次退出应用程序
	private long							mPreClickTime;
	// 属性
	public SPUtils							mSpUtils;

	public TextView							mTvTitle;												// 头标题
	public ImageView						mIvLeftHeader;											// 左菜单
	public ImageView						mIvRightHeader;
	public static final String				VALUE_PASS		= "pass_value";						// 传值
	public AsyncHttpClient					mClient			= AsyncHttpClientUtil.getInstance();
	public ImageUtils						mImageUtils		= new ImageUtils(this);
	public Gson								mGson;
	public boolean							isReceiveDriver	= true;								// 是否接受司机端消息
																									// 默认接受
	public boolean							isReceiveUser	= true;								// 是否接受用户端消息
																									// 默认接受

	public Timer							mTimer;

	// 右菜单
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mSpUtils = new SPUtils(this);
		mGson = new Gson();
		mTimer = new Timer();
		init();
		initView();
		initActionBar();
		initData();
		initListener();
	}

	@Override
	protected void onResume()
	{
		allActivitys.add(this);
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		allActivitys.remove(this);
		super.onDestroy();
	}

	public void init()
	{
		// TODO

	}

	public abstract void initView();

	public void initActionBar()
	{
		// TODO

	}

	public void initData()
	{
		// TODO
		mTvTitle = (TextView) findViewById(R.id.header_title);
		mIvLeftHeader = (ImageView) findViewById(R.id.header_leftMenu);
		mIvRightHeader = (ImageView) findViewById(R.id.header_rightMenu);
	}

	public void initListener()
	{

	}

	/**
	 * 完全退出
	 */
	public void exit()
	{
		BaseApplication.setUser(new User());
		BaseApplication.setDriver(new Driver());
		// stopService(new Intent(this, DriverService.class));
		// stopService(new Intent(this, MyService.class));
		for (BaseActivity activity : allActivitys)
		{
			activity.finish();
		}
	}

	@Override
	protected void onPause()
	{
		JPushInterface.onPause(this);
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putSerializable("user", BaseApplication.getUser());
		outState.putSerializable("driver", BaseApplication.getDriver());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null)
		{
			BaseApplication.setUser((User) savedInstanceState.getSerializable("user"));
			BaseApplication.setDriver((Driver) savedInstanceState.getSerializable("driver"));
		}
	}

	@Override
	public void onBackPressed()
	{
		if (this instanceof BaseActivity)
		{
			if (System.currentTimeMillis() - mPreClickTime > 2000)
			{// 两次连续点击的时间间隔>2s
				Toast.makeText(getApplicationContext(), "再按一次,退出应用程序", 0).show();
				mPreClickTime = System.currentTimeMillis();
				return;
			}
			else
			{// 点的快
				exit();// 完全退出
			}
		}
		else
		{
			super.onBackPressed();// finish
		}
	}

	/**
	 * 开启一个新界面,并且结束当前界面
	 * 
	 * @param clazz
	 */
	public void startActivityAndFinish(Class clazz)
	{
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
	}

	/**
	 * 开启一个新界面,并且结束当前界面
	 * 
	 * @param clazz
	 */
	public void startActivityAndFinish(Class clazz, Bundle bundle)
	{
		Intent intent = new Intent(this, clazz);
		intent.putExtra(VALUE_PASS, bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
	}

	/**
	 * 开启一个新界面
	 * 
	 * @param clazz
	 */
	public void startActivity(Class clazz)
	{
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
	}

	/**
	 * 开启一个新界面,并携带数据
	 * 
	 * @param clazz
	 */
	public void startActivity(Class clazz, Bundle bundle)
	{
		Intent intent = new Intent(this, clazz);
		intent.putExtra(VALUE_PASS, bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
	}

	/**
	 * 结束当前界面
	 * 
	 * @param clazz
	 */
	public void finishActivity()
	{
		finish();
		overridePendingTransition(R.anim.pre_enter, R.anim.pre_exit);
	}

	/**
	 * 开启一个新界面,并且结束当前界面
	 * 
	 * @param clazz
	 */
	public void finishActivity(Class clazz)
	{
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.pre_enter, R.anim.pre_exit);
	}

	/**
	 * 判段集合是否为空
	 * 
	 * @param collection
	 * @return true :为空；false 不为空
	 */
	public boolean isListEmpty(Collection collection)
	{
		if (collection.size() > 0 && collection != null) { return false; }
		return true;
	}

	/***
	 * 設置空視圖
	 * 
	 * @param listview
	 * @param msg
	 *            說明文字
	 */
	public void setEmptyView(ListView listview, String msg)
	{
		TextView emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		emptyView.setText(msg);
		emptyView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listview.getParent()).addView(emptyView);
		listview.setEmptyView(emptyView);
	}

	/***
	 * 設置空視圖
	 * 
	 * @param listview
	 * @param msg
	 *            說明文字
	 */
	public void setEmptyView(PullToRefreshListView listview, String msg)
	{
		ImageView emptyView = new ImageView(this);
		emptyView.setImageResource(R.drawable.cxw);
		//TextView emptyView = new TextView(this);
		//emptyView.setBackground(new d );
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		emptyView.setLayoutParams(params);
//		emptyView.setText(msg);
//		emptyView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		
		emptyView.setVisibility(View.GONE);
		((ViewGroup) listview.getParent()).addView(emptyView);
		listview.setEmptyView(emptyView);
	}

	/**
	 * 退出 司机
	 */
	public void userLogout()
	{
		DAlertDialog dialog = new DAlertDialog(this);
		dialog.setMessage("是否退出登陸！");
		dialog.addConfirmListener(new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (which == 1)
				{
					if (MyConstains.OPEN_SERVICE)
					{
						ServiceUtil.cancleDriverAlarmManager(BaseActivity.this);
					}
					else if (MyConstains.OPEN_LONG_SERVICE)
					{
						stopService(new Intent(BaseActivity.this, DriverConnectService.class));
					}
					// startActivityAndFinish(SplashUI.class);
					// 还有另外一种实现方法
					BaseApplication.setDriver(null);
					Intent intent = new Intent(BaseActivity.this,SplashUI.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			}
		});
		dialog.show();

	}

	/**
	 * 退出
	 */
	public void userLogout1()
	{
		// ToastUtils.makeShortText(getApplicationContext(), "你已成功退出！");
		DAlertDialog dialog = new DAlertDialog(this);
		dialog.setMessage("是否退出登陆！");
		dialog.addConfirmListener(new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (which == 1)
				{
					if (MyConstains.OPEN_SERVICE)
					{
						ServiceUtil.cancleUserAlarmManager(BaseActivity.this);
					}
					else if (MyConstains.OPEN_LONG_SERVICE)
					{
						stopService(new Intent(BaseActivity.this, UserConnectService.class));
					}
					//startActivityAndFinish(SplashUI.class);
					BaseApplication.setUser(null);
					Intent intent = new Intent(BaseActivity.this,SplashUI.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			}
		});
		dialog.show();

	}

	/**
	 * 上拉加載更多數據設置
	 * 
	 * @param refreshView
	 */
	public void setPullRefreshListDriverLoadMoreData(PullToRefreshBase<ListView> refreshView)
	{
		String str = DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在加載");
		refreshView.getLoadingLayoutProxy().setPullLabel("上拉加載更多");
		refreshView.getLoadingLayoutProxy().setReleaseLabel("釋放開始加載");
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最後加載時間:" + str);
	}

	/**
	 * 上拉加載更多數據設置
	 * 
	 * @param refreshView
	 */
	public void setPullRefreshListDriverData(PullToRefreshBase<ListView> refreshView)
	{
		String str = DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
		refreshView.getLoadingLayoutProxy().setPullLabel("下拉以刷新");
		refreshView.getLoadingLayoutProxy().setReleaseLabel("放開以刷新");
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最後刷新時間:" + str);
	}

	/**
	 * 上拉加載更多數據設置
	 * 
	 * @param refreshView
	 */
	public void setPullRefreshListUserRefresh(PullToRefreshBase<ListView> refreshView)
	{
		String str = DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		/** 上拉加载数据设置 */
		refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
		refreshView.getLoadingLayoutProxy().setPullLabel("下拉以刷新");
		refreshView.getLoadingLayoutProxy().setReleaseLabel("放开以刷新");
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后刷新时间:" + str);
	}

	/**
	 * 上拉加載更多數據設置
	 * 
	 * @param refreshView
	 */
	public void setPullRefreshListUserLoadMoreData(PullToRefreshBase<ListView> refreshView)
	{
		String str = DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		/** 上拉加载数据设置 */
		refreshView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
		refreshView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
		refreshView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
	}

	/**
	 * 获取Android手机唯一标志串
	 * 
	 * @return DEVICE_ID
	 */
	public String getDeviceId()
	{
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String DEVICE_ID = tm.getDeviceId();
		return DEVICE_ID;
	}

	/**
	 * 为用户端设置标签
	 */
	public void setUserAligs()
	{
		Set<String> tags = new HashSet<String>();
		tags.add(MD5Utils.encode(BaseApplication.getUser().mobile));
		JPushInterface.setAliasAndTags(this, "aligs", tags, new TagAliasCallback() {

			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2)
			{
				if (arg0 != 0)
				{
					PrintUtils.print("设置用户别名失败！");
				}
			}
		});
	}

	public void showTimeCountDown(final Button bt)
	{
		CountDownButtonHelper helper = new CountDownButtonHelper(bt, "发送验证码", 60, 1);
		helper.setOnFinishListener(new OnFinishListener() {

			@Override
			public void finish()
			{
				bt.setText("点击获取验证码");
			}
		});
		helper.start();
	}

	/**
	 * 为司机端设置标签
	 */
	public void setDriverAligs()
	{
		Set<String> tags = new HashSet<String>();
		tags.add(MD5Utils.encode(BaseApplication.getDriver().mobile));
		PrintUtils.print("司機別名=" + MD5Utils.encode(BaseApplication.getDriver().mobile));
		JPushInterface.setAliasAndTags(this, "aligs", tags, new TagAliasCallback() {

			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2)
			{
				if (arg0 != 0)
				{
					PrintUtils.print("设置司机别名失败！");
				}
			}
		});
	}

	/**
	 * 打开或关闭软件盘
	 */
	public void openInputMethod()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if (!isOpen)
		{
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 打开或关闭软件盘
	 */
	public void closeInputMethod()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if (isOpen)
		{
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
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
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
			}
		});
	}
	public  void lianjieUser()
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
				if (userBean.content != null)
				{
					User user = userBean.content.member_data;
					if (user != null && password != null)
					{
						user.password = password;
						user.access_token = userBean.content.access_token;
						BaseApplication.setUser(user);
						PrintUtils.println("用户连接成功！");
					}
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
			}
		});
	}
}
