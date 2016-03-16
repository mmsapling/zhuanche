package com.baidu.zhuanche.ui.driver;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.QuhaoAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.JsonUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
    
public class DriverFindPasswordUI extends BaseActivity implements OnClickListener
{
	private EditText		mEtNumber; 
	private TextView		mTvQuhao;
	private EditText		mEtCode;
	private Button			mBtNext;
	private Button			mBtGetCode;
	private List<String>	mDatas;

	private LinearLayout	mContainerQuhao;
	private QuhaoAdapter	mAdapter;
	protected String		mVerifyCode	= "";

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_driver_findpassword);
		mContainerQuhao = (LinearLayout) findViewById(R.id.df_container_quhao);
		mTvQuhao = (TextView) findViewById(R.id.df_tv_quhao);
		mEtNumber = (EditText) findViewById(R.id.df_et_number);
		mEtCode = (EditText) findViewById(R.id.df_et_yanzhengma);
		mBtGetCode = (Button) findViewById(R.id.df_bt_yanzhengma);
		mBtNext = (Button) findViewById(R.id.df_bt_next);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("修改密碼");
		mDatas = new ArrayList<String>();
		mDatas.add("+86");
		mDatas.add("+852");
		mAdapter = new QuhaoAdapter(this, mDatas);
	}

	@Override
	public void initListener()
	{
		super.initListener();
		mContainerQuhao.setOnClickListener(this);
		mBtGetCode.setOnClickListener(this);
		mIvLeftHeader.setOnClickListener(this);
		mBtNext.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mContainerQuhao)
		{
			doClickQuhao();
		}
		else if (v == mBtGetCode)
		{
			doClickGetCode();
		}
		else if (v == mIvLeftHeader)
		{
			finishActivity();
		}
		else if (v == mBtNext)
		{
			doClickNext();
		}
		else if (v == mIvLeftHeader)
		{
			finishActivity();
		}
	}

	/** 找回密码 */
	private void doClickNext()
	{
		String code = mEtCode.getText().toString().trim();
		String number = mEtNumber.getText().toString().trim();
		if (TextUtils.isEmpty(code) || TextUtils.isEmpty(number))
		{
			ToastUtils.makeShortText(this, "請完善信息!");
			return;
		}
		if (TextUtils.isEmpty(mVerifyCode))
		{
			ToastUtils.makeShortText(this, "請先獲取驗證碼!");
			return;
		}
		// 传送数据过去 TODO
		Intent intent = new Intent(this, DriverFindPasswordNextUI.class);
		intent.putExtra("code", mVerifyCode);
		intent.putExtra("mobile", number);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
	}

	/** 获取验证码 */
	private void doClickGetCode()
	{
		String number = mEtNumber.getText().toString().trim();
		if (TextUtils.isEmpty(number))
		{
			ToastUtils.makeShortText(this, "请输入手机号码！");
			return;
		}
		showTimeCountDown(mBtGetCode);
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.Driver.verify;
		RequestParams params = new RequestParams();
		params.put(URLS.MOBILE, number);
		ToastUtils.showProgress(this);
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				ToastUtils.closeProgress();
				String json = new String(arg2);
				/** 获取验证码,并未验证码赋值 */
				try
				{
					JSONObject content = JsonUtils.getContent(json);
					mVerifyCode = content.getString("verify");
				}
				catch (JSONException e)
				{
					e.printStackTrace();
					ToastUtils.makeShortText(UIUtils.getContext(), "Json解析出错！");
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
				ToastUtils.closeProgress();
			}
		});
	}

	/** 区号选择 */
	private void doClickQuhao()
	{
		AlertDialog.Builder builder = new Builder(this);
		builder.setAdapter(mAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String quhao = (String) mAdapter.getItem(which);
				mTvQuhao.setText(quhao);
			}
		});
		builder.show();
	}

}
