package com.baidu.zhuanche.ui.driver;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: ApplyCashUI
 * @创建者: 陈选文
 * @创建时间: 2016-1-6 上午9:12:59
 * @描述: 申请提现
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class ApplyCashUI extends BaseActivity implements OnClickListener
{
	private String		mCanCash;		// 可提现金额
	private EditText	mEtName;
	private EditText	mEtNum;
	private EditText	mEtMoney;
	private EditText	mEtBank;		// 开户银行
	private EditText	mEtBankAddress; // 所在支行地
	private TextView    mTvCanWithDrwa; 
	private TextView    mTvCharge;
	private Button		mBtOk;
	@Override
	public void init()
	{
		super.init();
		Bundle bundle = getIntent().getBundleExtra(VALUE_PASS);
		mCanCash = bundle.getString("canCash");
	}

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_applycash);
		mEtName = (EditText) findViewById(R.id.applycash_et_name);
		mEtNum = (EditText) findViewById(R.id.applycash_et_idcardnum);
		mEtMoney = (EditText) findViewById(R.id.applycash_et_cashcount);
		mEtBank = (EditText) findViewById(R.id.applycash_et_bankname);
		mEtBankAddress = (EditText) findViewById(R.id.applycash_et_location);
		mTvCanWithDrwa = (TextView) findViewById(R.id.applycash_tv_cancash);
		mTvCharge =(TextView) findViewById(R.id.applycash_tv_charge);
		mBtOk = (Button) findViewById(R.id.applycash_bt_submit);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("申請提現");
		mTvCanWithDrwa.setText("￥" + mCanCash);
	}

	@Override
	public void initListener()
	{
		mIvLeftHeader.setOnClickListener(this);
		mEtMoney.addTextChangedListener(new MoneyTextWatcher());
		mBtOk.setOnClickListener(this);
	}

	@Override
	public void onBackPressed()
	{
		finishActivity();
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}else if(v == mBtOk){
			doClickOk();
		}
	}
	/**确认提现*/
	private void doClickOk()
	{
		String name = mEtName.getText().toString().trim();
		String num = mEtNum.getText().toString().trim();
		String money = mEtMoney.getText().toString().trim();
		String bank = mEtBank.getText().toString().trim();
		String bankAddress = mEtBankAddress.toString().trim();
		String fee = mTvCharge.getText().toString().replace("￥", "");
		if(TextUtils.isEmpty(name) ||TextUtils.isEmpty(num) ||TextUtils.isEmpty(money) ||TextUtils.isEmpty(bank)||TextUtils.isEmpty(bankAddress)){
			ToastUtils.makeShortText(UIUtils.getContext(), UIUtils.getString(R.string.info_not_empty));
			return;
		}
		String url = URLS.BASESERVER + URLS.Driver.withdrawApply;
		RequestParams params = new RequestParams();
		params.add(URLS.ACCESS_TOKEN, BaseApplication.getDriver().access_token);
		params.add("name", name);
		params.add("bank_number", num);
		params.add("bank_name", bank);
		params.add("bank_location",bankAddress);
		params.add("money", money);
		params.add("fee", fee);
		mClient.post(url, params, new MyAsyncResponseHandler() {
			
			@Override
			public void success(String json)
			{
				ToastUtils.makeShortText(UIUtils.getContext(),"申请提現成功！");
				float money = Float.parseFloat(mEtMoney.getText().toString().trim());
				mCanCash = "" +( Float.parseFloat(mCanCash) - money - (money * 0.05));
				mTvCanWithDrwa.setText("￥" + mCanCash);
				clearData();
			}
		});
	}
	/**
	 * 清空数据
	 */
	protected void clearData()
	{
		mEtName.setText("");
		mEtNum.setText("");
		mEtMoney.setText("");
		mEtBank.setText("");
		mEtBankAddress.setText("");
	}
	class MoneyTextWatcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			if(TextUtils.isEmpty(s)){
				mTvCharge.setText("");
				return;
			}
			/*不能大于可提现金额*/
			float money = Float.parseFloat(s.toString());
			if(money > Float.parseFloat(mCanCash)){
				ToastUtils.makeShortText(UIUtils.getContext(), "你的可體現金額爲" + mCanCash + "元");
				money = Float.parseFloat(mCanCash);
				mEtMoney.setText(mCanCash);
			}
			money = money * 0.05f;
			money = Math.round((money * 100 ))/100f;
			mTvCharge.setText("￥" + money);
		}

		@Override
		public void afterTextChanged(Editable s)
		{
		}
		
	}
}
