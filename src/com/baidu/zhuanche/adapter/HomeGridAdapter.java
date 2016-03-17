package com.baidu.zhuanche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.bean.UserIndexBean.Cate;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.utils.ImageUtils;
import com.baidu.zhuanche.utils.UIUtils;

public class HomeGridAdapter extends BaseAdapter
{
	private Context		mContext;
	private List<Cate>	mDatas;

	public HomeGridAdapter(Context context, List<Cate> gridViewDatas) {
		mContext = context;
		mDatas = gridViewDatas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		HomeGridViewHolder holder = null;
		if (convertView == null)
		{
			holder = new HomeGridViewHolder();
			convertView = View.inflate(UIUtils.getContext(), R.layout.item_user_home_gridview, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.item_uh_gridview_pic);
			holder.tv = (TextView) convertView.findViewById(R.id.item_uh_gridview_name);
			convertView.setTag(holder);
		}
		else
		{
			holder = (HomeGridViewHolder) convertView.getTag();
		}
		Cate bean = (Cate) getItem(position);
		ImageUtils imageUtils = new ImageUtils(mContext);
		imageUtils.display(holder.iv, URLS.BASE + bean.icon);
		holder.tv.setText(bean.name);
		return convertView;
	}

	@Override
	public int getCount()
	{
		if (mDatas != null) { return mDatas.size(); }
		return 0;
	}

	@Override
	public Object getItem(int position)
	{
		if (mDatas != null) { return mDatas.get(position); }
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

}

class HomeGridViewHolder
{
	ImageView	iv;
	TextView	tv;
}
