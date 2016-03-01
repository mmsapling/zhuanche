package com.baidu.zhuanche.ui.user;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.OrderListBean.DriverInfo;
import com.baidu.zhuanche.bean.OrderListBean.OrderBean;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.conf.MyConstains;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.view.CircleImageView;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.user
 * @类名: AssessDetailUI
 * @创建者: 陈选文
 * @创建时间: 2016-1-14 下午4:22:28
 * @描述: 用户端去评价页面
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class AssessDetailUI extends BaseActivity implements OnClickListener
{
	private OrderBean		mOrderBean;

	private RelativeLayout	mContainerDriver;
	private CircleImageView	mCivPic;
	private RatingBar		mRatingBar;
	private TextView		mTvCarid;
	private TextView		mTvDriverName;
	private TextView		mTvCarNum;
	private ImageView		mIvCall;
	private TextView		mTvPrice;
	private RatingBar		mAssessRationBar;
	private EditText		mEtAssess;
	private Button			mBtSubmit;
	private User			user;
	@Override
	public void init()
	{
		super.init();
		user = BaseApplication.getUser();
		Bundle bundle = getIntent().getBundleExtra(VALUE_PASS);
		position = bundle.getInt("position");
		mOrderBean = (OrderBean) bundle.getSerializable(MyConstains.ITEMBEAN);
	}

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_assessment);
		mCivPic = (CircleImageView) findViewById(R.id.layout_driver_iv_pic);
		mTvDriverName = (TextView) findViewById(R.id.layout_driver_tv_name);
		mRatingBar = (RatingBar) findViewById(R.id.layout_driver_ratingbar12);
		mTvCarid = (TextView) findViewById(R.id.layout_driver_tv_carid);
		mTvCarNum = (TextView) findViewById(R.id.layout_driver_tv_car_name);
		mIvCall = (ImageView) findViewById(R.id.layout_driver_iv_call);
		mTvPrice = (TextView) findViewById(R.id.assess_tv_price111);
		mAssessRationBar = (RatingBar) findViewById(R.id.assess_rb_ratingbar);
		mEtAssess = (EditText) findViewById(R.id.assess_et_pingjia);
		mBtSubmit = (Button) findViewById(R.id.assess_bt_submit);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("用户评价");
		float price = Float.parseFloat(mOrderBean.budget) + Float.parseFloat(mOrderBean.fee);
		mTvPrice.setText(price + "元");
		setData(mOrderBean.d_del);
	}

	private void setData(DriverInfo info)
	{
		if(TextUtils.isEmpty(info.name)){
			return;
		}
		mImageUtils.display(mCivPic, URLS.BASE + info.icon);
		mTvDriverName.setText(info.name);
		mTvCarid.setText(info.carid);
		mTvCarNum.setText(info.type);
		mRatingBar.setRating(Float.parseFloat(info.star));
	}

	@Override
	public void initListener()
	{
		mIvLeftHeader.setOnClickListener(this);
		mIvCall.setOnClickListener(this);
		mBtSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			finishActivity();
		}
		else if (v == mIvCall)
		{
			showCallDialog();
		}else if(v == mBtSubmit){
			submit();
		}
	}

	private void submit()
	{
		String assess = mEtAssess.getText().toString().trim();
		if(TextUtils.isEmpty(assess)){
			ToastUtils.makeShortText("请输入评价！");
			return;
		}
		String url = URLS.BASESERVER + URLS.User.addDriverComment;
		ToastUtils.showProgress(this);
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, user.access_token);
		params.put("driver_id", mOrderBean.d_del.id);
		params.put("remark", assess);
		params.put("sn",mOrderBean.sn);
		params.put("star",mAssessRationBar.getRating());
		mClient.post(url, params, new MyAsyncResponseHandler() {
			
			@Override
			public void success(String json)
			{
				if(mOnAssessListener != null){
					mOrderBean.status = "3";
					mOnAssessListener.onAssess(mOrderBean);
				}
				mAssessRationBar.setEnabled(false);
				mEtAssess.setEnabled(false);
				mBtSubmit.setEnabled(false);
				mBtSubmit.setText("评价成功！");
				
			}
		});
	}
	public static OnAssessListener mOnAssessListener;
	public static void setOnAssessListener(OnAssessListener onAssessListener){
		mOnAssessListener = onAssessListener;
	}
	public interface OnAssessListener{
		void onAssess(OrderBean orderBean);
	}
	@Override
	public void onBackPressed()
	{
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

	private Dialog	mDialog;
	private int		position;

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
