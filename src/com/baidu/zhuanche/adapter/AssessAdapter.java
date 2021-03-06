package com.baidu.zhuanche.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.AllAssessBean.Assess;
import com.baidu.zhuanche.utils.DateFormatUtil;
import com.baidu.zhuanche.utils.OrderUtil;


public class AssessAdapter extends MyBaseApdater<Assess>
{

	public AssessAdapter(Context context, List<Assess> dataSource) {
		super(context, dataSource);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_assess, null);
			convertView.setTag(holder);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.item_assess_ratingbar);
			holder.tvAssess = (TextView) convertView.findViewById(R.id.item_assess_tv_assess);
			holder.tvMobile = (TextView) convertView.findViewById(R.id.item_assess_tv_mobile);
			holder.tvTime = (TextView) convertView.findViewById(R.id.item_assess_tv_time);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		Assess bean = (Assess) getItem(position);
		Log.d("tylz", bean.star);
		holder.ratingBar.setRating(Float.parseFloat(bean.star));
		holder.tvAssess.setText(bean.remark.equals("(null)") ? "":bean.remark);
		holder.tvMobile.setText(bean.mobile);
		holder.tvTime.setText(OrderUtil.getDateText(bean.createtime));
		return convertView;
	}
	private class ViewHolder{
		TextView tvMobile;
		TextView tvTime;
		TextView tvAssess;
		RatingBar ratingBar;
	}
}
