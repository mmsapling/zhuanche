package com.baidu.zhuanche.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseHolder;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.utils.AtoolsUtil;
import com.baidu.zhuanche.utils.UIUtils;

/**
 * @项目名: ZhuanChe
 * @包名: com.baidu.zhuanche.holder
 * @类名: InfoPicHolder
 * @创建者: 陈选文
 * @创建时间: 2015-12-8 下午9:06:37
 * @描述: 手机
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class InfoMobileHolder extends BaseHolder<User>
{


	private TextView	mTvMobile;

	@Override
	protected View initHolderView()
	{
		View view = View.inflate(UIUtils.getContext(), R.layout.holder_info_mobile, null);
		mTvMobile = (TextView) view.findViewById(R.id.info_tv_mobile);
		return view;
	}

	@Override
	protected void refreshHolderView(User data)
	{
		if (!TextUtils.isEmpty(data.mobile))
		{
			mTvMobile.setText(AtoolsUtil.mobile4(data.mobile));
		}
	
	}

	

}
