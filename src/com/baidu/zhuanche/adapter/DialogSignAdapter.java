package com.baidu.zhuanche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.Sex;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.adapter
 * @类名: QuhaoAdapter
 * @创建者: 陈选文
 * @创建时间: 2015-12-26 下午4:18:38
 * @描述: 通用对话框适配器
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DialogSignAdapter extends MyBaseApdater<Sex>
{

	public DialogSignAdapter(Context context, List<Sex> dataSource) {
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
		Sex bean = (Sex) getItem(position);
		holder.tv.setText(bean.name);
		return convertView;
	}
	private class DialogViewHolder
	{
		TextView	tv;
	}
}
