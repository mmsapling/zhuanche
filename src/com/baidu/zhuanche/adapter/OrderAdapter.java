package com.baidu.zhuanche.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.FeeBean.Fee;
import com.baidu.zhuanche.bean.OrderListBean.DriverInfo;
import com.baidu.zhuanche.bean.OrderListBean.OrderBean;
import com.baidu.zhuanche.bean.FeeBean;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.ui.user.AssessDetailUI;
import com.baidu.zhuanche.ui.user.AssessDetailUI.OnAssessListener;
import com.baidu.zhuanche.ui.user.LookAssessUI;
import com.baidu.zhuanche.ui.user.MyRouteUI;
import com.baidu.zhuanche.ui.user.YuYueDetailUI;
import com.baidu.zhuanche.ui.user.YuYueDetailUI.OnAddFeeListener;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.JsonUtils;
import com.baidu.zhuanche.utils.OrderUtil;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class OrderAdapter extends MyBaseApdater<OrderBean> implements OnAddFeeListener, OnAssessListener
{
	private User	mUser;

	public OrderAdapter(Context context, List<OrderBean> dataSource) {
		super(context, dataSource);
		mUser = BaseApplication.getUser();
		YuYueDetailUI.setOnAddFeeListener(this);
		AssessDetailUI.setOnAssessListener(this);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		OrderViewHolder holder = null;
		if (convertView == null)
		{
			holder = new OrderViewHolder();
			convertView = View.inflate(mContext, R.layout.item_orderlist, null);
			convertView.setTag(holder);
			holder.tvTime = (TextView) convertView.findViewById(R.id.item_yuyue_date);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.item_yuyue_status);
			holder.tvLevel = (TextView) convertView.findViewById(R.id.item_yuyue_level);
			holder.tvType = (TextView) convertView.findViewById(R.id.item_yuyue_type);
			holder.tvPeople = (TextView) convertView.findViewById(R.id.item_yuyue_people);
			holder.tvPort = (TextView) convertView.findViewById(R.id.item_yuyue_port);
			holder.tvScPosition = (TextView) convertView.findViewById(R.id.item_yuyue_to);
			holder.tvXcPosition = (TextView) convertView.findViewById(R.id.item_yuyue_from);
			holder.tvBudget = (TextView) convertView.findViewById(R.id.item_yuyue_budget);
			holder.tvFee = (TextView) convertView.findViewById(R.id.item_yuyue_fee);
			holder.ivArrow = (ImageView) convertView.findViewById(R.id.item_yuyue_iv_arrow);
			holder.btLook = (Button) convertView.findViewById(R.id.item_yuyue_look);
			holder.tvSign = (TextView) convertView.findViewById(R.id.item_yuyue_qianzheng);
			holder.tvXingli = (TextView) convertView.findViewById(R.id.item_yuyue_wu);
			holder.tvHangban = (TextView) convertView.findViewById(R.id.item_yuyue_hangbanhao);
			// 加小费容器
			holder.btAddFee = (Button) convertView.findViewById(R.id.item_yuyue_addFee);
			// 去付款按钮
			holder.btPay = (Button) convertView.findViewById(R.id.item_yuyue_fukuang);
			// 去评价按钮
			holder.btGoAssess = (Button) convertView.findViewById(R.id.item_yuyue_bt_assess);
			// 查看评价
			holder.hahaha = (RelativeLayout) convertView.findViewById(R.id.hahaha);
			holder.btLookAssess = (Button) convertView.findViewById(R.id.item_yuyue_bt_lookassess);
			//已取消按鈕
			holder.btCancel = (Button) convertView.findViewById(R.id.item_yuyue_bt_cancel);
			// 显示或者隐藏的容器
			holder.container_orderDetail = (LinearLayout) convertView.findViewById(R.id.item_yuyue_ll_order_detail);
			holder.container_daijiedan = (RelativeLayout) convertView.findViewById(R.id.item_yuyue_status_daijiedan);
			holder.container_yiyueyue = (LinearLayout) convertView.findViewById(R.id.item_yuyue_container_yiyuyue);
			holder.container_goAssess = (RelativeLayout) convertView.findViewById(R.id.item_container_assess);
			holder.container_lookAssess = (RelativeLayout) convertView.findViewById(R.id.item_container_lookassess);
			holder.container_cancel = (RelativeLayout) convertView.findViewById(R.id.item_container_cancel);
			
			holder.tvDriverName = (TextView) convertView.findViewById(R.id.item_yuyue_tv_drivername);
			holder.tvCarId = (TextView) convertView.findViewById(R.id.item_yuyue_tv_carid);
			
		}
		else
		{
			holder = (OrderViewHolder) convertView.getTag();
		}
		final OrderBean bean = (OrderBean) getItem(position);
		
		holder.tvXingli.setText(bean.luggage);
		holder.tvStatus.setText(OrderUtil.getStatusText(bean.status));
		holder.tvLevel.setText(bean.cartype);
		holder.tvHangban.setText(TextUtils.isEmpty(bean.air_number)? "无" :bean.air_number);
		holder.tvType.setText(OrderUtil.getCarPoolText(bean.carpool));
		holder.tvPeople.setText(bean.count + "人");
		holder.tvPort.setText(bean.seaport);
		holder.tvScPosition.setText(bean.from);
		holder.tvXcPosition.setText(bean.to);
		holder.tvBudget.setText(bean.budget + "元");
		holder.tvFee.setText(bean.fee + "元");
		holder.tvTime.setText(OrderUtil.getDateText(bean.time));
//		String sign = "";
//		if(bean.is_hk != null && bean.is_hk.size() > 0){
//			for(String s : bean.is_hk){
//				sign += s +",";
//			}
//			sign = sign.substring(0,sign.length() - 1);
//		}
		holder.tvSign.setText(TextUtils.isEmpty(bean.is_hk) ? "无" : bean.is_hk);
		DriverInfo driverInfo = bean.d_del;
		
		if(!TextUtils.isEmpty(driverInfo.name)){
			holder.tvDriverName.setText(driverInfo.name);
		}
		if(!TextUtils.isEmpty(driverInfo.carid)){
			holder.tvCarId.setText(driverInfo.carid);
		}
		/** 箭头点击事件 */
		holder.ivArrow.setOnClickListener(new MyOnClickLsterner(holder));
		// 预约中状态 ，显示加小费容器
		if ("0".equals(bean.status))
		{
			holder.container_daijiedan.setVisibility(0);
			holder.container_yiyueyue.setVisibility(8);
			holder.container_goAssess.setVisibility(8);
			holder.container_lookAssess.setVisibility(8);
			holder.container_cancel.setVisibility(8);
		}
		else if ("1".equals(bean.status))
		{
			holder.container_daijiedan.setVisibility(8);
			holder.container_yiyueyue.setVisibility(0);
			holder.container_goAssess.setVisibility(8);
			holder.container_lookAssess.setVisibility(8);
			holder.container_cancel.setVisibility(8);
		}
		else if ("2".equals(bean.status))
		{
			holder.container_daijiedan.setVisibility(8);
			holder.container_yiyueyue.setVisibility(8);
			holder.container_goAssess.setVisibility(0);
			holder.container_lookAssess.setVisibility(8);
			holder.container_cancel.setVisibility(8);
		}
		else if ("3".equals(bean.status)) // 查看评价
		{
			holder.container_daijiedan.setVisibility(8);
			holder.container_yiyueyue.setVisibility(8);
			holder.container_goAssess.setVisibility(8);
			holder.container_lookAssess.setVisibility(0);
			holder.container_cancel.setVisibility(8);
		}
		else if("4".equals(bean.status)){// 4 已取消
			holder.container_daijiedan.setVisibility(8);
			holder.container_yiyueyue.setVisibility(8);
			holder.container_goAssess.setVisibility(8);
			holder.container_lookAssess.setVisibility(8);
			holder.container_cancel.setVisibility(8);
			holder.container_cancel.setVisibility(0);
		}
		else if("5".equals(bean.status)){
			holder.container_daijiedan.setVisibility(8);
			holder.container_yiyueyue.setVisibility(0);
			holder.container_goAssess.setVisibility(8);
			holder.container_lookAssess.setVisibility(8);
			holder.container_cancel.setVisibility(8);
			holder.btPay.setVisibility(8);
			holder.hahaha.setVisibility(8);
		}else if("6".equals(bean.status)){
			holder.container_daijiedan.setVisibility(8);
			holder.btPay.setEnabled(false);
			holder.container_yiyueyue.setVisibility(0);
			holder.container_goAssess.setVisibility(8);
			holder.container_lookAssess.setVisibility(8);
			holder.container_cancel.setVisibility(8);
		}
		// 5 待评价 线下支付 等待评价 和2一样
		/** 小费按钮点击事件 */
		holder.btAddFee.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				
				ToastUtils.showProgress(mContext);
				AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
				String url = URLS.BASESERVER + URLS.User.fee;
				RequestParams params = new RequestParams();
				params.put(URLS.ACCESS_TOKEN, mUser.access_token);
				client.post(url, params, new MyAsyncResponseHandler() {
					
					private int	mCheckedItem;

					@Override
					public void success(String json)
					{
						List<FeeBean> data = new  ArrayList<FeeBean>();
						Gson gson = new Gson();
						FeeBean feeData = gson.fromJson(json, FeeBean.class);
						List<Fee> listData = feeData.content;
						 mCheckedItem = 0;
						final String[] items = new String[listData.size()];
						for(int i = 0; i < listData.size(); i++){
							items[i] = listData.get(i).value;
							if(listData.get(i).select.equals("1")){
								mCheckedItem = i;
							}
						}
						AlertDialog.Builder builder = new Builder(mContext);
						builder.setSingleChoiceItems(items, mCheckedItem, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								mCheckedItem = which;
							}
						});
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								String fee = items[mCheckedItem];
								AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
								String url = URLS.BASESERVER + URLS.User.addFee;
								RequestParams params = new RequestParams();
								params.put(URLS.ACCESS_TOKEN, mUser.access_token);
								params.put("fee", fee);
								params.put("sn", bean.sn);
								ToastUtils.showProgress(mContext);
								client.post(url, params, new MyAsyncResponseHandler() {

									@Override
									public void success(String json)
									{
										try
										{
											JSONObject content = JsonUtils.getContent(json);
											String newfee = content.getString("fee");
											String newBudget = content.getString("budget");
											OrderBean orderBean = mDataSource.get(position);
											orderBean.budget = newBudget;
											orderBean.fee = newfee;
											mDataSource.set(position, orderBean);
											notifyDataSetChanged();
											ToastUtils.makeShortText(UIUtils.getContext(), "小费信息添加成功！");
										}
										catch (JSONException e)
										{
											e.printStackTrace();
										}
									}
								});
							}
							
						});
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
							}
						});
						builder.show();
					}
				});
				
				
			}
		});
		/** 去付款按钮点击事件 */
		holder.btPay.setOnClickListener(new MyOnClickLsterner(mContext, holder, bean));
		/** 行程点击事件 */
		holder.btLook.setOnClickListener(new MyOnClickLsterner(mContext, holder, bean));
		/** 去评价点击事件 */
		holder.btGoAssess.setOnClickListener(new MyOnClickLsterner(mContext, holder, bean,position));
		/** 查看评价点击事件 */
		holder.btLookAssess.setOnClickListener(new MyOnClickLsterner(mContext, holder, bean));
		return convertView;
	}

	@Override
	public void onAddFee(OrderBean orderBean,int position)
	{
		
		int index = 0;
		for(int i = 0 ; i < mDataSource.size(); i++){
			if((mDataSource.get(i).sn).equals(orderBean.sn)){
				index = i;
			}
		}
		mDataSource.set(index, orderBean);
		notifyDataSetChanged();
	}

	@Override
	public void onAssess(OrderBean orderBean)
	{
		int position = 0;
		for(int i = 0 ; i < mDataSource.size(); i++){
			if((mDataSource.get(i).sn).equals(orderBean.sn)){
				position = i;
			}
		}
		mDataSource.set(position, orderBean);
		notifyDataSetChanged();
	}

	@Override
	public void onChangeStatus(OrderBean orderBean)
	{
		int position = 0;
		for(int i = 0 ; i < mDataSource.size(); i++){
			if((mDataSource.get(i).sn).equals(orderBean.sn)){
				position = i;
			}
		}
		mDataSource.set(position, orderBean);
		notifyDataSetChanged();
	}

}

