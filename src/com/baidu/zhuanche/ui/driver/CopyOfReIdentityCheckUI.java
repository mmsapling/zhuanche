package com.baidu.zhuanche.ui.driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.DialogDriverLevelAdapter;
import com.baidu.zhuanche.adapter.DialogDriverSeaportAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.CartTypeBean;
import com.baidu.zhuanche.bean.CartTypeBean.LevelBean;
import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.bean.IdentityCheckBean;
import com.baidu.zhuanche.bean.IdentityCheckBean.Identity;
import com.baidu.zhuanche.bean.SearportBean;
import com.baidu.zhuanche.bean.SearportBean.SearPort;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.CopyOfImageUtils;
import com.baidu.zhuanche.utils.PhotoUtilChange;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.view.CircleImageView;
import com.baidu.zhuanche.view.SelectPicPopupWindow;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: IdentityCheckUI
 * @创建者: 陈选文
 * @创建时间: 2016-1-4 下午2:03:32
 * @描述: 身份审核
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class CopyOfReIdentityCheckUI extends BaseActivity implements OnClickListener
{
	private Dialog					mDialog;										// 底部对话框
	private int						selectedLevelPosition	= 0;					// 默认选中位置
	private int						selectedSeaportPosition	= 0;
	private int						status					= 0;
	private Button					mBtSubmit;
	private Button					mBtOK;
	private Button					mBtCancel;
	private ListView				mListView;
	private List<LevelBean>			mLevelDatas;									// 等级数据
	private List<SearPort>			mSeaportDatas;									// 港口数据
	private SelectPicPopupWindow	mPopupWindow;
	private RelativeLayout			mContainerLevel;								// 等級
	private RelativeLayout			mContainerSeaport;								// 港口
	/* 审核的三个状态图标 */
	private ImageView				mIvStatusCheck;
	private ImageView				mIvStatusChecking;
	private ImageView				mIvStatusChecked;
	private static String			path					= "/sdcard/myHead/";	// sd路径
	/* 审核数据 */
	private TextView				mTvLevel;										// 等级
	private TextView				mTvSeaport;									// 港口
	private EditText				mEtCarpool;									// 车型
	private EditText				mEtCarNum;										// 车牌号
	private EditText				mEtName;
	private EditText				mEtIdCard;
	private EditText				mEtZjzh;
	private ImageView				mIvCarNum;										// 车牌号照片
	private ImageView				mIvZjzh;										// 准驾证号照片
	private ImageView				mIvIdcard;										// 身份证
	private Bitmap					carnum;										// 车牌号Bitmap
	private Bitmap					zjzh;											// 准驾证号Bitmap
	private Bitmap					idcard;										// 身份证Bitmap
	private Driver					mDriver;
	private File					mCarnumFile;
	private File					mZjzhFile;
	private File					mIdcardFile;
	private CircleImageView			mCivPhoto;
	@Override
	public void initView()
	{
		setContentView(R.layout.ui_identity_check);
		mIvStatusCheck = (ImageView) findViewById(R.id.ic_iv_status01);
		mIvStatusChecking = (ImageView) findViewById(R.id.ic_iv_status02);
		mIvStatusChecked = (ImageView) findViewById(R.id.ic_iv_status03);
		mContainerLevel = (RelativeLayout) findViewById(R.id.ic_container_level);
		mTvLevel = (TextView) findViewById(R.id.ic_tv_carlevel);
		mEtCarpool = (EditText) findViewById(R.id.ic_et_carpool);
		mEtCarNum = (EditText) findViewById(R.id.ic_et_carnum);
		mEtName = (EditText) findViewById(R.id.ic_et_name);
		mEtIdCard = (EditText) findViewById(R.id.ic_et_idcard);
		mEtZjzh = (EditText) findViewById(R.id.ic_et_zjzh);
		mIvCarNum = (ImageView) findViewById(R.id.ic_iv_carpai);
		mIvZjzh = (ImageView) findViewById(R.id.ic_iv_zjzh);
		mIvIdcard = (ImageView) findViewById(R.id.ic_iv_idcard);
		mContainerSeaport = (RelativeLayout) findViewById(R.id.ic_container_seaport);
		mTvSeaport = (TextView) findViewById(R.id.ic_tv_seaport);
		mBtSubmit = (Button) findViewById(R.id.ic_bt_submit);
		mCivPhoto = (CircleImageView) findViewById(R.id.ic_civ_photo);
	}
	
	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("身份審覈");
		mDriver = BaseApplication.getDriver();
		mImageUtils.display(mCivPhoto, URLS.BASE + mDriver.icon);
		// 圖標的設置
		setStatusImg();
		// 根據狀態設置默認值
		//setDefaultValue();
	}

	private void setDefaultValue()
	{
		if ("3".equals(mDriver.status) || "2".equals(mDriver.status))
		{
			ToastUtils.showProgress(this);
			String url = URLS.BASESERVER + URLS.Driver.showVerifyInfo;
			RequestParams params = new RequestParams();
			params.put(URLS.ACCESS_TOKEN, mDriver.access_token);
			mClient.post(url, params, new MyAsyncResponseHandler() {

				@Override
				public void success(String json)
				{
					processDefaultJson(json);
				}
			});
			/* 設置不能輸入框不能用 */
			enableView(false);
		}
		else if ("4".equals(mDriver.status))
		{
			ToastUtils.showProgress(this);
			String url = URLS.BASESERVER + URLS.Driver.showVerifyInfo;
			RequestParams params = new RequestParams();
			params.put(URLS.ACCESS_TOKEN, mDriver.access_token);
			mClient.post(url, params, new MyAsyncResponseHandler() {

				@Override
				public void success(String json)
				{
					processDefaultJson(json);
				}
			});
			/* 設置不能輸入框能用 */
			enableView(true);
		}

	}

	/**
	 * 處理默認的json
	 * 
	 * @param json
	 */
	protected void processDefaultJson(String json)
	{
		IdentityCheckBean identityCheckBean = mGson.fromJson(json, IdentityCheckBean.class);
		Identity data = identityCheckBean.content;
		/* 設置默認數據 */
		mEtName.setText(data.name);
		// mTvLevel.setText(data.cartype);
		mEtIdCard.setText(data.citizenid);
		mEtZjzh.setText(data.driverid);
		mEtCarNum.setText(data.carid);
		// mTvSeaport.setText(data.seaport);
		mEtCarpool.setText(data.type);
		//對的也要改成錯的
		CopyOfImageUtils utils = new CopyOfImageUtils(this);
		utils.display(mIvZjzh, URLS.BASE + data.driverid_pic);
		utils.display(mIvIdcard, URLS.BASE + data.citizenid_pic);
		utils.display(mIvCarNum, URLS.BASE + data.carid_pic);
		mZjzhFile = mImageUtils.getBitmapFile(URLS.BASE + data.driverid_pic);
		mIdcardFile = mImageUtils.getBitmapFile(URLS.BASE + data.citizenid_pic);
		mCarnumFile = mImageUtils.getBitmapFile(URLS.BASE + data.carid_pic);
		/* 更新数据 */
		mDriver.status = data.status;
		BaseApplication.setDriver(mDriver);
	}

	/**
	 * 設置控件是否能用
	 * 
	 * @param enabled
	 */
	private void enableView(boolean enabled)
	{
		mEtName.setEnabled(enabled);

		mEtCarNum.setEnabled(enabled);
		mEtCarpool.setEnabled(enabled);
		mContainerLevel.setEnabled(enabled);
		mContainerSeaport.setEnabled(enabled);
		mIvCarNum.setEnabled(enabled);
		mIvIdcard.setEnabled(enabled);
		mEtZjzh.setEnabled(enabled);
		mEtIdCard.setEnabled(enabled);
		mIvZjzh.setEnabled(enabled);
		mBtSubmit.setEnabled(enabled);
		mBtSubmit.setBackgroundResource(enabled ? R.drawable.bg_identity : R.drawable.bg_identity_press);
		if(!enabled && mDriver.status.equals("2")){
			mBtSubmit.setText("審覈中");
		}else if(!enabled && mDriver.status.equals("3")){
			mBtSubmit.setText("審覈通過");
		}else if(!enabled && mDriver.status.equals("4")){
			mBtSubmit.setText("審覈失敗");
		}
	}

	private void setStatusImg()
	{
		int status = Integer.parseInt(mDriver.status);
		switch (status)
		{
			case 1:
				mIvStatusCheck.setImageResource(R.drawable.ic_dui_press);
				mIvStatusChecking.setImageResource(R.drawable.ic_dui_normal);
				mIvStatusChecked.setImageResource(R.drawable.ic_dui_normal);
				break;
			case 2:
				mIvStatusCheck.setImageResource(R.drawable.ic_dui_press);
				mIvStatusChecking.setImageResource(R.drawable.ic_dui_press);
				mIvStatusChecked.setImageResource(R.drawable.ic_dui_normal);
				break;
			case 3:
				mIvStatusCheck.setImageResource(R.drawable.ic_dui_press);
				mIvStatusChecking.setImageResource(R.drawable.ic_dui_press);
				mIvStatusChecked.setImageResource(R.drawable.ic_dui_press);
				break;
			case 4:
				mIvStatusCheck.setImageResource(R.drawable.ic_dui_press);
				mIvStatusChecking.setImageResource(R.drawable.ic_dui_press);
				mIvStatusChecked.setImageResource(R.drawable.ic_dui_error);
				break;
			default:
				break;
		}
	}

	@Override
	public void initListener()
	{
		mIvLeftHeader.setOnClickListener(this);
		mContainerLevel.setOnClickListener(this);
		mIvCarNum.setOnClickListener(this);
		mIvZjzh.setOnClickListener(this);
		mIvIdcard.setOnClickListener(this);
		mContainerSeaport.setOnClickListener(this);
		mBtSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftHeader)
		{
			if("3".equals(mDriver.status)){
				finishActivity();
			}else{
				startActivityAndFinish(DriverLoginUI.class);
			}
			
		}
		else if (v == mContainerLevel)
		{
			showLevelDialog();
		}
		else if (v == mBtCancel)
		{
			dismiss();
		}
		else if (v == mIvCarNum)
		{
			setOnModifyCarNumIcon(mIvCarNum, "carnum.jpg");
		}
		else if (v == mIvZjzh)
		{
			setOnModifyZjzhIcon(mIvZjzh, "zjzh.jpg");
		}
		else if (v == mIvIdcard)
		{
			setOnModifyIdCardIcon(mIvIdcard, "idcard.jpg");
		}
		else if (v == mContainerSeaport)
		{
			showSeaportDialog();
		}
		else if (v == mBtSubmit)
		{
			try
			{
				submit();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				PrintUtils.print("文件沒找着！");
			}
		}
	}

	private void submit() throws FileNotFoundException
	{
		/* 判斷數據是否爲空 */
		String name = mEtName.getText().toString().trim();
		String carid = mEtCarNum.getText().toString().trim();
		String driverid = mEtZjzh.getText().toString().trim();
		String citizenid = mEtIdCard.getText().toString().trim();
		String type = mEtCarpool.getText().toString().trim();
		String level = mTvLevel.getText().toString().trim();
		String seaport = mTvSeaport.getText().toString().trim();
		boolean empty = TextUtils.isEmpty(name) || TextUtils.isEmpty(carid) || TextUtils.isEmpty(driverid)
						|| TextUtils.isEmpty(citizenid) || TextUtils.isEmpty(type) || TextUtils.isEmpty(level)
						|| TextUtils.isEmpty(seaport) || carnum == null || zjzh == null || idcard == null;
		boolean imgEmpty =  carnum == null || zjzh == null || idcard == null;
		if(imgEmpty){
			ToastUtils.makeShortText(this, "請選擇照片！");
			return;
		}
		if (TextUtils.isEmpty(name))
		{
			ToastUtils.makeShortText(this, "請填寫姓名！");
			return;
		}
		if (TextUtils.isEmpty(carid))
		{
			ToastUtils.makeShortText(this, "請填寫車牌號！");
			return;
		}

		if (TextUtils.isEmpty(driverid))
		{
			ToastUtils.makeShortText(this, "請填寫駕駛證號！");
			return;
		}
		if (TextUtils.isEmpty(citizenid))
		{
			ToastUtils.makeShortText(this, "請填寫身份證號！");
			return;
		}
		if (TextUtils.isEmpty(type))
		{
			ToastUtils.makeShortText(this, "請填寫車型！");
			return;
		}
		if (TextUtils.isEmpty(level))
		{
			ToastUtils.makeShortText(this, "請填寫級別！");
			return;
		}
		if (TextUtils.isEmpty(seaport))
		{
			ToastUtils.makeShortText(this, "請填寫口岸！");
			return;
		}
		if(imgEmpty){
			ToastUtils.makeShortText(this, "請上傳圖片！");
			return;
		}
		// if (!mIdcardFile.exists() || !mZjzhFile.exists() ||
		// !mCarnumFile.exists())
		// {
		// ToastUtils.makeShortText(this, "请上传照片！");
		// return;
		// }
		/* 設置輸入框不能用 */

		ToastUtils.showProgress(this);
		/* 爲bitmap創建文件 */
		File dirFile = new File(Environment.getExternalStorageDirectory(), "/myHead/");
		PrintUtils.print(dirFile.getAbsolutePath());
		mCarnumFile = new File(dirFile, "carnum.jpg");
		mZjzhFile = new File(dirFile, "zjzh.jpg");
		mIdcardFile = new File(dirFile, "idcard.jpg");
		if (!mCarnumFile.exists())
		{
			ToastUtils.makeShortText(this, "请上传車牌號照片！");
			return;
		}
		if (!mZjzhFile.exists())
		{
			ToastUtils.makeShortText(this, "请上传準駕證號照片！");
			return;
		}
		if (!mIdcardFile.exists())
		{
			ToastUtils.makeShortText(this, "请上传身份證號照片！");
			return;
		}
		String url = URLS.BASESERVER + URLS.Driver.driverVerify;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, mDriver.access_token);
		params.put("name", name);
		params.put("carid", carid);
		params.put("driverid", driverid);
		params.put("citizenid", citizenid);
		params.put("type", type);
		params.put("cartype", mLevelDatas.get(selectedLevelPosition).eid);
		params.put("seaport", mSeaportDatas.get(selectedSeaportPosition).value);
		params.put("carid_pic", mCarnumFile);
		params.put("driverid_pic", mZjzhFile);
		params.put("citizenid_pic", mIdcardFile);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				enableView(false);
				processJson(json);
			}
		});

	}

	/**
	 * 提交成功的json處理
	 * 
	 * @param json
	 */
	protected void processJson(String json)
	{
		ToastUtils.makeShortText(this, "提交成功！");
		mBtSubmit.setText("審覈中");
		String url = URLS.BASESERVER + URLS.Driver.showVerifyInfo;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, mDriver.access_token);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				
				processLookJson(json);
				//processDefaultJson(json);
			}
		});
	}

	/**
	 * 处理查看审核资料的json
	 * 
	 * @param json
	 */
	protected void processLookJson(String json)
	{
		IdentityCheckBean identityCheckBean = mGson.fromJson(json, IdentityCheckBean.class);
		Identity identity = identityCheckBean.content;
		mDriver.status = identity.status;
		BaseApplication.setDriver(mDriver);
		setStatusImg();
	}

	/** 港口對話框 */
	private void showSeaportDialog()
	{
		if (mDialog == null)
		{
			mDialog = new Dialog(this, R.style.CustomBottomDialog);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setCancelable(false);
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
			mDialog.setContentView(R.layout.layout_dialog);
			mBtCancel = (Button) mDialog.findViewById(R.id.dialog_cancel);
			mBtOK = (Button) mDialog.findViewById(R.id.dialog_ok);
			mListView = (ListView) mDialog.findViewById(R.id.dialog_listview);
			TextView tv = (TextView) mDialog.findViewById(R.id.dialog_title);
			tv.setText("口岸");
			// 设置点击事件
			mBtCancel.setOnClickListener(this);
			mBtOK.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					dismiss();
					mTvSeaport.setText(mSeaportDatas.get(selectedSeaportPosition).name);
				}
			});
			loadSeaportData();
		}
	}

	/** 加载港口数据 */
	private void loadSeaportData()
	{
		ToastUtils.showProgress(this);
		String url = URLS.BASESERVER + URLS.Driver.enum_;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, mDriver.access_token);
		params.put(URLS.TYPE, URLS.SEAPORT);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				processSeaportJson(json);
			}
		});
	}

	/** 处理港口 枚举接口返回数据 */
	protected void processSeaportJson(String json)
	{
		SearportBean searportBean = mGson.fromJson(json, SearportBean.class);
		if (isListEmpty(searportBean.content)) { return; }
		mSeaportDatas = searportBean.content;
		mListView.setAdapter(new DialogDriverSeaportAdapter(this, mSeaportDatas));
		/** 爲子條目设置点击事件 */
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				view.setSelected(true);
				selectedSeaportPosition = position;
			}
		});
		mDialog.show();
	}

	/** 车型等级对话框 */
	private void showLevelDialog()
	{

		if (mDialog == null)
		{
			mDialog = new Dialog(this, R.style.CustomBottomDialog);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setCancelable(false);
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
			mDialog.setContentView(R.layout.layout_dialog);
			mBtCancel = (Button) mDialog.findViewById(R.id.dialog_cancel);
			mBtOK = (Button) mDialog.findViewById(R.id.dialog_ok);
			mListView = (ListView) mDialog.findViewById(R.id.dialog_listview);
			TextView title = (TextView) mDialog.findViewById(R.id.dialog_title);
			title.setText("车级别");
			// 设置点击事件
			mBtCancel.setOnClickListener(this);
			mBtOK.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					dismiss();
					mTvLevel.setText(mLevelDatas.get(selectedLevelPosition).name);
				}
			});
			loadLevelData();
		}

	}

	/** 加載車型數據 */
	private void loadLevelData()
	{
		ToastUtils.showProgress(this);
		String url = URLS.BASESERVER + URLS.Driver.enum_;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, mDriver.access_token);
		params.put(URLS.TYPE, URLS.CARTYPE);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				processLevelJson(json);
			}
		});
	}

	protected void processLevelJson(String json)
	{
		CartTypeBean cartTypeBean = mGson.fromJson(json, CartTypeBean.class);
		if (!isListEmpty(cartTypeBean.content))
		{
			mLevelDatas = cartTypeBean.content;
			mListView.setAdapter(new DialogDriverLevelAdapter(this, mLevelDatas));
			/** 爲子條目设置点击事件 */
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					view.setSelected(true);
					selectedLevelPosition = position;
				}
			});
			mDialog.show();
		}

	}

	@Override
	public void onBackPressed()
	{
		if("3".equals(mDriver.status)){
			finishActivity();
		}else{
			startActivityAndFinish(DriverLoginUI.class);
		}
	}

	/**
	 * 隐藏对话框
	 */
	public void dismiss()
	{
		// 隐藏对话框之前先判断对话框是否存在，以及是否正在显示
		if (mDialog != null && mDialog.isShowing())
		{
			mDialog.dismiss();
			mDialog = null;
		}
	}

	/**
	 * 身份证拍照
	 * 
	 * @param iv
	 * @param photoName
	 *            照片名称
	 */
	public void setOnModifyIdCardIcon(ImageView iv, final String photoName)
	{
		mPopupWindow = PhotoUtilChange.getPicPopupWindow(this, new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// 点击按钮,框就消失
				if (mPopupWindow != null)
				{
					mPopupWindow.dismiss();
				}
				switch (v.getId())
				{
					case R.id.btn_cancel:
						break;
					case R.id.btn_take_photo:// 拍照
						Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), photoName)));
						startActivityForResult(intent2, 21);// 采用ForResult打开
						break;
					case R.id.btn_pick_photo: // 从相册取照片
						Intent intent1 = new Intent(Intent.ACTION_PICK, null);
						intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent1, 20);
						break;
					default:
						break;
				}
			}
		}, iv);

	}

	/**
	 * 准驾证号拍照
	 * 
	 * @param iv
	 * @param photoName
	 *            照片名称
	 */
	public void setOnModifyZjzhIcon(ImageView iv, final String photoName)
	{
		mPopupWindow = PhotoUtilChange.getPicPopupWindow(this, new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// 点击按钮,框就消失
				if (mPopupWindow != null)
				{
					mPopupWindow.dismiss();
				}
				switch (v.getId())
				{
					case R.id.btn_cancel:
						break;
					case R.id.btn_take_photo:// 拍照
						Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), photoName)));
						startActivityForResult(intent2, 11);// 采用ForResult打开
						break;
					case R.id.btn_pick_photo: // 从相册取照片
						Intent intent1 = new Intent(Intent.ACTION_PICK, null);
						intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent1, 10);
						break;
					default:
						break;
				}
			}
		}, iv);

	}

	/**
	 * 车牌照拍照
	 * 
	 * @param iv
	 * @param photoName
	 *            照片名称
	 */
	public void setOnModifyCarNumIcon(ImageView iv, final String photoName)
	{
		mPopupWindow = PhotoUtilChange.getPicPopupWindow(this, new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// 点击按钮,框就消失
				if (mPopupWindow != null)
				{
					mPopupWindow.dismiss();
				}
				switch (v.getId())
				{
					case R.id.btn_cancel:
						break;
					case R.id.btn_take_photo:// 拍照
						Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), photoName)));
						startActivityForResult(intent2, 2);// 采用ForResult打开
						break;
					case R.id.btn_pick_photo: // 从相册取照片
						Intent intent1 = new Intent(Intent.ACTION_PICK, null);
						intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent1, 1);
						break;
					default:
						break;
				}
			}
		}, iv);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		/* 车牌号 */
			case 1:
				if (resultCode == RESULT_OK) // 从相册取
				{
					cropPhoto(data.getData(), 3);// 裁剪图片
				}
				break;
			case 2:
				if (resultCode == RESULT_OK) // 拍照
				{
					File temp = new File(Environment.getExternalStorageDirectory() + "/carnum.jpg");
					cropPhoto(Uri.fromFile(temp), 3);// 裁剪图片
				}
				break;
			case 3: // 车牌号照片
				if (data != null)
				{
					Bundle extras = data.getExtras();
					carnum = extras.getParcelable("data");
					if (carnum != null)
					{
						setPicToView(carnum, "carnum.jpg");// 保存在SD卡中
						mIvCarNum.setImageBitmap(carnum);// 用ImageView显示出来
					}
				}
				break;

			/* 准驾证号 */
			case 10:
				if (resultCode == RESULT_OK) // 从相册取
				{
					cropPhoto(data.getData(), 12);// 裁剪图片
				}
				break;
			case 11:
				if (resultCode == RESULT_OK) // 拍照
				{
					File temp = new File(Environment.getExternalStorageDirectory() + "/zjzh.jpg");
					cropPhoto(Uri.fromFile(temp), 12);// 裁剪图片
				}
				break;
			case 12: // 准驾照片
				if (data != null)
				{
					Bundle extras = data.getExtras();
					zjzh = extras.getParcelable("data");
					if (zjzh != null)
					{
						setPicToView(zjzh, "zjzh.jpg");// 保存在SD卡中
						mIvZjzh.setImageBitmap(zjzh);// 用ImageView显示出来
					}
				}
				break;
			/* 身份证号 */
			case 20:
				if (resultCode == RESULT_OK) // 从相册取
				{
					cropPhoto(data.getData(), 22);// 裁剪图片
				}
				break;
			case 21:
				if (resultCode == RESULT_OK) // 拍照
				{
					File temp = new File(Environment.getExternalStorageDirectory() + "/idcard.jpg");
					cropPhoto(Uri.fromFile(temp), 22);// 裁剪图片
				}
				break;
			case 22: // 身份证照片
				if (data != null)
				{
					Bundle extras = data.getExtras();
					idcard = extras.getParcelable("data");
					if (idcard != null)
					{
						setPicToView(idcard, "idcard.jpg");// 保存在SD卡中
						mIvIdcard.setImageBitmap(idcard);// 用ImageView显示出来
					}
				}
				break;
			default:
				break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 调用系统的裁剪
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri, int requestCode)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, requestCode);
	}

	/** 设置图片 */
	private void setPicToView(Bitmap bitmap, String photName)
	{
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED))
		{ // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		String fileName = path + photName;// 图片名字
		try
		{
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				// 关闭流
				b.flush();
				b.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	}

}