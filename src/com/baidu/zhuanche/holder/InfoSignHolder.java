package com.baidu.zhuanche.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
public class InfoSignHolder extends BaseHolder<User> implements OnClickListener
{

	private RelativeLayout	mContainerSign;
	private ImageView		mIvArrow;
	private ImageView		mIvError;
	private EditText		mEtSign;
	private RelativeLayout	mContainerSigned;	// 已经命名的条目

	private boolean			isShow	= true;
	private User			mUser;
	@Override
	protected View initHolderView()
	{
		View view = View.inflate(UIUtils.getContext(), R.layout.holder_info_sign, null);
		mContainerSign = (RelativeLayout) view.findViewById(R.id.info_cotainer_sign);
		mContainerSigned = (RelativeLayout) view.findViewById(R.id.info_cotainer_signed);
		mIvArrow = (ImageView) view.findViewById(R.id.info_iv_arrow);
		mIvError = (ImageView) view.findViewById(R.id.info_iv_error);
		mEtSign = (EditText) view.findViewById(R.id.info_et_sign);
		return view;
	}

	@Override
	protected void refreshHolderView(User data)
	{
		mUser = data;
		if (!TextUtils.isEmpty(data.autograph))
		{
			mEtSign.setText(data.autograph);
		}
		mContainerSigned.setOnClickListener(this);
		doAnimation();
		mIvError.setOnClickListener(this);
		mEtSign.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				final String sign = mEtSign.getText().toString();
				if (!hasFocus && !sign.equals(mUser.autograph))
				{// 如果没有焦点且两次签名不一样，修改
					AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
					String url = URLS.BASESERVER + URLS.User.modifyUser;
					RequestParams params = new RequestParams();
					params.add(URLS.ACCESS_TOKEN, mUser.access_token);
					params.add("field", "autograph");
					params.add("value", sign);
					client.post(url, params, new MyAsyncResponseHandler() {
						
						@Override
						public void success(String json)
						{
							User user = BaseApplication.getUser();
							user.autograph = sign;
							BaseApplication.setUser(user);
							// mTvName.setText(user.autograph);
							ToastUtils.makeShortText(UIUtils.getContext(), "修改个性签名成功！");
						}
					});
				}
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		if (v == mContainerSigned)
		{
			doAnimation();
		}
		else if (v == mIvError)
		{
			mEtSign.setText("");
		}
	}

	private void doAnimation()
	{
		mContainerSign.setVisibility(isShow ? View.GONE : View.VISIBLE);
		mIvArrow.setImageResource(isShow ? R.drawable.arrow_down : R.drawable.arrow_up);
		isShow = !isShow;
	}

}