class AddFeeAdapter extends MyBaseApdater<String>
{
	public AddFeeAdapter(List<String> dataSource) {
		super(dataSource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		FeeViewHolder holder = null;
		if (convertView == null)
		{
			holder = new FeeViewHolder();
			convertView = View.inflate(UIUtils.getContext(), R.layout.item_dialog_adapter, null);
			convertView.setTag(holder);
			holder.tv = (TextView) convertView.findViewById(R.id.item_tv_dialog_title);
		}
		else
		{
			holder = (FeeViewHolder) convertView.getTag();
		}
		holder.tv.setText((String) getItem(position));
		return convertView;
	}

}

class MyOnClickLsterner implements OnClickListener
{
	private OrderViewHolder	mHolder;
	private boolean			mIsShow	= false;
	private List<OrderBean>	mDataSource;
	private Context			mContext;
	private OrderBean		mOrderBean;

	public MyOnClickLsterner(OrderViewHolder holder) {
		mHolder = holder;
		doIvArrowAnimation();
	}

	public MyOnClickLsterner(Context context, OrderViewHolder holder, List<OrderBean> dataSource) {
		mContext = context;
		mDataSource = dataSource;
		mHolder = holder;
	}

	/** 我的行程，去付款 初始数值传递 */
	public MyOnClickLsterner(Context context, OrderViewHolder holder, OrderBean bean) {
		mContext = context;
		mHolder = holder;
		mOrderBean = bean;
	}
	private int position;
	public MyOnClickLsterner(Context context, OrderViewHolder holder, OrderBean bean, int position) {
		mContext = context;
		mHolder = holder;
		mOrderBean = bean;
		position = position;
	}

