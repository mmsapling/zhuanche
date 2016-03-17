package com.baidu.zhuanche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.GetSeaport;

public class DialogGetSeaportAdapter extends MyBaseApdater<GetSeaport>
{

	public DialogGetSeaportAdapter(Context context, List<GetSeaport> dataSource) {
		super(context, dataSource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		DialogViewHolder holder = null;
		if (convertView == null)
		{
			holder = new DialogViewHolder();
			convertView = View.inflate(mContext, R.layout.item_dialog_adapter, null);
			convertView.setTag(holder);
			holder.tv = (TextView) convertView.findViewById(R.id.item_tv_dialog_title);
		}
		else
		{
			holder = (DialogViewHolder) convertView.getTag();
		}
		GetSeaport bean = (GetSeaport) getItem(position);
		if(!bean.name.equals("不限定口岸")){
			String startTime = bean.start_hour + ":" + bean.start_min;
			String endTime = bean.end_hour + ":" + bean.end_min;
			holder.tv.setText(bean.name + "(" + startTime + "-" + endTime + ")");
		}else{
			holder.tv.setText(bean.name );
		}
			
		return convertView;
	}

	private class DialogViewHolder
	{
		TextView	tv;
	}
}
