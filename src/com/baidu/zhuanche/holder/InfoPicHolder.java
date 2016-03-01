package com.baidu.zhuanche.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseHolder;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.ui.user.UserInfoUI;
import com.baidu.zhuanche.utils.ImageUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.view.CircleImageView;

/**
 * @项目名: ZhuanChe
 * @包名: com.baidu.zhuanche.holder
 * @类名: InfoPicHolder
 * @创建者: 陈选文
 * @创建时间: 2015-12-8 下午9:06:37
 * @描述: 头像
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class InfoPicHolder extends BaseHolder<User>
{
	public CircleImageView	mCivPic;
	private UserInfoUI		mActivity;
	private RelativeLayout	mContainer;

	public InfoPicHolder(UserInfoUI activity) {
		mActivity = activity;
	}

	@Override
	protected View initHolderView()
	{
		View view = View.inflate(UIUtils.getContext(), R.layout.holder_info_pic, null);
		mCivPic = (CircleImageView) view.findViewById(R.id.info_civ_pic);
		mContainer = (RelativeLayout) view.findViewById(R.id.info_container);
		return view;
	}

	@Override
	protected void refreshHolderView(User data)
	{
		setOnModifyIconListener(mActivity);
		if (!TextUtils.isEmpty(data.icon))
		{
			String uri = URLS.BASE + data.icon;
			ImageUtils i = new ImageUtils(UIUtils.getContext());
			i.display(mCivPic, uri);
		}
		// 为容器添加点击事件
		mContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if (mListener != null)
				{
					mListener.setOnModifyIcon(mCivPic);
				}
			}
		});

	}

	public OnModifyIconListener	mListener;

	public void setOnModifyIconListener(OnModifyIconListener listener)
	{
		mListener = listener;
	}

	/**
	 * @描述：修改头像的事件监听回调
	 * @描述: 点击回调接口
	 */
	public interface OnModifyIconListener
	{
		void setOnModifyIcon(CircleImageView civ);
	}

}