	@Override
	public void onClick(View v)
	{
		if (v == mHolder.ivArrow)
		{
			doIvArrowAnimation();
		}
		else if (v == mHolder.btPay)
		{
			Bundle bundle = new Bundle();
			bundle.putSerializable(MyConstains.ITEMBEAN, mOrderBean);
			startActivity(YuYueDetailUI.class, bundle);
		}
		else if (v == mHolder.btLook)
		{ // 查看我的行程
			Bundle bundle = new Bundle();
			bundle.putSerializable(MyConstains.ITEMBEAN, mOrderBean);
			startActivity(MyRouteUI.class, bundle);

		}
		else if (v == mHolder.btGoAssess)
		{
			// 去评价
			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			bundle.putSerializable(MyConstains.ITEMBEAN, mOrderBean);
			startActivity(AssessDetailUI.class, bundle);
		}
		else if (v == mHolder.btLookAssess)
		{
			// 查看评价
			Bundle bundle = new Bundle();
			bundle.putSerializable(MyConstains.ITEMBEAN, mOrderBean);
			startActivity(LookAssessUI.class, bundle);
		}
	}

	public void doIvArrowAnimation()
	{
		mHolder.container_orderDetail.setVisibility(mIsShow ? 0 : 8);
		mHolder.ivArrow.setImageResource(mIsShow ? R.drawable.arrow_up : R.drawable.arrow_down);
		mIsShow = !mIsShow;
	}

