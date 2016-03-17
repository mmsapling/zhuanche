package com.baidu.zhuanche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;

public class DialogAdapter extends MyBaseApdater<String>
{

	public DialogAdapter(Context context, List<String> dataSource) {
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
		String title = (String) getItem(position);
		holder.tv.setText(title);
		return convertView;
	}

}
class DialogViewHolder
{
	TextView	tv;
}