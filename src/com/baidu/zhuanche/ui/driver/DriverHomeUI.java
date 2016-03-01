package com.baidu.zhuanche.ui.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.http.Header;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.DriverHomeOrderAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.bean.DriverBean;
import com.baidu.zhuanche.bean.DriverHomeBean;
import com.baidu.zhuanche.bean.DriverHomeBean.DriverHomeOrder;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.ui.driver.AcceptOrderUI.OnReceiverOrderListener;
import com.baidu.zhuanche.ui.user.UserHomeUI;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.MD5Utils;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: DriverHomeUI
 * @创建者: 陈选文
 * @创建时间: 2015-12-31 下午1:58:49
 * @描述: 司机端首页
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DriverHomeUI extends BaseActivity implements
												OnClickListener,
												OnItemClickListener,
												AMapLocationListener,
												OnReceiverOrderListener,
												com.baidu.zhuanche.adapter.DriverHomeOrderAdapter.OnReceiverOrderListener
{
	private PullToRefreshListView	mListView;
	private DriverHomeOrderAdapter	mAdapter;
	private List<DriverHomeOrder>	mDatas			= new ArrayList<DriverHomeOrder>();
	private int						currentPage		= 1;
	// 声明AMapLocationClient类对象
	public AMapLocationClient		mLocationClient	= null;
	// 声明定位回调监听器
	public AMapLocationListener		mLocationListener;

	public AMapLocationClientOption	mLocationOption	= null;
	private double					mLatitude		= 0;
	private double					mLongitude		= 0;

	@Override
	public void init()
	{
		super.init();
		setDriverAligs();
		// 初始化定位
		mLocationClient = new AMapLocationClient(getApplicationContext());
		// 设置定位回调监听
		mLocationClient.setLocationListener(this);

		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		// 设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		// 设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(MyConstains.TIME_LOCATION);
		// 给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		// 启动定位
		mLocationClient.startLocation();
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		mLocationClient.startLocation();
	}

	@Override
	public void onBackPressed()
	{
		finishActivity(UserHomeUI.class);
	}

	@Override
	public void initView()
	{

		setContentView(R.layout.ui_driverhome);
		mListView = (PullToRefreshListView) findViewById(R.id.driverhome_listview);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("首頁");
		mListView.setMode(Mode.BOTH);
		mIvRightHeader.setVisibility(0);
		mIvLeftHeader.setVisibility(0);
		mIvRightHeader.setImageResource(R.drawable.picture_31);

		mAdapter = new DriverHomeOrderAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);
		setEmptyView(mListView, "沒有訂單列表數據");
		ToastUtils.showProgress(this);

	}

	/**
	 * 推送消息，暂时不用管测试用
	 * 
	 * @param alias
	 * @param tags
	 */
	protected void pushMsg(String alias, Set<String> tags)
	{
		String url = URLS.BASESERVER + URLS.Driver.pushMessage;
		RequestParams params = new RequestParams();
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		params.add("tags", MD5Utils.encode(BaseApplication.getDriver().mobile));
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
			}
		});
	}

	public void loadMore()
	{
		String url = URLS.BASESERVER + URLS.Driver.getorderList;
		RequestParams params = new RequestParams();
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		params.add("point", mLongitude + "," + mLatitude);
		params.add(URLS.CURRENTPAGER, "" + currentPage);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				processJson(json);
			}
		});
	}

	public void refresh()
	{
		String url = URLS.BASESERVER + URLS.Driver.getorderList;
		RequestParams params = new RequestParams();
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		params.add("point", mLongitude + "," + mLatitude);
		params.add(URLS.CURRENTPAGER, "" + 1);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				processRefreshJson(json);
			}
		});
	}

	protected void processJson(String json)
	{
		Gson gson = new Gson();
		DriverHomeBean driverHomeBean = gson.fromJson(json, DriverHomeBean.class);
		if (driverHomeBean.content != null && driverHomeBean.content.size() > 0)
		{
			mDatas.addAll(driverHomeBean.content);
			mAdapter.notifyDataSetChanged();
			currentPage++;
		}
		mListView.onRefreshComplete();
	}

	protected void processRefreshJson(String json)
	{
		Gson gson = new Gson();
		DriverHomeBean driverHomeBean = gson.fromJson(json, DriverHomeBean.class);
		if (driverHomeBean.content != null && driverHomeBean.content.size() > 0)
		{
			mDatas.clear();
			mDatas.addAll(driverHomeBean.content);
			mAdapter.notifyDataSetChanged();
			currentPage++;
		}
		mListView.onRefreshComplete();
	}

	@Override
	public void initListener()
	{
		super.initListener();
		mIvLeftHeader.setOnClickListener(this);
		AcceptOrderUI.setOnReceiverOrderListener(this);
		DriverHomeOrderAdapter.setOnReceiverOrderListener(this);
		mIvRightHeader.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView)
			{
				setPullRefreshListDriverData(refreshView);
				currentPage = 1;
				mListView.postDelayed(new Refresh(), 1000);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView)
			{

				setPullRefreshListDriverLoadMoreData(refreshView);
				mListView.postDelayed(new LoadMore(), 1000);
			}

		});
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			// finishActivity(SplashUI.class);
			finishActivity(UserHomeUI.class);
		}
		else if (v == mIvRightHeader)
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
					PrintUtils.println("重新登陆下");
					startActivity(DriverUI.class);
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
				{
					ToastUtils.makeShortText("网络请求失败!");
				}
			});
			
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
	{
		// TODO 这里需要传值，将本条目的值传过去
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
				Bundle bundle = new Bundle();
				bundle.putSerializable("orderbean", mDatas.get(position - 1));
				PrintUtils.println("重新登陆下");
				startActivity(AcceptOrderUI.class, bundle);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
				ToastUtils.makeShortText("网络请求失败!");
			}
		});
		
	}

	private class LoadMore implements Runnable
	{

		@Override
		public void run()
		{
			loadMore();
		}
	}

	private class Refresh implements Runnable
	{

		@Override
		public void run()
		{
			refresh();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mLocationClient.stopLocation();
		mLocationClient.onDestroy();
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation)
	{
		if (amapLocation != null)
		{
			if (amapLocation.getErrorCode() == 0)
			{
				mLatitude = amapLocation.getLatitude(); // 纬度
				mLongitude = amapLocation.getLongitude();// 经度
				loadMore();
			}
			else
			{
				loadMore();
				// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
				Log.e("AmapError", "location Error, ErrCode:"
									+ amapLocation.getErrorCode() + ", errInfo:"
									+ amapLocation.getErrorInfo());
			}
		}

	}

	@Override
	public void onReceiverOrder(DriverHomeOrder order)
	{
		/**
		 * 想要删掉不容易啊
		 */
		ListIterator<DriverHomeOrder> listIterator = mDatas.listIterator();
		while (listIterator.hasNext())
		{
			DriverHomeOrder next = listIterator.next();
			if (next.sn.equals(order.sn))
			{
				listIterator.remove();
			}
		}
		mAdapter.notifyDataSetChanged();
	}

}
