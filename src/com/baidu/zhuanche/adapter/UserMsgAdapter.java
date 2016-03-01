package com.baidu.zhuanche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.Msg;
import com.baidu.zhuanche.utils.AtoolsUtil;

/**
 * @项目名: ZhuanChe
 * @包名: com.baidu.zhuanche.adapter
 * @类名: MyMsgAdapter
 * @创建者: 陈选文
 * @创建时间: 2015-12-21 上午10:27:04
 * @描述: 消息适配器
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class UserMsgAdapter extends MyBaseApdater<Msg>
{
	public UserMsgAdapter(Context context, List<Msg> mDatas) {
		super(context, mDatas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_user_msg, null);
			convertView.setTag(holder);
			holder.tvDate = (TextView) convertView.findViewById(R.id.item_msg_tv_date);
			holder.tvMsg = (TextView) convertView.findViewById(R.id.item_msg_tv_msg);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		Msg bean = (Msg) getItem(position);
		holder.tvDate.setText(AtoolsUtil.unixTimeToLocalTime(bean.createtime));
		holder.tvMsg.setText(bean.content);
		return convertView;
	}

	private class ViewHolder
	{
		TextView	tvMsg;
		TextView	tvDate;
	}
}
