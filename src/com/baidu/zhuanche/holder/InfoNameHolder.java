package com.baidu.zhuanche.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
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
public class InfoNameHolder extends BaseHolder<User> implements OnClickListener
{

	private RelativeLayout	mContainerName;
	private ImageView		mIvArrow;
	private ImageView		mIvError;
	private EditText		mEtName;
	private TextView		mTvName;
	private RelativeLayout	mContainerNamed;	// 已经命名的条目

	private boolean			isShow	= true;
	private User			mUser;

	@Override
	protected View initHolderView()
	{
		View view = View.inflate(UIUtils.getContext(), R.layout.holder_info_name, null);
		mContainerName = (RelativeLayout) view.findViewById(R.id.info_cotainer_name);
		mContainerNamed = (RelativeLayout) view.findViewById(R.id.info_cotainer_named);
		mIvArrow = (ImageView) view.findViewById(R.id.info_iv_arrow);
		mIvError = (ImageView) view.findViewById(R.id.info_iv_error);
		mEtName = (EditText) view.findViewById(R.id.info_et_name);
		mTvName = (TextView) view.findViewById(R.id.info_tv_name);
		return view;
	}

	@Override
	protected void refreshHolderView(User data)
	{
		mUser = data;
		if (!TextUtils.isEmpty(data.username))
		{
			mTvName.setText(data.username);
			mEtName.setText(data.username);
		}
		mContainerNamed.setOnClickListener(this);
		doAnimation();
		mIvError.setOnClickListener(this);
		mEtName.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				final String username = mEtName.getText().toString();
				//当失去焦点且文本框中的值不等于原来的值，改变用户名
				if(!hasFocus && !mUser.username.equals(username)){
					AsyncHttpClient client =  AsyncHttpClientUtil.getInstance();
					String url = URLS.BASESERVER + URLS.User.modifyUser;
					RequestParams params = new RequestParams();
					params.add(URLS.ACCESS_TOKEN, mUser.access_token);
					params.add("field", "username");
					params.add("value", username);
					client.post(url, params, new MyAsyncResponseHandler() {
						
						@Override
						public void success(String json)
						{
							mUser.username = username;
							BaseApplication.setUser(mUser);
							mTvName.setText(mUser.username);
							ToastUtils.makeShortText(UIUtils.getContext(), "修改用户名成功！");
							if(mListener != null){
								mListener.onModifyName(username);
							}
						}
					});
				}
			}
		});
	}
	private static OnModifyNameListener mListener;
	public static void setOnModifyNameListener(OnModifyNameListener listener){
		mListener = listener;
	}
	public interface OnModifyNameListener{
		void onModifyName(String name);
	}
	@Override
	public void onClick(View v)
	{
		if (v == mContainerNamed)
		{
			doAnimation();
		}
		else if (v == mIvError)
		{
			mEtName.setText("");
		}
	}

	private void doAnimation()
	{
		mContainerName.setVisibility(isShow ? View.GONE : View.VISIBLE);
		mIvArrow.setImageResource(isShow ? R.drawable.arrow_down : R.drawable.arrow_up);
		isShow = !isShow;
	}

}
