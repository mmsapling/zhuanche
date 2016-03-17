package com.baidu.zhuanche.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.CartTypeBean.LevelBean;
import com.baidu.zhuanche.utils.UIUtils;

public class LevelAdapter extends MyBaseApdater<LevelBean>
{

	public LevelAdapter(List<LevelBean> dataSource) {
		super(dataSource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = View.inflate(UIUtils.getContext(), R.layout.item_dialog_adapter, null);
			convertView.setTag(holder);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.item_tv_dialog_title);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		LevelBean bean = (LevelBean) getItem(position);
		holder.tvTitle.setText(bean.name);
		return convertView;
	}

	private class ViewHolder
	{
		TextView	tvTitle;
	}
}
