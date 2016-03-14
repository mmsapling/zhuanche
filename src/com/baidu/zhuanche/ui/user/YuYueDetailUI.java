package com.baidu.zhuanche.ui.user;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.android.app.sdk.AliPay;
import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.DialogSignAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.FeeBean.Fee;
import com.baidu.zhuanche.bean.OrderListBean.DriverInfo;
import com.baidu.zhuanche.bean.OrderListBean.OrderBean;
import com.baidu.zhuanche.bean.FeeBean;
import com.baidu.zhuanche.bean.Sex;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.pay.apay.Keys;
import com.baidu.zhuanche.pay.apay.PayResult;
import com.baidu.zhuanche.pay.apay.Result;
import com.baidu.zhuanche.pay.apay.Rsa;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.JsonUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.view.CircleImageView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class YuYueDetailUI extends BaseActivity implements OnClickListener
{

	private OrderBean			mOrderBean;
	private RelativeLayout		mContainerDriver;
	private CircleImageView		mCivPic;
	private RatingBar			mRatingBar;
	private TextView			mTvCarid;
	private TextView			mTvDriverName;
	private TextView			mTvCarNum;
	private ImageView			mIvCall;

	private TextView			mTvPrice;
	private LinearLayout		mContainerPay;
	private Button				mBtStatus;
	private User				user;
	private LinearLayout		mContainerFee;
	private RelativeLayout		mContainerItemFee;	;
	private TextView			mTvFee;

	private RadioGroup			mRadioGroup;
	private static final int	RQF_PAY		= 1;

	private static final int	RQF_LOGIN	= 2;

	@Override
	public void init()
	{
		super.init();
		Bundle bundle = getIntent().getBundleExtra(VALUE_PASS);
		mOrderBean = (OrderBean) bundle.getSerializable(MyConstains.ITEMBEAN);
		position = bundle.getInt("position");
		user = BaseApplication.getUser();
	}

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_yuyue_detail);
		mContainerDriver = (RelativeLayout) findViewById(R.id.yyd_container_driver);
		mTvPrice = (TextView) findViewById(R.id.yyd_tv_price);
		mCivPic = (CircleImageView) findViewById(R.id.yyd_civ_pic);
		mRatingBar = (RatingBar) findViewById(R.id.yyd_ratingbar);
		mIvCall = (ImageView) findViewById(R.id.yyd_iv_call);
		mTvDriverName = (TextView) findViewById(R.id.yyd_tv_drivername);
		mTvCarid = (TextView) findViewById(R.id.yyd_tv_carid);
		mTvCarNum = (TextView) findViewById(R.id.yyd_tv_carnum);
		mContainerPay = (LinearLayout) findViewById(R.id.yyd_container_pay);
		mBtStatus = (Button) findViewById(R.id.yyd_bt_status);
		mContainerFee = (LinearLayout) findViewById(R.id.yyd_container_fee);
		mContainerItemFee = (RelativeLayout) findViewById(R.id.yyd_container_itemfee);
		mTvFee = (TextView) findViewById(R.id.yyd_tv_fee);
		mRadioGroup = (RadioGroup) findViewById(R.id.yyd_radiogroup);
	}

	@Override
	public void initData()
	{
		super.initData();
		mBuilder = new Builder(this);
		setData(mOrderBean.d_del);
		setStatusData(mOrderBean.status);
		mTvFee.setText("￥" + mOrderBean.fee);
		float price = Float.parseFloat(mOrderBean.budget) + Float.parseFloat(mOrderBean.fee);
		mTvPrice.setText("￥" + price);

	}

	private void setData(DriverInfo info)
	{
		if (TextUtils.isEmpty(info.name)) { return; }
		mImageUtils.display(mCivPic, URLS.BASE + info.icon);
		mTvCarNum.setText(info.carid);
		mTvDriverName.setText(info.name);
		mRatingBar.setRating(Float.parseFloat(info.star));
		mTvCarid.setText(info.type);
	}

	private void setStatusData(String status)
	{
		mTvTitle.setText("预约详情");
		if ("0".equals(status))
		{
			// 加小费，不看司机
			mContainerDriver.setVisibility(8);
			mContainerPay.setVisibility(8);
			mContainerFee.setVisibility(0);
			mBtStatus.setText("取消");

		}
		else if ("1".equals(status))
		{
			// 待出发 不看小费，看支付 看司机
			mContainerDriver.setVisibility(0);
			mContainerPay.setVisibility(0);
			mContainerFee.setVisibility(8);
			mTvTitle.setText("支付详情");
			mBtStatus.setText("确认支付");
		}
		else if ("2".equals(status))
		{
			// 已经付款了 不看小费，不看支付，看司机
			mContainerDriver.setVisibility(0);
			mContainerPay.setVisibility(8);
			mContainerFee.setVisibility(8);
			mBtStatus.setText("评价");
		}
		else if ("3".equals(status))
		{
			// 已经评价了
			mContainerDriver.setVisibility(0);
			mContainerPay.setVisibility(8);
			mContainerFee.setVisibility(8);
			mBtStatus.setText("查看评价");
		}
		else if ("4".equals(status))
		{
			// 已经取消了
			mContainerDriver.setVisibility(8);
			mContainerPay.setVisibility(8);
			mContainerFee.setVisibility(8);
			mBtStatus.setText("已取消");
			mBtStatus.setEnabled(false);
			mBtStatus.setPressed(true);
		}
		else if ("5".equals(status))
		{
			// 线下支付
			// 已经付款了 不看小费，不看支付，看司机
			mContainerDriver.setVisibility(0);
			mContainerPay.setVisibility(8);
			mContainerFee.setVisibility(8);
			mBtStatus.setText("等待确认");
			mBtStatus.setEnabled(false);
		}

	}

	@Override
	public void initListener()
	{
		mIvLeftHeader.setOnClickListener(this);
		mContainerItemFee.setOnClickListener(this);
		mBtStatus.setOnClickListener(this);
		mIvCall.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
		else if (v == mContainerItemFee)
		{
			showDialogFee();
		}
		else if (v == mBtStatus)
		{
			doClickStatus();
		}
		else if (v == mIvCall)
		{
			showCallDialog();
		}
	}

	private Dialog	mDialog;

	private void doClickStatus()
	{
		if (mBtStatus.getText().equals("取消"))
		{
			// 取消订单
			cancelOrder();
		}
		else if (mBtStatus.getText().equals("确认支付"))
		{
			gopay();
		}
		else if (mBtStatus.getText().equals("评价"))
		{
			// 去评价
			Bundle bundle = new Bundle();
			bundle.putSerializable(MyConstains.ITEMBEAN, mOrderBean);
			startActivityAndFinish(AssessDetailUI.class, bundle);
		}
		else if (mBtStatus.getText().equals("查看评价"))
		{
			// 查看评价
			Bundle bundle = new Bundle();
			bundle.putSerializable(MyConstains.ITEMBEAN, mOrderBean);
			startActivity(LookAssessUI.class, bundle);
		}
	}

	private void gopay()
	{
		int id = mRadioGroup.getCheckedRadioButtonId();
		switch (id)
		{
			case R.id.yyd_rb_xianjin:
				payxianjing();
				break;
			case R.id.yyd_rb_weixin:
				break;
			case R.id.yyd_rb_zfb:
				payzfb();
				break;
			default:
				ToastUtils.makeShortText("请选择支付方式！");
				break;
		}
	}

	/**
	 * 支付宝支付
	 */
	private void payzfb()
	{
		String url = URLS.BASESERVER + URLS.User.payment;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, BaseApplication.getUser().access_token);
		params.put(URLS.TYPE, "zfb");
		params.put("sn", mOrderBean.sn);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				try
				{
					JSONObject object = JsonUtils.getContent(json);
					JSONObject jsonObject = object.getJSONObject("desc");
					String alipayKey = jsonObject.getString("alipayKey");
					String orderInfo1 = jsonObject.getString("orderInfo");
					String partner = jsonObject.getString("partner");
					String sellerID = jsonObject.getString("sellerID");

					// alipayKey uac71bri06e7cexektpg883f3vqghmjj
					// orderInfo
					// partner="2088121000279565"&seller_id="crossbordercar@163.com"&out_trade_no="20160121032232079920"&subject="订单支付"&body="订单支付427元"&total_fee="427"&notify_url="http://gjxc.fanc.com.cn/index.php/User/Order/Pay/Alipay/notify_url.php"&service="mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay="30m"&return_url="m.alipay.com"
					// partner 2088121000279565
					// sellerID crossbordercar@163.com

					String sign = Rsa.sign(orderInfo1, Keys.PRIVATE);
					sign = URLEncoder.encode(sign);
					orderInfo1 += "&sign=\"" + sign + "\"&" + getSignType();
					final String orderInfo = orderInfo1;
					Log.i("ExternalPartner", "start pay");
					// start the pay.
					/** ---------------生成支付串码的过程 end--------------- **/
					new Thread() {

						public void run()
						{
							AliPay alipay = new AliPay(YuYueDetailUI.this, mHandler);

							// 设置为沙箱模式，不设置默认为线上环境
							// alipay.setSandBox(true);

							String result = alipay.pay(orderInfo);// orderInfo支付串码

							Log.i("tylz", "result = " + result);
							Message msg = new Message();
							msg.what = RQF_PAY;
							msg.obj = result;
							mHandler.sendMessage(msg);
						}
					}.start();
				}
				catch (JSONException e)
				{
					ToastUtils.makeShortText("json解析异常！");
					e.printStackTrace();
				}
			}
		});
	}

	Handler	mHandler	= new Handler() {
							public void handleMessage(android.os.Message msg)
							{
								
								switch (msg.what)
								{
									case RQF_PAY:
										PayResult payResult = new PayResult((String) msg.obj);
										String resultInfo = payResult.getResult();
										String resultStatus = payResult.getResultStatus();
										if(resultStatus.equals("9000")){
											ToastUtils.makeShortText("支付成功！");
											mOrderBean.status = "2";
											if (mAddFeeListener != null)
											{
												mAddFeeListener.onChangeStatus(mOrderBean);
											}
											setStatusData("2");
										}else{
											if(resultStatus.equals("8000")){
												ToastUtils.makeShortText("支付结果确认中！");
											}else{
												ToastUtils.makeShortText("支付失败！");
											}
										}
										break;
									default:
										break;
								}
							};
						};

	private String getSignType()
	{
		return "sign_type=\"RSA\"";
	}

	/**
	 * 现金支付
	 */
	private void payxianjing()
	{
		String url = URLS.BASESERVER + URLS.User.payment;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, BaseApplication.getUser().access_token);
		params.put(URLS.TYPE, "cash");
		params.put("sn", mOrderBean.sn);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				// TODO
				mOrderBean.status = "5";
				if (mAddFeeListener != null)
				{
					mAddFeeListener.onChangeStatus(mOrderBean);
				}
				ToastUtils.makeShortText("现金支付成功！");
				setStatusData("5");
			}
		});
	}

	private void cancelOrder()
	{
		String url = URLS.BASESERVER + URLS.User.orderCancel;
		RequestParams params = new RequestParams();
		params.add("sn", mOrderBean.sn);
		params.add(URLS.ACCESS_TOKEN, user.access_token);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				if (mAddFeeListener != null)
				{
					mOrderBean.status = "" + 4;
					mAddFeeListener.onAddFee(mOrderBean, position);
					setStatusData(mOrderBean.status);
				}
			}
		});
	}

	private AlertDialog.Builder	mBuilder;

	private void showDialogFee()
	{
		// List<Sex> dataSource = new ArrayList<Sex>();
		// dataSource.add(new Sex("10", "0"));
		// dataSource.add(new Sex("20", "1"));
		// dataSource.add(new Sex("30", "0"));
		// dataSource.add(new Sex("40", "1"));
		// dataSource.add(new Sex("50", "0"));
		// final DialogSignAdapter adapter = new DialogSignAdapter(this,
		// dataSource);
		// mBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which)
		// {
		// Sex sex = (Sex) adapter.getItem(which);
		// addFee(sex.name);
		// }
		// });
		// mBuilder.show();
		// ------------------

		ToastUtils.showProgress(this);
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.User.fee;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, BaseApplication.getUser().access_token);
		client.post(url, params, new MyAsyncResponseHandler() {

			private int	mCheckedItem;

			@Override
			public void success(String json)
			{
				List<FeeBean> data = new ArrayList<FeeBean>();
				Gson gson = new Gson();
				FeeBean feeData = gson.fromJson(json, FeeBean.class);
				List<Fee> listData = feeData.content;
				mCheckedItem = 0;
				final String[] items = new String[listData.size()];
				for (int i = 0; i < listData.size(); i++)
				{
					items[i] = listData.get(i).value;
					if (listData.get(i).select.equals("1"))
					{
						mCheckedItem = i;
					}
				}
				AlertDialog.Builder builder = new Builder(YuYueDetailUI.this);
				builder.setSingleChoiceItems(items, mCheckedItem, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						mCheckedItem = which;
					}
				});
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						String fee = items[mCheckedItem];
						addFee(fee);
					}

				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				builder.show();
			}
		});

	}

	protected void addFee(final String name)
	{
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
		String url = URLS.BASESERVER + URLS.User.addFee;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, user.access_token);
		params.put("fee", name);
		params.put("sn", mOrderBean.sn);
		ToastUtils.showProgress(this);
		client.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				String fee = mTvFee.getText().toString().replace("￥", "");
				float ff = Float.parseFloat(fee) + Float.parseFloat(name);
				mTvFee.setText("￥" + ff);
				float price = Float.parseFloat(mOrderBean.budget) + ff;
				mTvPrice.setText("￥" + price);
				try
				{
					JSONObject content = JsonUtils.getContent(json);
					String newfee = content.getString("fee");
					String newBudget = content.getString("budget");
					mOrderBean.budget = newBudget;
					mOrderBean.fee = newfee;
					// 接口回调
					if (mAddFeeListener != null)
					{
						mAddFeeListener.onAddFee(mOrderBean, position);
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private int						position;

	private static OnAddFeeListener	mAddFeeListener;

	public static void setOnAddFeeListener(OnAddFeeListener addFeeListener)
	{
		mAddFeeListener = addFeeListener;
	}

	public interface OnAddFeeListener
	{
		void onAddFee(OrderBean orderBean, int position);

		void onChangeStatus(OrderBean orderBean);
	}

	@Override
	public void onBackPressed()
	{
		// super.onBackPressed();
		finishActivity();
	}

	private void showCallDialog()
	{
		if (mDialog == null)
		{
			mDialog = new Dialog(this, R.style.CustomBottomDialog);
			mDialog.setCanceledOnTouchOutside(false);
			// mDialog.setCancelable(false);
			// 获取对话框，并设置窗口参数
			Window window = mDialog.getWindow();
			LayoutParams params = new LayoutParams();
			// 不能写成这样,否则Dialog显示不出来
			// LayoutParams params = new
			// LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			// 对话框窗口的宽和高
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.WRAP_CONTENT;
			// 对话框显示的位置
			params.x = 120;
			params.y = UIUtils.dip2Px(1000);
			window.setAttributes(params);
			// 设置对话框布局
			mDialog.setContentView(R.layout.layout_call);
			Button btCancle = (Button) mDialog.findViewById(R.id.call_bt_cancle);
			final TextView tvName = (TextView) mDialog.findViewById(R.id.call_bt_text1);
			final TextView tvName1 = (TextView) mDialog.findViewById(R.id.call_bt_text2);
			tvName1.setVisibility(0);
			tvName.setText(mOrderBean.d_del.mobile);
			tvName1.setText(mOrderBean.d_del.mobile1);
			tvName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mOrderBean.d_del.mobile));
					startActivity(intent);

				}
			});
			tvName1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mOrderBean.d_del.mobile1));
					startActivity(intent);

				}
			});
			btCancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					dismiss();
				}
			});
			mDialog.show();
		}
	}

	public void dismiss()
	{
		// 隐藏对话框之前先判断对话框是否存在，以及是否正在显示
		if (mDialog != null && mDialog.isShowing())
		{
			mDialog.dismiss();
			mDialog = null;
		}
	}

}
