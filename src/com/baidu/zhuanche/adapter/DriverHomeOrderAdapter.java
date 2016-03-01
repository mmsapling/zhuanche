package com.baidu.zhuanche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.DriverHomeBean.DriverHomeOrder;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.ui.driver.DriverUI;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.AtoolsUtil;
import com.baidu.zhuanche.utils.ImageUtils;
import com.baidu.zhuanche.utils.OrderUtil;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.view.CircleImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.adapter
 * @类名: DriverHomeOrderAdapter
 * @创建者: 陈选文
 * @创建时间: 2016-1-14 上午9:46:10
 * @描述: 司機端首頁適配器
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DriverHomeOrderAdapter extends MyBaseApdater<DriverHomeOrder>
{
	private static OnReceiverOrderListener	mListener;

	public DriverHomeOrderAdapter(Context context, List<DriverHomeOrder> dataSource) {
		super(context, dataSource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_driverhome_listview, null);
			convertView.setTag(holder);
			holder.ivPic = (CircleImageView) convertView.findViewById(R.id.item_driverhome_civ_photo);
			holder.tvTime = (TextView) convertView.findViewById(R.id.driverhome_tv_time);
			holder.tvGetOn = (TextView) convertView.findViewById(R.id.driverhome_tv_geton);
			holder.tvGetOff = (TextView) convertView.findViewById(R.id.driverhome_tv_getoff);
			holder.tvLevel = (TextView) convertView.findViewById(R.id.driverhome_tv_level);
			holder.tvCarPool = (TextView) convertView.findViewById(R.id.driverhome_tv_carpool);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.driverhome_tv_price);
			holder.tvFee = (TextView) convertView.findViewById(R.id.driverhome_tv_fee);
			holder.tvSpace = (TextView) convertView.findViewById(R.id.driverhome_tv_distance);
			holder.btAcceptOrder = (Button) convertView.findViewById(R.id.driverhome_bt_acceptorder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		final DriverHomeOrder bean = (DriverHomeOrder) getItem(position);
		holder.btAcceptOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				receiverOrder(bean);

			}
		});
		holder.tvTime.setText(OrderUtil.getDateText(bean.time));
		holder.tvGetOn.setText(bean.from);
		holder.tvGetOff.setText(bean.to);
		holder.tvCarPool.setText(AtoolsUtil.getCarPool(bean.carpool));
		holder.tvLevel.setText(bean.cartype);
		holder.tvPrice.setText(bean.budget);
		holder.tvFee.setText(bean.fee);
		holder.tvSpace.setText(AtoolsUtil.getSpace(bean.range));
		ImageUtils utils = new ImageUtils(mContext);
		utils.display(holder.ivPic, URLS.BASE + bean.icon);
		return convertView;
	}

	protected void receiverOrder(final DriverHomeOrder bean)
	{
		ToastUtils.showProgress(mContext);
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.Driver.receiveOrder;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		params.put("sn", bean.sn);
		client.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				if(mListener != null){
					mListener.onReceiverOrder(bean);
				}
				startActivity(DriverUI.class);
			}
		});
	}

	private class ViewHolder
	{
		CircleImageView	ivPic;
		TextView		tvTime;
		TextView		tvGetOn;
		TextView		tvGetOff;
		TextView		tvLevel;
		TextView		tvCarPool;
		TextView		tvPrice;
		TextView		tvFee;
		TextView		tvSpace;
		Button			btAcceptOrder;
	}

	public static void setOnReceiverOrderListener(OnReceiverOrderListener listener)
	{
		mListener = listener;
	}

	public interface OnReceiverOrderListener
	{
		void onReceiverOrder(DriverHomeOrder order);
	}

}
