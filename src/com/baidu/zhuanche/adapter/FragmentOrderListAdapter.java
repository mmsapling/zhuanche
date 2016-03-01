package com.baidu.zhuanche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.DriverCenterOrderListBean.OrderBean;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.utils.AtoolsUtil;
import com.baidu.zhuanche.utils.ImageUtils;
import com.baidu.zhuanche.utils.OrderUtil;
import com.baidu.zhuanche.view.CircleImageView;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.adapter
 * @类名:	FragmentOrderListAdapter
 * @创建者:	陈选文
 * @创建时间:	2016-1-8	上午10:43:49 
 * @描述:    司机端全部订单fragment中订单适配器
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class FragmentOrderListAdapter extends MyBaseApdater<OrderBean>
{

	public FragmentOrderListAdapter(Context context, List<OrderBean> dataSource) {
		super(context, dataSource);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_fragment_order, null);
			convertView.setTag(holder);
			holder.ivPic = (CircleImageView) convertView.findViewById(R.id.item_fragment_order_civ_photo);
			holder.tvTime = (TextView) convertView.findViewById(R.id.item_fragment_order_tv_time);
			holder.tvGetOn = (TextView) convertView.findViewById(R.id.item_fragment_order_tv_geton);
			holder.tvGetOff = (TextView) convertView.findViewById(R.id.item_fragment_order_tv_getoff);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.item_fragment_order_tv_status);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		OrderBean bean = (OrderBean) getItem(position);
		ImageUtils imageUtils = new ImageUtils(mContext);
		imageUtils.display(holder.ivPic, URLS.BASE + bean.icon);
		holder.tvTime.setText(OrderUtil.getDateText(bean.time));
		holder.tvGetOn.setText(bean.from);
		holder.tvGetOff.setText(bean.to);
		holder.tvStatus.setText(AtoolsUtil.getDriverStatus(bean.status));
		return convertView;
	}
	private class ViewHolder
	{
		CircleImageView	ivPic;
		TextView		tvTime;
		TextView		tvGetOn;
		TextView		tvGetOff;
		TextView		tvStatus;
	}
	private void changeStatusColor(String status){
		int index = Integer.parseInt(status);
		switch (index)
		{
			case 1: //待出發
				
				break;

			default:
				break;
		}
	}
}
