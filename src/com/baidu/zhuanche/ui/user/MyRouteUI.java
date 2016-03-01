package com.baidu.zhuanche.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.bean.OrderListBean.OrderBean;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.utils.AMapUtil;
import com.baidu.zhuanche.utils.ToastUtils;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.user
 * @类名: MyRouteUI
 * @创建者: 陈选文
 * @创建时间: 2015-12-30 下午2:29:43
 * @描述: 我的行程
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class MyRouteUI extends Activity implements OnPoiSearchListener, OnRouteSearchListener
{
	private ProgressDialog		mProgDialog		= null;						// 搜索时进度条
	private PoiResult			mPoiResult;									// poi返回的结果
	private int					mCurrentPage	= 0;							// 当前页面，从0开始计数

	private PoiSearch			mPoiSearch;									// POI搜索
	private RouteSearch			mRouteSearch;
	private int					mDrivingMode	= RouteSearch.DrivingDefault;	// 驾车默认模式
	private DriveRouteResult	mDriveRouteResult;								// 驾车模式查询结果

	private PoiSearch.Query		mStartQuery;									// Poi查询条件类
	private PoiSearch.Query		mMiddleQuery;
	private PoiSearch.Query		mEndQuery;

	private LatLonPoint			mStartPoint		= null;
	private LatLonPoint			mEndPoint		= null;
	private LatLonPoint			mMiddlePoint	= null;

	private MapView				mMapView;
	private AMap				mAMap;
	private OrderBean			mOrderBean;

	private ImageView			mIvLeft;
	private TextView			mTvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_myroute);
		Bundle bundle = getIntent().getBundleExtra(BaseActivity.VALUE_PASS);
		mOrderBean = (OrderBean) bundle.getSerializable(MyConstains.ITEMBEAN);
		if(mOrderBean.seaport.equals("不指定")){
			mOrderBean.seaport = "皇岗";
		}
		mMapView = (MapView) findViewById(R.id.myroute_mapview);
		mAMap = mMapView.getMap();
		mMapView.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
	}

	public void initView()
	{
		mIvLeft = (ImageView) findViewById(R.id.header_leftMenu);
		mTvTitle = (TextView) findViewById(R.id.header_title);
	}

	public void initData()
	{
		mTvTitle.setText("行程路线");
		mRouteSearch = new RouteSearch(this);
	}

	public void initListener()
	{
		mRouteSearch.setRouteSearchListener(this);
		mIvLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				finishActivity();
			}
		});
		mAMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0)
			{
				return false;
			}
		});
		doSearchStartQuery();
	}

	/**
	 * 开始进行 起点 poi搜索
	 */
	protected void doSearchStartQuery()
	{
		showProgressDialog();// 显示进度框
		mStartQuery = new PoiSearch.Query(mOrderBean.from, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		mStartQuery.setPageSize(1);// 设置每页最多返回多少条poiitem
		mStartQuery.setPageNum(mCurrentPage);// 设置查第一页

		mPoiSearch = new PoiSearch(this, mStartQuery);
		mPoiSearch.setOnPoiSearchListener(this);
		mPoiSearch.searchPOIAsyn();
	}

	/**
	 * 开始进行 途经点 poi搜索
	 */
	protected void doSearchMiddleQuery()
	{
		mMiddleQuery = new PoiSearch.Query(mOrderBean.seaport + "口岸", "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		mMiddleQuery.setPageSize(1);// 设置每页最多返回多少条poiitem
		mMiddleQuery.setPageNum(mCurrentPage);// 设置查第一页

		mPoiSearch = new PoiSearch(this, mMiddleQuery);
		mPoiSearch.setOnPoiSearchListener(this);
		mPoiSearch.searchPOIAsyn();
	}

	/**
	 * 开始进行 终点 poi搜索
	 */
	protected void doSearchEndQuery()
	{
		mEndQuery = new PoiSearch.Query(mOrderBean.to, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		mEndQuery.setPageSize(1);// 设置每页最多返回多少条poiitem
		mEndQuery.setPageNum(mCurrentPage);// 设置查第一页

		mPoiSearch = new PoiSearch(this, mEndQuery);
		mPoiSearch.setOnPoiSearchListener(this);
		mPoiSearch.searchPOIAsyn();
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode)
	{
		if (rCode == 0)
		{
			if (result != null && result.getQuery() != null)
			{
				if (result.getQuery().equals(mStartQuery))
				{
					mPoiResult = result;
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = mPoiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					if (poiItems != null && poiItems.size() > 0)
					{
						mAMap.clear();// 清理之前的图标
						mStartPoint = poiItems.get(0).getLatLonPoint();
						doSearchMiddleQuery();
					}
				}
				else if (result.getQuery().equals(mMiddleQuery))
				{
					mPoiResult = result;
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = mPoiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					if (poiItems != null && poiItems.size() > 0)
					{
						mAMap.clear();// 清理之前的图标
						mMiddlePoint = poiItems.get(0).getLatLonPoint();
						doSearchEndQuery();
					}
				}
				else if (result.getQuery().equals(mEndQuery))
				{

					mPoiResult = result;
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = mPoiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					if (poiItems != null && poiItems.size() > 0)
					{
						mEndPoint = poiItems.get(0).getLatLonPoint();
						RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
						// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
						List<LatLonPoint> middleList = new ArrayList<LatLonPoint>();// 途经点
						middleList.add(mMiddlePoint);
						DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mDrivingMode, middleList, null, "");
						mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
						addMiddlePointMarker();
					}
				}
			}else{
				ToastUtils.makeShortText(this, "查询为空！");
			}

		}
		else
		{
			ToastUtils.makeShortText(this, "地点查询失败！");
		}
	}
	/**
	 * 添加途经点图标
	 */
	public void addMiddlePointMarker()
	{
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.title("途径口岸");
		//markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pass));
		markerOptions.position(AMapUtil.convertToLatLng(mMiddlePoint));
		markerOptions.snippet(mOrderBean.seaport);
		mAMap.addMarker(markerOptions);
	}

	/**
	 * 驾车结果回调
	 */
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode)
	{
		dissmissProgressDialog();
		if (rCode == 0)
		{
			if (result != null && result.getPaths() != null && result.getPaths().size() > 0)
			{
				mDriveRouteResult = result;
				DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(this,
																					mAMap,
																					drivePath,
																					mDriveRouteResult.getStartPos(),
																					mDriveRouteResult.getTargetPos());
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
			}
			else
			{
				ToastUtils.makeShortText(this, "规划失败！");
			}
		}
		else
		{
			ToastUtils.makeShortText(this, "路线规划失败！");
		}
	}

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1)
	{
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult arg0, int arg1)
	{
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mMapView.onDestroy();
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog()
	{
		if (mProgDialog == null) mProgDialog = new ProgressDialog(this);
		mProgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgDialog.setIndeterminate(false);
		mProgDialog.setCancelable(true);
		mProgDialog.setMessage("正在搜索...");
		// mProgDialog.setMessage("正在搜索:\n" + keyWord);
		mProgDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog()
	{
		if (mProgDialog != null)
		{
			mProgDialog.dismiss();
		}
	}

	@Override
	public void onBackPressed()
	{
		finishActivity();
	}

	protected void finishActivity()
	{
		finish();
		overridePendingTransition(R.anim.pre_enter, R.anim.pre_exit);
	}
}
