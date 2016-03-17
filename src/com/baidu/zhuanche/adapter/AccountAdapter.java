package com.baidu.zhuanche.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.AccountBean.Account;
import com.baidu.zhuanche.utils.DateFormatUtil;

public class AccountAdapter extends MyBaseApdater<Account>
{



	public AccountAdapter(Context context, List<Account> dataSource) {
		super(context, dataSource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_accout, null);
			convertView.setTag(holder);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.item_accout_tv_money);
			holder.tvTime = (TextView) convertView.findViewById(R.id.item_accout_tv_time);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.item_accout_tv_title);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		Account bean = (Account) getItem(position);
		holder.tvMoney.setText("+" + bean.money);
		holder.tvTime.setText(DateFormatUtil.getDateTimeStr(new Date(Long.parseLong(bean.time) * 1000)));
		return convertView;
	}

	private class ViewHolder
	{
		TextView	tvTitle;
		TextView	tvTime;
		TextView	tvMoney;
	}
}
