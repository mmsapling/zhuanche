package com.baidu.zhuanche.ui.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.DialogGetSeaportAdapter;
import com.baidu.zhuanche.adapter.DialogSignAdapter;
import com.baidu.zhuanche.adapter.LevelAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.CartTypeBean;
import com.baidu.zhuanche.bean.CartTypeBean.LevelBean;
import com.baidu.zhuanche.bean.GetSeaPortBean;
import com.baidu.zhuanche.bean.GetSeaport;
import com.baidu.zhuanche.bean.Location;
import com.baidu.zhuanche.bean.Sex;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.bean.Yuyue;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.timeselector.TimeSelector;
import com.baidu.zhuanche.timeselector.utils.DateUtil;
import com.baidu.zhuanche.ui.user.GetOffUI.OnGetOffLocationListener;
import com.baidu.zhuanche.ui.user.GetOnUI.OnGetOnLocationListener;
import com.baidu.zhuanche.utils.AMapUtil;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.JsonUtils;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui
 * @类名: YuyueUI
 * @创建者: 陈选文
 * @创建时间: 2015-12-26 下午5:50:58
 * @描述: 预约界面
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class CopyOfYuyueUI extends BaseActivity implements OnClickListener, OnGetOnLocationListener, OnGetOffLocationListener, AMapLocationListener, OnGeocodeSearchListener, OnCheckedChangeListener
{
	private Yuyue					mYuyueData;											// 预约所需值，都封装在这个class中

	private LinearLayout			mGroupView;											// 各条目容器
	private ProgressDialog			mPogressDialog	= null;
	private User					mUser;
	private AlertDialog.Builder		mBuilder;												// 对话框
	private AsyncHttpClient			mClient			= AsyncHttpClientUtil.getInstance();
	/** 级别容器 */
	private RelativeLayout			mContainerLevel;
	private EditText				mEtLevel;
	/** 类型容器 */
	private RelativeLayout			mContainerType;
	private EditText				mEtType;
	/** 签证类型 */
	private RelativeLayout			mContainerSignType;
	private TextView				mTvSignType;
	/** 时间 */
	private RelativeLayout			mContainerTime;
	private TextView				mTvTime;
	/** 口岸 */
	private RelativeLayout			mContainerPort;
	private TextView				mTvPort;
	/** 上车地点 */
	private RelativeLayout			mContainerGetOn;
	private EditText				mEtGetOn;
	/** 下车地点 */
	private RelativeLayout			mContainerGetOff;
	private EditText				mEtGetOff;

	/** 估价 */
	private RelativeLayout			mContainerBudget;
	private TextView				mTvBudget;
	private Button					mBtYuyue;
	private EditText				mEtPeopleCount;
	private EditText				mEtXingLiCount;
	private EditText				mEtFee;
	private EditText				mEtDes;
	private EditText				mEtPhone;
	// 声明AMapLocationClient类对象
	public AMapLocationClient		mLocationClient	= null;
	// 声明定位回调监听器
	public AMapLocationListener		mLocationListener1;

	public AMapLocationClientOption	mLocationOption	= null;
	private double					mLatitude		= 0;
	private double					mLongitude		= 0;
	private GeocodeSearch			mGeocoderSearch;

	private RadioGroup				mRadioGroup;
	private RadioButton				mRbSpecialCar;
	private RadioButton				mRbFreeCar;

	@Override
	public void init()
	{
		super.init();
		setUserAligs();
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
		mLocationOption.setOnceLocation(true);
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

	@SuppressLint("CutPasteId")
	@Override
	public void initView()
	{
		setContentView(R.layout.ui_yuyue);
		mGroupView = (LinearLayout) findViewById(R.id.yuyue_groupview);
		View view = View.inflate(this, R.layout.layout_group_children, null);
		mGroupView.addView(view);
		mEtAriNumber = (EditText) view.findViewById(R.id.yuyue_et_airnumber);
		// 级别
		mContainerLevel = (RelativeLayout) view.findViewById(R.id.yuyueContainer_level);
		mEtLevel = (EditText) view.findViewById(R.id.yuyue_et_level);
		// 类型
		// mContainerType = (RelativeLayout)
		// view.findViewById(R.id.yuyueContainer_type);
		// mEtType = (EditText) view.findViewById(R.id.yuyue_et_type);
		// 签证类型
		mContainerSignType = (RelativeLayout) view.findViewById(R.id.yuyueContainer_signtype);
		mTvSignType = (TextView) view.findViewById(R.id.yuyue_tv_signtype);
		// 时间
		mContainerTime = (RelativeLayout) view.findViewById(R.id.yuyueContainer_date);
		mTvTime = (TextView) view.findViewById(R.id.yuyue_tv_time);
		// 口岸
		mContainerPort = (RelativeLayout) view.findViewById(R.id.yuyueContainer_port);
		mTvPort = (TextView) view.findViewById(R.id.yuyue_tv_port);
		// 上车地点
		mContainerGetOn = (RelativeLayout) view.findViewById(R.id.yuyueContainer_geton);
		mEtGetOn = (EditText) view.findViewById(R.id.yuyue_et_geton);
		// 下车地点
		mContainerGetOff = (RelativeLayout) view.findViewById(R.id.yuyueContainer_getoff);
		mEtGetOff = (EditText) view.findViewById(R.id.yuyue_et_getoff);
		// 估价
		mContainerBudget = (RelativeLayout) view.findViewById(R.id.yuyue_container_budget);
		mTvBudget = (TextView) view.findViewById(R.id.yuyue_tv_price);
		mBtYuyue = (Button) findViewById(R.id.home_bt_yuyue);
		mEtPeopleCount = (EditText) view.findViewById(R.id.childer_et_peopleCount);
		mEtXingLiCount = (EditText) view.findViewById(R.id.childer_et_luggageCount);
		mEtDes = (EditText) view.findViewById(R.id.yuyue_tv_des);
		mEtFee = (EditText) view.findViewById(R.id.yuyue_et_fee);

		mRadioGroup = (RadioGroup) view.findViewById(R.id.children_radiogroup);
		mRbFreeCar = (RadioButton) view.findViewById(R.id.childer_rb_freecar);
		mRbSpecialCar = (RadioButton) view.findViewById(R.id.childer_rb_specialcar);
		
		mEtPhone = (EditText) view.findViewById(R.id.yuyue_et_phone);
		init1();
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
		mPogressDialog.setMessage("正在获取你当前的地址...");
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

	@Override
	public void initData()
	{
		super.initData();
		mUser = BaseApplication.getUser();
		mIvRightHeader.setVisibility(0);
		mPogressDialog = new ProgressDialog(this);
		mIvRightHeader.setImageResource(R.drawable.page_02);
		mTvTitle.setText("预约");
		mEtPhone.setText(mUser.mobile);
		mBtYuyue.setEnabled(false);
		mGeocoderSearch = new GeocodeSearch(this);
		mYuyueData = new Yuyue();

		mBuilder = new Builder(this);
	}

	@Override
	public void initListener()
	{
		super.initListener();
		GetOnUI.setOnGetOnLocationListener(this);// 上车地点接口回调
		GetOffUI.setOnGetOffLocationListener(this);
		mIvLeftHeader.setOnClickListener(this);
		mIvRightHeader.setOnClickListener(this);
		mContainerLevel.setOnClickListener(this);
		// mContainerType.setOnClickListener(this);
		mContainerSignType.setOnClickListener(this);
		mContainerTime.setOnClickListener(this);
		mContainerPort.setOnClickListener(this);
		mContainerGetOn.setOnClickListener(this);
		mContainerGetOff.setOnClickListener(this);
		mBtYuyue.setOnClickListener(this);
		mGeocoderSearch.setOnGeocodeSearchListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
		mEtPeopleCount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (!TextUtils.isEmpty(s))
				{
					int p1 = Integer.parseInt(mYuyueData.maxPeopleCount);
					int p2 = Integer.parseInt(mEtPeopleCount.getText().toString().trim());
					if (p2 > p1)
					{
						ToastUtils.makeShortText("乘车人数不能大于最大乘车人数！");
					}
				}
				enableYuyue();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				if (!TextUtils.isEmpty(s))
				{
					int p1 = Integer.parseInt(mYuyueData.maxPeopleCount);
					int p2 = Integer.parseInt(mEtPeopleCount.getText().toString().trim());
					if (p2 > p1)
					{
						ToastUtils.makeShortText("乘车人数不能大于最大乘车人数！");
					}
				}
			}
		});
		mEtPeopleCount.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
					panDuan(1);
				}
				if (!hasFocus && !TextUtils.isEmpty(mEtPeopleCount.getText().toString().trim()))
				{
					panDuan(2);
				}
			}
		});
		mEtLevel.addTextChangedListener(new BudgetListener(1));
		// mEtType.addTextChangedListener(new BudgetListener(2));
		mEtGetOff.addTextChangedListener(new BudgetListener(3));
		mEtGetOn.addTextChangedListener(new BudgetListener(4));
		mTvPort.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (TextUtils.isEmpty(s))
				{
					mBtYuyue.setEnabled(false);
				}
				enableYuyue();

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub

			}
		});
		mEtGetOn.setOnTouchListener(new OnTouchListener() {

			long	time;

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						time = System.currentTimeMillis();
						break;
					case MotionEvent.ACTION_UP:
						if (System.currentTimeMillis() - time < 200)
						{
							Bundle bundle = new Bundle();
							bundle.putString("geton", mYuyueData.getOnLocation.address);
							startActivity(GetOnUI.class, bundle);
						}
						closeInputMethod();
						break;
					default:
						break;
				}
				return true;
			}
		});

		mEtGetOff.setOnTouchListener(new OnTouchListener() {

			long	time;

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
						time = System.currentTimeMillis();
						break;
					case MotionEvent.ACTION_UP:
						if (System.currentTimeMillis() - time < 200)
						{
							startActivity(GetOffUI.class);
						}
						closeInputMethod();
						break;
					default:
						break;
				}
				return true;
			}
		});
	}

	public void enableYuyue()
	{
		String peopleCount = mEtPeopleCount.getText().toString().trim();
		String port = mTvPort.getText().toString().trim();
		String getoff = mEtGetOff.getText().toString().trim();
		String geton = mEtGetOn.getText().toString().trim();
		String price = mTvBudget.getText().toString().trim();
		boolean flag = !TextUtils.isEmpty(peopleCount) && !TextUtils.isEmpty(port)
						&& !TextUtils.isEmpty(getoff) && !TextUtils.isEmpty(geton) && !TextUtils.isEmpty(price);

		if (flag)
		{
			mBtYuyue.setEnabled(true);
		}
		else
		{
			mBtYuyue.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v == mContainerLevel)
		{
			doClickLevel();
		}
		else if (v == mIvLeftHeader)
		{
			finishActivity(UserHomeUI.class);
		}
		else if (v == mIvRightHeader)
		{
			startActivity(UserCenterUI.class);
		}
		else if (v == mContainerType)
		{
			doClickType();
		}
		else if (v == mContainerSignType)
		{
			// doClickSignType();
			doClickSign();
			// doClickCanCancelSign();
		}
		else if (v == mContainerTime)
		{
			doClickTime();
		}
		else if (v == mContainerPort)
		{
			doClickPort();
		}
		else if (v == mContainerGetOn)
		{
			startActivity(GetOnUI.class);
		}
		else if (v == mContainerGetOff)
		{
			startActivity(GetOffUI.class);
		}
		else if (v == mBtYuyue)
		{
			yuyueOrder();
		}
		else if (v == mContainerBudget)
		{
			getBudget();
		}
		else if (v == mEtPeopleCount)
		{
			// panDuan();
		}
	}

	private void panDuan(int index)
	{
		if (1 == index)
		{
			if (TextUtils.isEmpty(mYuyueData.cartype))
			{
				mEtPeopleCount.setText("");
				mEtPeopleCount.setEnabled(false);
				ToastUtils.makeShortText("请先选择车级别！");
				return;
			}
		}
		else if (2 == index)
		{
			int p1 = Integer.parseInt(mYuyueData.maxPeopleCount);
			int p2 = Integer.parseInt(mEtPeopleCount.getText().toString().trim());
			if (p2 > p1)
			{
				mEtPeopleCount.setText("");
				ToastUtils.makeShortText("乘车人数不能大于最大乘车人数！");
			}
		}

	}

	private void getBudget()
	{
		if (TextUtils.isEmpty(mYuyueData.cartype))
		{
			ToastUtils.makeShortText("请选择车级别！");
			return;
		}
		if (TextUtils.isEmpty(mYuyueData.carpool))
		{
			ToastUtils.makeShortText("请选择车类型！");
			return;
		}
		Location offLocation = mYuyueData.getOffLocation;
		Location onLocation = mYuyueData.getOnLocation;
		if (TextUtils.isEmpty(offLocation.address))
		{
			ToastUtils.makeShortText("请选择下车地点！");
			return;
		}
		if (TextUtils.isEmpty(onLocation.address))
		{
			ToastUtils.makeShortText("请选择上车地点！");
			return;
		}
		String url = URLS.BASESERVER + URLS.User.budget;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, mUser.access_token);
		params.put("from_province", mYuyueData.getOnLocation.province);
		params.put("from_city", mYuyueData.getOnLocation.city);
		params.put("from_district", mYuyueData.getOnLocation.district);
		params.put("to_province", mYuyueData.getOffLocation.province);
		params.put("to_city", mYuyueData.getOffLocation.city);
		params.put("to_district", mYuyueData.getOffLocation.district);
		params.put("cartype", mYuyueData.cartype);
		params.put("carpool", mYuyueData.carpool);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				processBudgetJson(json);
			}
		});
	}

	protected void processBudgetJson(String json)
	{
		try
		{
			JSONObject jsonObject = JsonUtils.getContent(json);
			String budget = jsonObject.getString("budget");
			mYuyueData.budget = budget;
			mTvBudget.setText(budget + "元");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	private void yuyueOrder()
	{
		mYuyueData.peopleCount = mEtPeopleCount.getText().toString().trim();
		mYuyueData.xingliCount = mEtXingLiCount.getText().toString().trim();
		mYuyueData.fee = mEtFee.getText().toString().trim();
		mYuyueData.des = mEtDes.getText().toString().trim();
		mYuyueData.phone = mEtPhone.getText().toString().trim();
		if (TextUtils.isEmpty(mYuyueData.cartype))
		{
			ToastUtils.makeShortText("请选择级别");
			return;
		}
		else if (TextUtils.isEmpty(mYuyueData.carpool))
		{
			ToastUtils.makeShortText("请选择类型");
			return;
		}
		else if (TextUtils.isEmpty(mYuyueData.peopleCount))
		{
			ToastUtils.makeShortText("请输入乘车人数");
			return;
		}
		else if (TextUtils.isEmpty(mYuyueData.xingliCount))
		{
			ToastUtils.makeShortText("请输入行李数");
			return;
		}
		// else if (TextUtils.isEmpty(mYuyueData.signtype))
		// {
		// ToastUtils.makeShortText("请选择签证类型");
		// return;
		// }
		else if (TextUtils.isEmpty(mYuyueData.time))
		{
			ToastUtils.makeShortText("请选择时间");
			return;
		}
		else if (TextUtils.isEmpty(mYuyueData.seaport))
		{
			ToastUtils.makeShortText("请选择口岸");
			return;
		}
		else if (TextUtils.isEmpty(mYuyueData.getOnLocation.address))
		{
			ToastUtils.makeShortText("请选择上车地点");
			return;
		}
		else if (TextUtils.isEmpty(mYuyueData.getOffLocation.address))
		{
			ToastUtils.makeShortText("请选择下车地点");
			return;
		}else if(TextUtils.isEmpty(mYuyueData.phone)){
			ToastUtils.makeShortText("请輸入乘车人联系电话！");
			return;
		}
		List<String> signs = mYuyueData.signs;
		String sign1 = "";
		String[] items = new String[] { "自由行", "团签", "回乡证", "护照" };
		for (int i = 0; i < 4; i++)
		{
			if (items[i].equals(signs.get(i)))
			{
				sign1 += items[i] + ",";
			}
		}
		// for(String s : signs){
		// if(s.equals("1")){
		// sign1 += items[0] + ",";
		// }else if(s.equals("2")){
		// sign1 += items[1] + ",";
		// } else if( s.equals("3")){
		// sign1 += items[2] + ",";
		// }else if(s.equals("4")){
		// sign1 += items[3] + ",";
		// }
		// }
		PrintUtils.print("yyd = " + mYuyueData.signs);
		if (!TextUtils.isEmpty(sign1))
		{
			sign1 = sign1.substring(0, sign1.length() - 1);
		}
		PrintUtils.print("a = " + sign1);
		if (TextUtils.isEmpty(sign1))
		{
			ToastUtils.makeShortText("请选择签证类型");
			return;
		}
		PrintUtils.print("b = " + sign1);
		int p1 = Integer.parseInt(mYuyueData.maxPeopleCount);
		int p2 = Integer.parseInt(mEtPeopleCount.getText().toString().trim());
		if (p2 > p1)
		{
			ToastUtils.makeShortText("乘车人数不能大于最大乘车人数！");
			return;
		}
		String url = URLS.BASESERVER + URLS.User.makeOrder;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, mUser.access_token);
		params.put(URLS.CARTYPE, mYuyueData.cartype);
		params.put(URLS.CARPOOL, mYuyueData.carpool);
		params.put("from", mYuyueData.getOnLocation.address);
		params.put("from_province", mYuyueData.getOnLocation.province);
		params.put("from_city", mYuyueData.getOnLocation.city);
		params.put("from_district", mYuyueData.getOnLocation.district);
		params.put("to", mYuyueData.getOffLocation.address);
		params.put("to_province", mYuyueData.getOffLocation.province);
		params.put("to_city", mYuyueData.getOffLocation.city);
		params.put("to_district", mYuyueData.getOffLocation.district);
		params.put("count", mYuyueData.peopleCount);
		params.put("luggage", mYuyueData.xingliCount);
		params.put("is_hk", sign1);
		params.put("time", mYuyueData.time);
		params.put("seaport", mYuyueData.seaport);
		params.put("budget", mYuyueData.budget);
		params.put("fee", mYuyueData.fee);
		params.put("remark", mYuyueData.des);
		params.put("point", mYuyueData.getOnLocation.latLng.longitude + "," + mYuyueData.getOnLocation.latLng.latitude);
		params.put("air_number", mEtAriNumber.getText().toString());
		params.put("phone", mYuyueData.phone);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				ToastUtils.makeShortText("预约成功！");
				// mYuyueData = new Yuyue();
				// clearData();
				startActivity(UserCenterUI.class);
			}
		});
	}

	protected void clearData()
	{

	}

	/** 级别 */
	private void doClickLevel()
	{
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance(this);
		String url = URLS.BASESERVER + URLS.User.enum_;
		RequestParams params = new RequestParams();
		PrintUtils.print("level=" + mUser.access_token);
		params.put(URLS.ACCESS_TOKEN, BaseApplication.getUser().access_token);
		params.put("type", URLS.CARTYPE);
		ToastUtils.showProgress(this);
		client.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				Gson gson = new Gson();
				CartTypeBean cartTypeBean = gson.fromJson(json, CartTypeBean.class);
				final List<LevelBean> datas = cartTypeBean.content;
				LevelAdapter adapter = new LevelAdapter(datas);
				mBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						LevelBean bean = datas.get(which);
						mYuyueData.cartype = bean.eid;
						mYuyueData.maxPeopleCount = bean.value;
						mEtLevel.setText(bean.name);
						mEtPeopleCount.setEnabled(true);

					}
				});
				mBuilder.show();
			}
		});

	}

	/** 类型 */
	private void doClickType()
	{
		List<Sex> dataSource = new ArrayList<Sex>();
		dataSource.add(new Sex("专车", "0"));
		dataSource.add(new Sex("顺风车", "1"));
		final DialogSignAdapter adapter = new DialogSignAdapter(this, dataSource);
		mBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Sex sex = (Sex) adapter.getItem(which);
				mEtType.setText(sex.name);
				mYuyueData.carpool = sex.value;
			}
		});
		mBuilder.show();
	}

	/** 签证类型 */
	private void doClickSignType()
	{
		List<Sex> dataSource = new ArrayList<Sex>();
		dataSource.add(new Sex("自由行", "0"));
		dataSource.add(new Sex("团签", "1"));
		dataSource.add(new Sex("回乡证", "2"));
		dataSource.add(new Sex("护照", "3"));
		final DialogSignAdapter adapter = new DialogSignAdapter(this, dataSource);
		mBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Sex sex = (Sex) adapter.getItem(which);
				mTvSignType.setText(sex.name);
				mYuyueData.signtype = sex.value;
			}
		});
		mBuilder.show();
	}

	private String		sign	= "";

	private EditText	mEtAriNumber;

	private void doClickSign()
	{

		final String[] items = new String[] { "自由行", "团签", "回乡证", "护照" };
		boolean[] checkeItems = new boolean[] { false, false, false, false };
		PrintUtils.print("str1 = " + mYuyueData.signs.toString());
		for (int i = 0; i < mYuyueData.signs.size(); i++)
		{
			if (mYuyueData.signs.get(i).equals(items[i]))
			{
				checkeItems[i] = true;
			}
			else
			{
				checkeItems[i] = false;
			}
		}
		AlertDialog.Builder builder = new Builder(this);
		builder.setMultiChoiceItems(items, checkeItems, new OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked)
			{
				// if (isChecked)
				// {
				// sign += items[which] + " ";
				// mTvSignType.setText(sign);
				// mYuyueData.signs.add(items[which]);
				// }
				// else
				// {
				// sign = sign.replace(items[which], "");
				// mYuyueData.signs.remove(items[which]);
				// mTvSignType.setText(sign);
				// }
				for (int i = 0; i < 4; i++)
				{
					if (i == which && isChecked)
					{
						if (!sign.contains(items[which]))
						{
							sign += items[which] + " ";
							mTvSignType.setText(sign);
							mYuyueData.signs.set(which, items[which]);
						}
					}
					else if (i == which && !isChecked)
					{
						if (sign.contains(items[which]))
						{
							sign = sign.replace(items[which], "");
							mYuyueData.signs.set(which, which + "");
							mTvSignType.setText(sign);
						}
					}
				}
				PrintUtils.print("str2 = " + mYuyueData.signs.toString());
			}

		});
		// builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which)
		// {
		//
		// dialog.dismiss();
		// }
		// });
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				PrintUtils.print("str3 = " + mYuyueData.signs.toString());
				dialog.dismiss();
			}

		});
		builder.show();

	}

	/** 时间 */
	private void doClickTime()
	{
		String initStartDateTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm"); // 初始化开始时间
		TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
			@Override
			public void handle(String time)
			{
				Date date = DateUtil.parse(time, "yyyy-MM-dd HH:mm");
				mTvTime.setText(time);
				long time1 = (date.getTime() / 1000);
				mYuyueData.time = "" + time1;
			}
		}, initStartDateTime, "2100-01-01 00:00");
		timeSelector.show();
	}

	/** 口岸 */
	private void doClickPort()
	{
		if (TextUtils.isEmpty(mYuyueData.time))
		{
			ToastUtils.makeShortText("请选择时间");
			return;
		}
		ToastUtils.showProgress(this);
		String url = URLS.BASESERVER + URLS.User.getSeaport;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, BaseApplication.getUser().access_token);
		params.put("time", mYuyueData.time);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				processJson(json);
			}
		});
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		mUser = BaseApplication.getUser();
	}

	protected void processJson(String json)
	{
		List<GetSeaport> dataSource = new ArrayList<GetSeaport>();
		GetSeaPortBean seaPortBean = mGson.fromJson(json, GetSeaPortBean.class);
		dataSource = seaPortBean.content;
		GetSeaport sear = new GetSeaport();
		sear.eid = "0";
		sear.name = "不限定口岸";
		dataSource.add(sear);
		PrintUtils.print(dataSource.get(0).toString());
		if (isListEmpty(dataSource)) { return; }
		final DialogGetSeaportAdapter adapter = new DialogGetSeaportAdapter(this, dataSource);
		mBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				GetSeaport sex = (GetSeaport) adapter.getItem(which);
				mTvPort.setText(sex.name);
				mYuyueData.seaport = sex.eid;
			}
		});
		mBuilder.show();
	}

	@Override
	public void onGetOnLocation(Location location)
	{
		mYuyueData.getOnLocation = location;
		mEtGetOn.setText(location.address);
	}

	@Override
	public void onGetOffLocation(Location location)
	{
		mYuyueData.getOffLocation = location;
		mEtGetOff.setText(location.address);
	}

	private class BudgetListener implements TextWatcher
	{
		private int	index;

		public BudgetListener(int i) {
			index = i;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			if (TextUtils.isEmpty(s)) { return; }
			if (TextUtils.isEmpty(mYuyueData.cartype)) { return; }
			if (TextUtils.isEmpty(mYuyueData.carpool)) { return; }
			Location offLocation = mYuyueData.getOffLocation;
			Location onLocation = mYuyueData.getOnLocation;
			if (TextUtils.isEmpty(offLocation.address)) { return; }
			if (TextUtils.isEmpty(onLocation.address)) { return; }
			getBudget();
			enableYuyue();
		}

		@Override
		public void afterTextChanged(Editable s)
		{

		}

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
				showDialog();
				RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(mLatitude, mLongitude), 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
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
				Location location = new Location();
				location.address = address.getFormatAddress();
				location.province = address.getProvince();
				location.city = address.getCity();
				location.district = address.getDistrict();
				location.latLng = AMapUtil.convertToLatLng(address.getLatLonPoint());
				mYuyueData.getOnLocation = location;
				mEtGetOn.setText(mYuyueData.getOnLocation.address);
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

	@Override
	public void onBackPressed()
	{
		finishActivity(UserHomeUI.class);
	}

	@Override
	protected void onDestroy()
	{
		mGeocoderSearch = null;
		mLocationClient.onDestroy();
		mLocationClient = null;
		super.onDestroy();
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
				RegeocodeAddress address = result.getRegeocodeAddress();
				Location location = new Location();
				location.address = address.getFormatAddress();
				location.province = address.getProvince();
				location.city = address.getCity();
				location.district = address.getDistrict();
				location.latLng = new LatLng(query.getPoint().getLatitude(), query.getPoint().getLongitude());
				mYuyueData.getOnLocation = location;
				mEtGetOn.setText(mYuyueData.getOnLocation.address);
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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		if (checkedId == R.id.childer_rb_freecar)
		{
			mYuyueData.carpool = "1";
		}
		else if (checkedId == R.id.childer_rb_specialcar)
		{
			mYuyueData.carpool = "0";
		}
	}

}
