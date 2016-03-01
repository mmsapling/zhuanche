package com.baidu.zhuanche.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.DriverCenterOrderListBean.OrderBean;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.utils.AtoolsUtil;
import com.baidu.zhuanche.utils.DateFormatUtil;
import com.baidu.zhuanche.utils.ImageUtils;
import com.baidu.zhuanche.utils.OrderUtil;
import com.baidu.zhuanche.view.CircleImageView;

public class DriverOrderListAdapter extends MyBaseApdater<OrderBean>
	{

		public DriverOrderListAdapter(Context context, List<OrderBean> datas) {
			super(context, datas);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder = null;
			if (convertView == null)
			{
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.item_driver_listview, null);
				convertView.setTag(holder);
				holder.ivPic = (CircleImageView) convertView.findViewById(R.id.item_driver_civ_photo);
				holder.tvTime = (TextView) convertView.findViewById(R.id.item_driver_tv_time);
				holder.tvGetOn = (TextView) convertView.findViewById(R.id.item_driver_tv_geton);
				holder.tvGetOff = (TextView) convertView.findViewById(R.id.item_driver_tv_getoff);
				holder.tvLevel = (TextView) convertView.findViewById(R.id.item_driver_tv_level);
				holder.tvCarPool = (TextView) convertView.findViewById(R.id.item_driver_tv_carpool);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.item_driver_tv_price);
				holder.tvFee = (TextView) convertView.findViewById(R.id.item_driver_tv_fee);
				holder.tvStatus = (TextView) convertView.findViewById(R.id.item_driver_tv_status);
				holder.ivCall = (ImageView) convertView.findViewById(R.id.item_driver_iv_call);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			final OrderBean bean = (OrderBean) getItem(position);
			holder.tvTime.setText(OrderUtil.getDateText(bean.time));
			holder.tvGetOn.setText(bean.from);
			holder.tvGetOff.setText(bean.to);
			holder.tvCarPool.setText(AtoolsUtil.getCarPool(bean.carpool));
			holder.tvLevel.setText(bean.cartype);
			holder.tvPrice.setText(bean.budget + "元");
			holder.tvFee.setText(bean.fee + "元");
			holder.tvStatus.setText(AtoolsUtil.getDriverStatus(bean.status));
			ImageUtils imageUtils = new ImageUtils(mContext);
			imageUtils.display(holder.ivPic, URLS.BASE + bean.icon);
			/**撥打電話*/
			holder.ivCall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.mobile));
					mContext.startActivity(intent);
				}
			});
			return convertView;
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
			TextView		tvStatus;
			ImageView		ivCall;
		}
	}

	