	/**
	 * 开启一个新界面,并携带数据
	 * 
	 * @param clazz
	 */
	public void startActivity(Class clazz, Bundle bundle)
	{
		Intent intent = new Intent(mContext, clazz);
		intent.putExtra(BaseActivity.VALUE_PASS, bundle);
		mContext.startActivity(intent);
		((Activity) mContext).overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
	}
}

class OrderViewHolder
{
	TextView		tvHangban;
	TextView   		tvXingli;
	TextView        tvSign;
	TextView		tvTime;				// 08:00
	TextView		tvStatus;				// 订单状态
	TextView		tvLevel;				// 乘车级别 豪华5人座
	TextView		tvType;				// 类型 专车或者拼车
	TextView		tvPeople;				// 乘车人数
	TextView		tvPort;				// 口岸
	TextView		tvScPosition;			// 上车地点
	TextView		tvXcPosition;			// 下车地点
	TextView		tvBudget;				// 预算
	TextView		tvFee;					// 小费
	Button			btLook;				// 查看我的行程
	Button			btAddFee;				// 加小费
	Button			btPay;					// 去付款
	Button			btGoAssess;			// 去评价
	Button			btLookAssess;			// 查看评价
	ImageView		ivArrow;				// 箭头
	Button 			btCancel;
	TextView		tvDriverName;
	TextView		tvCarId;
	RelativeLayout	container_cancel;		//已取消
	RelativeLayout	container_daijiedan;	// 待接单状态
	LinearLayout	container_yiyueyue;	// 已预约状态
	LinearLayout	container_orderDetail;	// 详情
	RelativeLayout	container_goAssess;
	RelativeLayout	container_lookAssess;
	RelativeLayout  hahaha;
}

class FeeViewHolder
{
	TextView	tv;
}