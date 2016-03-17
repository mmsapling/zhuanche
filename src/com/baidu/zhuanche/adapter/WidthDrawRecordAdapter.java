package com.baidu.zhuanche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.WithDrawRecordBean.WithDraw;
import com.baidu.zhuanche.utils.AtoolsUtil;
import com.baidu.zhuanche.utils.DateFormatUtil;

public class WidthDrawRecordAdapter extends MyBaseApdater<WithDraw>
{

	public WidthDrawRecordAdapter(Context context, List<WithDraw> datas) {
		super(context, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		WidthDrawRecordViewHolder holder = null;
		if (convertView == null)
		{
			holder = new WidthDrawRecordViewHolder();
			convertView = View.inflate(mContext, R.layout.item_withdraw_record, null);
			convertView.setTag(holder);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.item_record_tv_money);
			holder.tvNum = (TextView) convertView.findViewById(R.id.item_record_tv_cardnum);
			holder.tvName = (TextView) convertView.findViewById(R.id.item_record_tv_name);
			holder.tvTime = (TextView) convertView.findViewById(R.id.item_record_tv_time);
		}
		else
		{
			holder = (WidthDrawRecordViewHolder) convertView.getTag();
		}
		WithDraw bean = (WithDraw) getItem(position);
		holder.tvMoney.setText("提現金額：￥" + bean.money);
		holder.tvNum.setText("卡號尾數：" + AtoolsUtil.getEndCardNum(bean.bank_number));
		holder.tvName.setText("用戶名：" + bean.name);
		holder.tvTime.setText(DateFormatUtil.getDateStr());
		return convertView;
	}

}

class WidthDrawRecordViewHolder
{
	TextView	tvMoney;
	TextView	tvName;
	TextView	tvNum;
	TextView	tvTime;
}