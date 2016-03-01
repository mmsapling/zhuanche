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

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.adapter
 * @类名: AccountAdapter
 * @创建者: 陈选文
 * @创建时间: 2016-1-6 上午11:07:54
 * @描述: 进帐适配器
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
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
