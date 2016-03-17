package com.baidu.zhuanche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;


public class QuhaoAdapter extends MyBaseApdater<String>
{

	public QuhaoAdapter(Context context, List<String> dataSource) {
		super(context, dataSource);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_dialog_adapter, null);
			convertView.setTag(holder);
			holder.tv = (TextView) convertView.findViewById(R.id.item_tv_dialog_title);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		String title = (String) getItem(position);
		holder.tv.setText(title);
		return convertView;
	}
	
}
class ViewHolder{
	TextView tv;
}