package com.baidu.zhuanche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.Sex;
import com.baidu.zhuanche.utils.UIUtils;

public class DialogSexAdapter extends MyBaseApdater<Sex>
{

	public DialogSexAdapter(Context context, List<Sex> dataSource) {
		super(context, dataSource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		DialogDriverViewHolder holder = null;
		if (convertView == null)
		{
			holder = new DialogDriverViewHolder();
			convertView = View.inflate(UIUtils.getContext(), R.layout.item_dialog, null);
			convertView.setTag(holder);
			holder.tvName = (TextView) convertView.findViewById(R.id.item_tv_name);
		}
		else
		{
			holder = (DialogDriverViewHolder) convertView.getTag();
		}
		Sex bean = (Sex) getItem(position);
		holder.tvName.setText(bean.name);
		return convertView;
	}
	private class DialogDriverViewHolder
	{
		TextView	tvName;
	}
}
