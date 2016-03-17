package com.baidu.zhuanche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.UserIndexBean.Article;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.utils.ImageUtils;

public class HotAskAdapter extends MyBaseApdater<Article>
{

	


	public HotAskAdapter(Context context, List<Article> dataSource) {
		super(context, dataSource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		HotAskViewHolder holder = null;
		if(convertView == null){
			holder = new HotAskViewHolder();
			convertView = View.inflate(mContext, R.layout.item_user_home_listview, null);
			convertView.setTag(holder);
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.item_user_home_iv_icon);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.item_user_home_tv_title);
			holder.tvText = (TextView) convertView.findViewById(R.id.item_user_home_tv_text);
		}
		else
		{
			holder = (HotAskViewHolder) convertView.getTag();
		}
		Article bean = (Article) getItem(position);
		holder.tvTitle.setText(bean.title);
		holder.tvText.setText(bean.description);
		ImageUtils imageUtils = new ImageUtils(mContext);
		imageUtils.display(holder.ivIcon, URLS.BASE + bean.img);
		return convertView;
	}
}

class HotAskViewHolder
{
	ImageView	ivIcon;
	TextView	tvTitle;
	TextView	tvText;
}
