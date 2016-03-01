package com.baidu.zhuanche.ui.driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.DialogSexAdapter;
import com.baidu.zhuanche.adapter.QuhaoAdapter;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.bean.Sex;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.AtoolsUtil;
import com.baidu.zhuanche.utils.JsonUtils;
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
 * @类名: CompleteUI
 * @创建者: 陈选文
 * @创建时间: 2016-1-11 下午4:03:10
 * @描述: 完善資料
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class CompleteUI extends BaseActivity implements OnClickListener
{
	private Bitmap					head;

	// 头像Bitmap
	private static String			path				= "/sdcard/myHead/";	// sd路径
	private String					mHeadName			= "head1.jpg";
	private CircleImageView			mCivPic;
	private TextView				mTvMobile;
	private EditText				mEtName;
	private RelativeLayout			mContainerSex;
	private TextView				mTvQuhao;
	private EditText				mEtBackupMobile;
	private Button					mBtConfirm;
	private LinearLayout			mContainerQuhao;
	private Dialog					mDialog;

	// 底部对话框
	private Button					mBtOK;
	private Button					mBtCancel;
	private ListView				mListView;
	private List<Sex>				mDatas;
	private DialogSexAdapter		mAdapter;
	private int						selectedSexPosition	= 0;
	private TextView				mTvSex;
	private List<String>			mQuhaoDatas;
	private QuhaoAdapter			mQuhaoAdapter;
	private SelectPicPopupWindow	mPopupWindow;
	private TextView				mTvMainQuhao;
	private Driver					mDriver;
	private TextView				mTvNumSb;
	private TextView				mTvQuhaoSb;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_complete);
		mCivPic = (CircleImageView) findViewById(R.id.complete_civ_photo);
		mTvMobile = (TextView) findViewById(R.id.complete_tv_mobile);
		mEtName = (EditText) findViewById(R.id.complete_et_name);
		mContainerSex = (RelativeLayout) findViewById(R.id.complete_container_sex);
		mBtConfirm = (Button) findViewById(R.id.complete_bt_ok);
		mEtBackupMobile = (EditText) findViewById(R.id.complete_et_backupmobile);
		mTvQuhao = (TextView) findViewById(R.id.complete_tv_quhao);
		mContainerQuhao = (LinearLayout) findViewById(R.id.complete_container_quhao);
		mTvSex = (TextView) findViewById(R.id.complet_tv_sex);
		mTvMainQuhao = (TextView) findViewById(R.id.complete_tv_mainquhao);
		mTvQuhaoSb = (TextView) findViewById(R.id.applycash_tv_title_carlevelsb);
		mTvNumSb = (TextView) findViewById(R.id.complet_tv_sexsb);
	}

	@Override
	public void initData()
	{
		super.initData();
		mTvTitle.setText("完善資料");
		mDriver = BaseApplication.getDriver();
		enable(false);
		mTvMainQuhao.setText(TextUtils.isEmpty(mDriver.area) ? "+86" : "+" +

																		mDriver.area);
		mTvMobile.setText(mDriver.mobile);
		// mTvQuhaoSb.setText(TextUtils.isEmpty(mDriver.area) ? "+86" : "+" +
		//
		// mDriver.area);
		mTvNumSb.setText(mDriver.area + "  " + mDriver.mobile);
		ToastUtils.showProgress(this);
		String url = URLS.BASESERVER + URLS.Driver.getModify;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, mDriver.access_token);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				try
				{
					processDefaultJson(json);
				}
				catch (JSONException e)
				{
					PrintUtils.print("json解析異常！");
					e.printStackTrace();
				}
			}
		});
	}

	private void enable(boolean enabled)
	{
		mCivPic.setEnabled(enabled);
		mEtName.setEnabled(enabled);
		mTvSex.setEnabled(enabled);
		mContainerQuhao.setEnabled(enabled);
		mContainerSex.setEnabled(enabled);
		mEtBackupMobile.setEnabled(enabled);
		mBtConfirm.setText(enabled ? "確定" : "編輯");
	}

	protected void processDefaultJson(String json) throws JSONException
	{
		JSONObject content = JsonUtils.getContent(json);
		String icon = content.getString("icon");
		String gender = content.getString("gender");
		String area1 = content.getString("area1");
		String mobile1 = content.getString("mobile1");
		String username = content.getString("username");
		mImageUtils.display(mCivPic, URLS.BASE + icon);
		mEtName.setText(username);
		mTvSex.setText(gender);
		mTvQuhao.setText("+" + area1);
		mEtBackupMobile.setText(mobile1);
		mTvSex.setText(gender);
		mDriver.icon = icon;
		mDriver.username = username;
		mDriver.gender = AtoolsUtil.getGenderToNumber(gender);
		BaseApplication.setDriver(mDriver);
	}

	@Override
	public void initListener()
	{
		mIvLeftHeader.setOnClickListener(this);
		mContainerQuhao.setOnClickListener(this);
		mContainerSex.setOnClickListener(this);
		mTvSex.setOnClickListener(this);
		mCivPic.setOnClickListener(this);
		mBtConfirm.setOnClickListener(this);
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
		}
		else if (v == mContainerSex)
		{
			showSexDialog();
		}
		else if (v == mContainerQuhao)
		{
			doClickQuhao();
		}
		else if (v == mBtCancel)
		{
			dismiss();
		}
		else if (v == mCivPic)
		{
			setOnModifyIcon(mCivPic, mHeadName);
		}
		else if (v == mBtConfirm)
		{
			try
			{
				if (mBtConfirm.getText().equals("確定"))
				{
					confirm();
				}
				else
				{
					enable(true);
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		else if (v == mTvSex)
		{
			showSexDialog();
		}
	}

	private void confirm() throws FileNotFoundException
	{
		/* 判段值不能为空 */
		final String username = mEtName.getText().toString().trim();
		String gender = selectedSexPosition + "";
		String area1 = mTvQuhao.getText().toString().replace("+", "");
		String mobile1 = mEtBackupMobile.getText().toString().trim();
		boolean b = TextUtils.isEmpty(username)
					|| TextUtils.isEmpty(gender) || TextUtils.isEmpty

(area1) || TextUtils.isEmpty(mobile1);
		if( TextUtils.isEmpty(mobile1)){
			ToastUtils.makeShortText(this, "請輸入備用手機號碼！");
			return;
		}
		if(TextUtils.isEmpty(area1)){
			ToastUtils.makeShortText(this, "請選擇備用區號！");
			return;
		}
		if(TextUtils.isEmpty(gender)){
			ToastUtils.makeShortText(this, "請選擇性別！");
			return;
		}
		if (b)
		{
			ToastUtils.makeShortText(this, "請完善資料！");
			return;
		}
		/* 爲bitmap創建文件 */
		File dirFile = new File(Environment.getExternalStorageDirectory(), "/myHead/");
		File headFile = new File(dirFile, mHeadName);
		if(!headFile.exists()){
			ToastUtils.makeShortText(this, "請重新上傳圖片！");
			return;
		}
		String url = URLS.BASESERVER + URLS.Driver.modifyDriver;
		RequestParams params = new RequestParams();
		params.put(URLS.ACCESS_TOKEN, mDriver.access_token);
		params.put("username", username);
		params.put("gender", gender);
		params.put("area1", area1);
		params.put("mobile1", mobile1);
		params.put("icon", headFile);
		mClient.post(url, params, new MyAsyncResponseHandler() {

			@Override
			public void success(String json)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "修改成功！");
				if (mListener != null)
				{
					if(head != null){
						mListener.onModify(head,username);
					}
				}
				finishActivity();
			}
		});
	}

	public static OnModifyListener	mListener;

	public static void setOnModifyListener(OnModifyListener listener)
	{
		mListener = listener;
	}

	/**
	 * 修改成功之後的接口回調
	 */
	public interface OnModifyListener
	{
		void onModify(Bitmap bitmap, String name);
	}

	/** 区号选择 */
	private void doClickQuhao()
	{
		AlertDialog.Builder builder = new Builder(this);
		mQuhaoDatas = new ArrayList<String>();
		mQuhaoDatas.add("+86");
		mQuhaoDatas.add("+852");
		mQuhaoAdapter = new QuhaoAdapter(this, mQuhaoDatas);
		builder.setAdapter(mQuhaoAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String quhao = (String) mQuhaoAdapter.getItem(which);
				mTvQuhao.setText(quhao);
			}
		});
		builder.show();
	}

	private void showSexDialog()
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
			TextView tvTitle = (TextView) mDialog.findViewById(R.id.dialog_title);
			tvTitle.setText("性别");
			mBtCancel = (Button) mDialog.findViewById(R.id.dialog_cancel);
			mBtOK = (Button) mDialog.findViewById(R.id.dialog_ok);
			mListView = (ListView) mDialog.findViewById(R.id.dialog_listview);
			// 设置点击事件
			mBtCancel.setOnClickListener(this);
			mBtOK.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					if (selectedSexPosition != 0)
					{
						mTvSex.setText(mDatas.get(selectedSexPosition).name);
					}
					dismiss();
				}
			});
			loadData();
		}
	}

	private void loadData()
	{
		mDatas = new ArrayList<Sex>();
		mDatas.add(new Sex("未知", "0"));
		mDatas.add(new Sex("男", "1"));
		mDatas.add(new Sex("女", "2"));
		mAdapter = new DialogSexAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);
		/** 爲子條目设置点击事件 */
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int

									position, long id)
			{
				view.setSelected(true);
				selectedSexPosition = position;
			}
		});
		mDialog.show();
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

	public void setOnModifyIcon(CircleImageView iv, final String photoName)
	{
		mPopupWindow = PhotoUtilChange.getPicPopupWindow(this, new OnClickListener

() {

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
						startActivityForResult(intent2, 21);// 采用

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{

			case 20:
				if (resultCode == RESULT_OK) // 从相册取
				{
					cropPhoto(data.getData(), 22);// 裁剪图片
				}
				break;
			case 21:
				if (resultCode == RESULT_OK) // 拍照
				{
					File temp = new File

(Environment.getExternalStorageDirectory() + "/" + mHeadName);
					cropPhoto(Uri.fromFile(temp), 22);// 裁剪图片
				}
				break;
			case 22: // 身份证照片
				if (data != null)
				{
					Bundle extras = data.getExtras();
					head = extras.getParcelable("data");
					if (head != null)
					{
						setPicToView(head, mHeadName);// 保存在SD卡

						mCivPic.setImageBitmap(head);// 用ImageView

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
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文

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