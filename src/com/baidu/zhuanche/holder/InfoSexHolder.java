package com.baidu.zhuanche.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.base.BaseHolder;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: ZhuanChe
 * @包名: com.baidu.zhuanche.holder
 * @类名: InfoNameHolder
 * @创建者: 陈选文
 * @创建时间: 2015-12-9 下午6:38:22
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class InfoSexHolder extends BaseHolder<User> implements OnClickListener
{

	private ImageView		mIvArrow;
	private ImageView		mIvSelect1;
	private ImageView		mIvSelect2;
	private TextView		mTvSex;
	private RelativeLayout	mContainerSexed;	// 已经命名的条目
	private boolean			isShow	= true;
	private LinearLayout	mContainerSex;
	private User			mUser;

	@Override
	protected View initHolderView()
	{
		View view = View.inflate(UIUtils.getContext(), R.layout.holder_info_sex, null);
		mContainerSex = (LinearLayout) view.findViewById(R.id.info_container_sex);
		mContainerSexed = (RelativeLayout) view.findViewById(R.id.info_cotainer_sexed);
		mIvArrow = (ImageView) view.findViewById(R.id.info_iv_arrow);
		mIvSelect1 = (ImageView) view.findViewById(R.id.info_iv_select1);
		mIvSelect2 = (ImageView) view.findViewById(R.id.info_iv_select2);
		mTvSex = (TextView) view.findViewById(R.id.info_tv_sex);
		return view;
	}

	@Override
	protected void refreshHolderView(User data)
	{
		mUser = data;
		if (!TextUtils.isEmpty(data.gender))
		{
			if (data.gender.equals("0"))
			{
				mTvSex.setText("未设置");
				mIvSelect1.setImageResource(R.drawable.dui_normal);
				mIvSelect2.setImageResource(R.drawable.dui_normal);
			}
			else if (data.gender.equals("1"))
			{
				mTvSex.setText("男");
				mIvSelect1.setImageResource(R.drawable.dui_normal);
				mIvSelect2.setImageResource(R.drawable.dui_pressed);
			}
			else
			{
				mTvSex.setText("女");
				mIvSelect1.setImageResource(R.drawable.dui_pressed);
				mIvSelect2.setImageResource(R.drawable.dui_normal);
			}
		}
		mContainerSexed.setOnClickListener(this);
		mIvSelect1.setOnClickListener(this);
		mIvSelect2.setOnClickListener(this);
		doAnimation();
	}

	@Override
	public void onClick(View v)
	{
		if (v == mContainerSexed)
		{
			doAnimation();
		}
		else if (v == mIvSelect1)
		{
			mIvSelect1.setImageResource(R.drawable.dui_pressed);
			mIvSelect2.setImageResource(R.drawable.dui_normal);
			doAnimation();
			// PreferenceUtils.putString(UIUtils.getContext(),
			// MyConstains.USER_SEX, "2"); //女
			mTvSex.setText("女");
			AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
			String url = URLS.BASESERVER + URLS.User.modifyUser;
			RequestParams params = new RequestParams();
			params.add(URLS.ACCESS_TOKEN, mUser.access_token);
			params.add("field", "gender");
			params.add("value", "2");
			client.post(url, params, new MyAsyncResponseHandler() {
				
				@Override
				public void success(String json)
				{
					User user = BaseApplication.getUser();
					user.gender = "2";
					BaseApplication.setUser(user);
					ToastUtils.makeShortText(UIUtils.getContext(), "修改信息成功！");
				}
			});
		}
		else if (v == mIvSelect2)
		{
			mIvSelect1.setImageResource(R.drawable.dui_normal);
			mIvSelect2.setImageResource(R.drawable.dui_pressed);
			doAnimation();
			mTvSex.setText("男");
			// PreferenceUtils.putString(UIUtils.getContext(),
			// MyConstains.USER_SEX, "1"); // 男
			AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
			String url = URLS.BASESERVER + URLS.User.modifyUser;
			RequestParams params = new RequestParams();
			params.add(URLS.ACCESS_TOKEN, mUser.access_token);
			params.add("field", "gender");
			params.add("value", "1");
			client.post(url, params, new MyAsyncResponseHandler() {
				
				@Override
				public void success(String json)
				{
					User user = BaseApplication.getUser();
					user.gender = "1";
					BaseApplication.setUser(user);
					ToastUtils.makeShortText(UIUtils.getContext(), "修改信息成功！");
				}
			});
		}
	}

	private void doAnimation()
	{
		mContainerSex.setVisibility(isShow ? View.GONE : View.VISIBLE);
		mIvArrow.setImageResource(isShow ? R.drawable.arrow_down : R.drawable.arrow_up);
		isShow = !isShow;
	}

}
