package com.baidu.zhuanche.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.base.MyBaseApdater;
import com.baidu.zhuanche.bean.Msg;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.AtoolsUtil;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.zlist.adapter.BaseSwipeAdapter;
import com.baidu.zhuanche.zlist.enums.DragEdge;
import com.baidu.zhuanche.zlist.enums.ShowMode;
import com.baidu.zhuanche.zlist.widget.ZSwipeItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

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
public class UserMsgRefreshAdapter extends BaseSwipeAdapter
{
	private List<Msg>	mDatas;
	private Activity	mContext;

	public UserMsgRefreshAdapter(Activity context, List<Msg> datas) {
		mContext = context;
		mDatas = datas;
	}

	// @Override
	// public View getView(int position, View convertView, ViewGroup parent)
	// {
	// ViewHolder holder = null;
	// if (convertView == null)
	// {
	// holder = new ViewHolder();
	// convertView = View.inflate(mContext, R.layout.item_user_msg, null);
	// convertView.setTag(holder);
	// holder.tvDate = (TextView)
	// convertView.findViewById(R.id.item_msg_tv_date);
	// holder.tvMsg = (TextView) convertView.findViewById(R.id.item_msg_tv_msg);
	// }
	// else
	// {
	// holder = (ViewHolder) convertView.getTag();
	// }
	// Msg bean = (Msg) getItem(position);
	// holder.tvDate.setText(AtoolsUtil.unixTimeToLocalTime(bean.createtime));
	// holder.tvMsg.setText(bean.content);
	// return convertView;
	// }

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

	@Override
	public int getSwipeLayoutResourceId(int position)
	{
		return R.id.swipe_item;
	}

	@Override
	public View generateView(int position, ViewGroup parent)
	{

		return mContext.getLayoutInflater().inflate(R.layout.item_listview, parent, false);
	}

	@Override
	public void fillValues(final int position, View convertView)
	{
		final ZSwipeItem swipeItem = (ZSwipeItem) convertView.findViewById(R.id.swipe_item);
		LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);
		TextView tvDate = (TextView) convertView.findViewById(R.id.item_msg_tv_date);
		TextView tvMsg = (TextView) convertView.findViewById(R.id.item_msg_tv_msg);
		Msg bean = (Msg) getItem(position);
		tvDate.setText(AtoolsUtil.unixTimeToLocalTime(bean.createtime));
		tvMsg.setText(bean.content);
		swipeItem.setShowMode(ShowMode.LayDown);
		swipeItem.setDragEdge(DragEdge.Right);
		ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				deleteMsg(position);
				swipeItem.close();
			}
		});
	}
	private void deleteMsg(final int position)
	{
		ToastUtils.showProgress(mContext);
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.User.cleanMessage;
		RequestParams params = new RequestParams();
		final Msg msg = mDatas.get(position);
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getUser().access_token);
		params.add("id", msg.id);
		client.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), msg.title + "已清理");
				mDatas.remove(position);
				notifyDataSetChanged();
			}
		});
	}
}
