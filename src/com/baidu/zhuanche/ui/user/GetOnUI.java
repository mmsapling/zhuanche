package com.baidu.zhuanche.ui.user;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.bean.Location;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.utils.AMapUtil;
import com.baidu.zhuanche.utils.GpsCorrectUtils;
import com.baidu.zhuanche.utils.ToastUtils;


public class GetOnUI extends BaseActivity implements OnGeocodeSearchListener, AMapLocationListener
{
	private MapView							mMapView;
	private AMap							mAMap;
	private ImageView						mIvLeftArrow;
	private EditText						mSearchView;
	private GeocodeSearch					mGeocoderSearch;
	private ProgressDialog					mPogressDialog	= null;
	private MarkerOptions					mMarkerOptions;
	private static OnGetOnLocationListener	mLocationListener;

	// 声明AMapLocationClient类对象
	public AMapLocationClient				mLocationClient	= null;
	// 声明定位回调监听器
	public AMapLocationListener				mLocationListener1;

	public AMapLocationClientOption			mLocationOption	= null;
	private double							mLatitude		= 0;
	private double							mLongitude		= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_geton);
		mMapView = (MapView) findViewById(R.id.geton_mapview);
		mIvLeftArrow = (ImageView) findViewById(R.id.geton_iv_leftarrow);
		mIvLeftArrow.setImageResource(R.drawable.sure);
		mSearchView = (EditText) findViewById(R.id.geton_searchview);
		mMapView.onCreate(savedInstanceState);
		initActivity();
		initEvent();
		init1();
	}

	public void selectPlace()
	{
		// DAlertDialog dialog = new DAlertDialog(this);
		// dialog.setMessage("你确认选中此地址吗？");
		// dialog.addConfirmListener(new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which)
		// {
		// if(which == 1){
		// finishActivity();
		// }
		//
		// }
		// });
		// dialog.show();
		finishActivity();
	}

	/**
	 * 收起软键盘并设置提示文字
	 */
	public void collapseSoftInputMethod()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mMapView.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 关闭软键盘
	 */
	public static void closeKeyboard(Activity activity)
	{
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 得到InputMethodManager的实例
		if (imm.isActive())
		{
			imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private void init1()
	{
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

	private void initEvent()
	{
		mGeocoderSearch.setOnGeocodeSearchListener(this);
		mAMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng)
			{
				showDialog();
				double[] ll = new double[2];
				GpsCorrectUtils.transform(latLng.latitude, latLng.longitude, ll);
				RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
				mGeocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
			}
		});
		/** 地图长按事件 */
		mAMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng latLng)
			{
				showDialog();
				RegeocodeQuery query = new RegeocodeQuery(new
															LatLonPoint(latLng.latitude, latLng.longitude), 200,
															GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
				mGeocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求

			}
		});
		mSearchView.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
				{
					closeInputMethod();
					showDialog();
					String text = mSearchView.getText().toString().trim();
					GeocodeQuery q = new GeocodeQuery(text, "");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
					mGeocoderSearch.getFromLocationNameAsyn(q);// 设置同步地理编码请求
					return true;
				}
				return false;
			}
		});
		// /** 搜索框地点查询 */
		// mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
		//
		// @Override
		// public boolean onQueryTextSubmit(String query)
		// {
		// showDialog();
		// GeocodeQuery q = new GeocodeQuery(query, "");//
		// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		// mGeocoderSearch.getFromLocationNameAsyn(q);// 设置同步地理编码请求
		// return true;
		// }
		//
		// @Override
		// public boolean onQueryTextChange(String newText)
		// {
		// return false;
		// }
		// });
		/** Marker点击事件 */
		mAMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker)
			{
				return false;
			}
		});
		/** 菜单左侧图标点击事件 */
		mIvLeftArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				selectPlace();
			}
		});
	}

	private void initActivity()
	{
		if (mAMap == null)
		{
			mAMap = mMapView.getMap();
		}
		mGeocoderSearch = new GeocodeSearch(this);
		mPogressDialog = new ProgressDialog(this);
		// mSearchView.setSubmitButtonEnabled(true);
		mMarkerOptions = new MarkerOptions();
		mMarkerOptions.title("你的位置");
		// mMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pass));
		/** 通过反射修改searchview 改变其样式 */
		try
		{
			Field field = mSearchView.getClass().getDeclaredField("mSubmitButton");
			field.setAccessible(true);
			ImageView iv = (ImageView) field.get(mSearchView);
			iv.setImageDrawable(this.getResources().getDrawable(R.drawable.piature_122));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode)
	{
		dismissDialog();
		if (rCode == 0)
		{
			if (result != null && result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0)
			{
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				mMarkerOptions.snippet(address.getFormatAddress());
				mMarkerOptions.position(AMapUtil.convertToLatLng(address.getLatLonPoint()));
				mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(address.getLatLonPoint()), 18));
				mAMap.clear();
				mAMap.addMarker(mMarkerOptions);
				if (mLocationListener != null)
				{
					Location location = new Location();
					location.address = address.getFormatAddress();
					location.province = address.getProvince();
					location.city = address.getCity();
					location.district = address.getDistrict();
					location.latLng = AMapUtil.convertToLatLng(address.getLatLonPoint());
					mLocationListener.onGetOnLocation(location);
					mSearchView.setText(location.address);
				}
				mSearchView.clearFocus();
			}
			else
			{
				ToastUtils.makeShortText(this, "抱歉，未搜索到任何结果！");
			}
		}
		else
		{
			ToastUtils.makeShortText(this, "抱歉，查询失败！");
		}
	}

	/**
	 * 逆地理编码接口回调
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode)
	{
		dismissDialog();
		if (rCode == 0)
		{
			if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null)
			{
				RegeocodeQuery query = result.getRegeocodeQuery();
				mMarkerOptions.position(new LatLng(query.getPoint().getLatitude(), query.getPoint().getLongitude()));
				RegeocodeAddress address = result.getRegeocodeAddress();
				mMarkerOptions.snippet(address.getFormatAddress());
				mAMap.clear();
				mAMap.addMarker(mMarkerOptions);
				if (mLocationListener != null)
				{
					Location location = new Location();
					location.address = address.getFormatAddress();
					location.province = address.getProvince();
					location.city = address.getCity();
					location.district = address.getDistrict();
					location.latLng = new LatLng(query.getPoint().getLatitude(), query.getPoint().getLongitude());
					mLocationListener.onGetOnLocation(location);
					mSearchView.setText(location.address);
				}

			}
			else
			{
				ToastUtils.makeShortText(this, "抱歉，未搜索到任何结果！");
			}
		}
		else
		{
			ToastUtils.makeShortText(this, "抱歉，查询失败！");
		}
	}

	public static void setOnGetOnLocationListener(OnGetOnLocationListener listener)
	{
		mLocationListener = listener;
	}

	/** 地址接口回调 */
	public interface OnGetOnLocationListener
	{
		void onGetOnLocation(Location location);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		mMapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		mMapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mLocationClient != null)
		{
			mLocationClient.onDestroy();
			mLocationClient = null;
		}
		mMapView.onDestroy();
	}

	/**
	 * 
	 * 显示进度条对话框
	 */
	public void showDialog()
	{
		mPogressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mPogressDialog.setIndeterminate(false);
		mPogressDialog.setCancelable(true);
		mPogressDialog.setMessage("正在获取地址");
		mPogressDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog()
	{
		if (mPogressDialog != null)
		{
			mPogressDialog.dismiss();
		}
	}

	/** 隐藏软键盘 **/
	public void hideSoftBoard()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 得到InputMethodManager的实例
		if (imm.isActive())
		{
			// 如果开启
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
			// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
		}
	}

	/** 这个方法在这个类废弃 */
	@Deprecated
	@Override
	public void initView()
	{
	}

	@Override
	public void onBackPressed()
	{
		// super.onBackPressed();
		// finishActivity();
		selectPlace();
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
				// ToastUtils.makeShortText(mLatitude +"," + mLongitude);
				// mAMap.getMyLocation().setLongitude(mLongitude);
				// mAMap.getMyLocation().setLatitude(mLatitude);
				LatLng latLng = new LatLng(mLatitude, mLongitude);
				// mGeocoderSearch.
				showDialog();
				CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 21);
				mAMap.moveCamera(update);
				RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
				mGeocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
			}
			else
			{
				// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
				Log.e("AmapError", "location Error, ErrCode:"
									+ amapLocation.getErrorCode() + ", errInfo:"
									+ amapLocation.getErrorInfo());
			}
		}
		// closeKeyboard(this);
	}
}
