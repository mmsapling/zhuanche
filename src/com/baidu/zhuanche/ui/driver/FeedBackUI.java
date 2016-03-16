package com.baidu.zhuanche.ui.driver;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.ToastUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;


public class FeedBackUI extends BaseActivity implements OnClickListener
{
	private EditText	mEtSuggestion;
	private EditText	mEtTitle;
	private Button		mBtSubmit;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_driver_feedback);
		mEtTitle = (EditText) findViewById(R.id.feedback_et_title);
		mEtSuggestion = (EditText) findViewById(R.id.feedback_et_suggestion);
		mBtSubmit = (Button) findViewById(R.id.feedback_bt_submit);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("用戶反饋");
	}

	@Override
	public void initListener()
	{
		mIvLeftHeader.setOnClickListener(this);
		mBtSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
		else if (v == mBtSubmit)
		{
			String suggest = mEtSuggestion.getText().toString();
			String title = mEtTitle.getText().toString();
			if( TextUtils.isEmpty(mEtSuggestion.getText().toString())){
				ToastUtils.makeShortText(this, "請輸入你的寶貴意見！");
				return;
			}
			if(TextUtils.isEmpty(title)){
				ToastUtils.makeShortText(this, "請輸入標題！");
				return;
			}
			ToastUtils.showProgress(this);
			AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
			String url = URLS.BASESERVER + URLS.Driver.feedback;
			RequestParams params = new RequestParams();
			params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
			params.add("title", title);
			params.add("content", suggest);
			params.add("type", "2");
			client.post(url, params, new MyAsyncResponseHandler() {
				
				@Override
				public void success(String json)
				{
					ToastUtils.makeShortText("非常感謝你提供的寶貴意見，我們會儘快處理！");
					finishActivity();
				}
			});
			
		
		}
	}

	@Override
	public void onBackPressed()
	{
		finishActivity();
	}
}
