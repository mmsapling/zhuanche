package com.baidu.zhuanche.ui.driver;

import java.util.Date;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.DriverHomeBean.DriverHomeOrder;
import com.baidu.zhuanche.bean.OrderDetailBean;
import com.baidu.zhuanche.bean.DriverCenterOrderListBean.OrderBean;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.timeselector.utils.TextUtil;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.AtoolsUtil;
import com.baidu.zhuanche.utils.DateFormatUtil;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.view.CircleImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: DriverCenterOrderDetailUI
 * @创建者: 陈选文
 * @创建时间: 2016-1-14 下午5:41:19
 * @描述: 司机中心订单详情
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DriverCenterOrderDetailUI extends BaseActivity implements OnClickListener
{
	private OrderBean		mOrderBean;
	private DriverHomeOrder	mHomeOrder;
	private Button			mBtOrder;		// 接单按钮
	private CircleImageView	mIvPhoto;		// 头像
	private TextView		mTvName;		// 名称
	private TextView		mTvTime;		// 时间
	private TextView		mTvPrice;		// 价钱
	private TextView		mTvGetOn;		// 上车地点
	private TextView		mTvGetOff;		// 下车地点
	private TextView		mTvLevel;		// 等级
	private TextView		mTvCarPool;	// 类型
	private TextView		mTvSeaport;	// 口岸
	private TextView		mTvPeopleCount; // 人数
	private TextView		mTvXingLiCount; // 行李数
	private TextView		mTvSignType;	// 签证类型
	private TextView		mTvYuyuePrice;	// 预约价格
	private TextView		mTvFee;		// 小费
	private TextView		mTvHangBan;	// 航班号
	private TextView		mTvMobile;
	private TextView		mTvDes;
	private LinearLayout	mContainerCall;
	private Dialog			mDialog;

	@Override
	public void init()
	{
		super.init();
		Bundle bundle = getIntent().getBundleExtra(VALUE_PASS);
		mOrderBean = (OrderBean) bundle.getSerializable(MyConstains.ITEMBEAN);
	}

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_center_acceptorder);
		mBtOrder = (Button) findViewById(R.id.acceptorder_bt_acceptorder);
		mIvPhoto = (CircleImageView) findViewById(R.id.acceptorder_iv_photo);
		mTvName = (TextView) findViewById(R.id.acceptorder_tv_name);
		mTvTime = (TextView) findViewById(R.id.acceptorder_tv_time);
		mTvPrice = (TextView) findViewById(R.id.acceptorder_tv_price);
		mTvGetOn = (TextView) findViewById(R.id.acceptorder_tv_geton);
		mTvGetOff = (TextView) findViewById(R.id.acceptorder_tv_getoff);
		mTvLevel = (TextView) findViewById(R.id.acceptorder_tv_level);
		mTvCarPool = (TextView) findViewById(R.id.acceptorder_tv_carpool);
		mTvSeaport = (TextView) findViewById(R.id.acceptorder_tv_seaport);
		mTvPeopleCount = (TextView) findViewById(R.id.acceptorder_tv_peoplecount);
		mTvXingLiCount = (TextView) findViewById(R.id.acceptorder_tv_xingli);
		mTvSignType = (TextView) findViewById(R.id.acceptorder_tv_signtype);
		mTvYuyuePrice = (TextView) findViewById(R.id.acceptorder_tv_yuyueprice);
		mTvFee = (TextView) findViewById(R.id.acceptorder_tv_fee);
		mTvHangBan = (TextView) findViewById(R.id.acceptorder_tv_hangban);
		mTvMobile = (TextView) findViewById(R.id.acceptorder_tv_mobile);
		mTvDes = (TextView) findViewById(R.id.acceptorder_tv_des);
		mContainerCall = (LinearLayout) findViewById(R.id.container_call);
	}
	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("接單詳細");
		ToastUtils.showProgress(this);
		String url = URLS.BASESERVER + URLS.Driver.orderDetail;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		params.put("sn", mOrderBean.sn);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				processJson(json);
			}
		});

	}

	public void setData()
	{
		mTvTime.setText(DateFormatUtil.getDateTimeStr(new Date(Long.parseLong(mHomeOrder.time) * 1000)));
		float price = Float.parseFloat(mHomeOrder.budget) + Float.parseFloat(mHomeOrder.fee);
		mTvPrice.setText("￥" + price);
		mTvGetOn.setText(mHomeOrder.from);
		mTvGetOff.setText(mHomeOrder.to);
		mTvLevel.setText(mHomeOrder.cartype);
		mTvCarPool.setText(AtoolsUtil.getCarPool(mHomeOrder.carpool));
		mTvSeaport.setText(mHomeOrder.seaport + "口岸");
		mTvPeopleCount.setText(mHomeOrder.count + "人");
		mTvXingLiCount.setText(TextUtils.isEmpty(mHomeOrder.luggage)? "無" : mHomeOrder.luggage +"个");// 行李數 //TODO
		mTvSignType.setText(mHomeOrder.is_hk);// 簽證類型
		mTvFee.setText("￥" + mHomeOrder.fee);
		mTvYuyuePrice.setText("￥" + mHomeOrder.budget);
		mTvHangBan.setText(TextUtils.isEmpty(mHomeOrder.air_number) ? "B100G" : mHomeOrder.air_number);// 航班
		mImageUtils.display(mIvPhoto, URLS.BASE + mHomeOrder.icon);
		mTvName.setText(mHomeOrder.username);
		mTvMobile.setText(mHomeOrder.mobile);
		mTvDes.setText(mHomeOrder.remark);
		mBtOrder.setText(setBtText(mHomeOrder.status));
		if("5".equals(mHomeOrder.status)){
			mBtOrder.setEnabled(true);
		}else {
			mBtOrder.setEnabled(false);
		}
	}

	protected void processJson(String json)
	{
		OrderDetailBean orderDetailBean = mGson.fromJson(json, OrderDetailBean.class);
		mHomeOrder = orderDetailBean.content;
		setData();

	}

	@Override
	public void initListener()
	{
		super.initListener();
		mIvLeftHeader.setOnClickListener(this);
		mBtOrder.setOnClickListener(this);
		mContainerCall.setOnClickListener(this);
	}

	public String setBtText(String status)
	{
		String text = "接单";
		if ("0".equals(status))
		{
			text = "预约中";
		}
		else if ("1".equals(status))
		{
			text = "已预约";
		}
		else if ("2".equals(status))
		{
			text = "已付款";
		}
		else if ("3".equals(status))
		{
			text = "已完成";
		}
		else if ("4".equals(status))
		{
			text = "已取消";
		}
		else if ("5".equals(status))
		{
			text = "確認收款";
		}
		return text;
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
		else if (v == mContainerCall)
		{
			showCall();
		}else if(v == mBtOrder){
			order();
		}
	}

	private void order()
	{
		if("確認收款".equals(mBtOrder.getText().toString())){
			String url = URLS.BASESERVER + URLS.Driver.finished;
			RequestParams params = new RequestParams();
			params.put(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
			params.put("sn", mOrderBean.sn);
			mClient.post(url, params, new MyAsyncResponseHandler() {
				
				@Override
				public void success(String json)
				{
					ToastUtils.makeShortText("確認收款成功！");
					mOrderBean.status = "2";
					if(mListener != null){
						mListener.onChange(mOrderBean);
					}
					mBtOrder.setEnabled(false);
				}
			});
		}
	}
	private static OnChangeStatusListener mListener;
	public static void setOnChangeStatusListener(OnChangeStatusListener listener){
		mListener = listener;
	}
	public interface OnChangeStatusListener{
		void onChange(OrderBean orderBean);
	}
	private void showCall()
	{
		if (mDialog == null)
		{
			mDialog = new Dialog(this, R.style.CustomBottomDialog);
			mDialog.setCanceledOnTouchOutside(false);
			// mDialog.setCancelable(false);
			// 获取对话框，并设置窗口参数
			Window window = mDialog.getWindow();
			LayoutParams params = new LayoutParams();
			// 不能写成这样,否则Dialog显示不出来
			// LayoutParams params = new
			// LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			// 对话框窗口的宽和高
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.WRAP_CONTENT;
			// 对话框显示的位置
			params.x = 120;
			params.y = UIUtils.dip2Px(1000);
			window.setAttributes(params);
			// 设置对话框布局
			mDialog.setContentView(R.layout.layout_call);
			Button btCancle = (Button) mDialog.findViewById(R.id.call_bt_cancle);
			TextView tvName = (TextView) mDialog.findViewById(R.id.call_bt_text1);
			tvName.setText(mHomeOrder.mobile);
			tvName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mHomeOrder.mobile));
					startActivity(intent);
				}
			});
			btCancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					dismiss();
				}
			});
			mDialog.show();
		}
	}

	/**
	 * 隐藏对话框
	 */
	public void dismiss()
	{
		// 隐藏对话框之前先判断对话框是否存在，以及是否正在显示
		if (mDialog != null && mDialog.isShowing())
		{
			mDialog.dismiss();
			mDialog = null;
		}
	}
}
