package com.baidu.zhuanche.ui.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.bean.UserBean;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.holder.InfoMobileHolder;
import com.baidu.zhuanche.holder.InfoNameHolder;
import com.baidu.zhuanche.holder.InfoPicHolder;
import com.baidu.zhuanche.holder.InfoPicHolder.OnModifyIconListener;
import com.baidu.zhuanche.holder.InfoSexHolder;
import com.baidu.zhuanche.holder.InfoSignHolder;
import com.baidu.zhuanche.listener.MyAsyncResponseHandler;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.FileUtils;
import com.baidu.zhuanche.utils.JsonUtils;
import com.baidu.zhuanche.utils.PhotoUtilChange;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.view.CircleImageView;
import com.baidu.zhuanche.view.SelectPicPopupWindow;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserInfoUI extends BaseActivity implements OnClickListener, OnModifyIconListener
{
	private Bitmap					head;							// 头像Bitmap
	private static String			path	= "/sdcard/myHead/";	// sd路径
	private SelectPicPopupWindow	mPopupWindow;
	private CircleImageView			mCiv;

	private FrameLayout				mContainerPic;
	private FrameLayout				mContainerSex;
	private FrameLayout				mContainerName;
	private FrameLayout				mContainerMobile;
	private FrameLayout				mContainerSign;
	private User					mUser;

	@Override
	public void initView()
	{
		setContentView(R.layout.ui_user_info);
		mContainerMobile = (FrameLayout) findViewById(R.id.info_fl_mobile);
		mContainerName = (FrameLayout) findViewById(R.id.info_fl_username);
		mContainerSign = (FrameLayout) findViewById(R.id.info_fl_sign);
		mContainerPic = (FrameLayout) findViewById(R.id.info_fl_pic);
		mContainerSex = (FrameLayout) findViewById(R.id.info_fl_sex);
		mUser = BaseApplication.getUser();
		/** 头像容器 */
		 InfoPicHolder picHolder = new InfoPicHolder(this);
		 mContainerPic.addView(picHolder.mHolderView);
		 picHolder.setDataAndRefreshHolderView(mUser);
		/** 昵称容器 */
		 InfoNameHolder nameHolder = new InfoNameHolder();
		 mContainerName.addView(nameHolder.mHolderView);
		 nameHolder.setDataAndRefreshHolderView(mUser);
		/** 性別容器 */
		 InfoSexHolder sexHolder = new InfoSexHolder();
		 mContainerSex.addView(sexHolder.mHolderView);
		 sexHolder.setDataAndRefreshHolderView(mUser);
		/** 手机容器 */
		InfoMobileHolder mobileHolder = new InfoMobileHolder();
		mContainerMobile.addView(mobileHolder.mHolderView);
		mobileHolder.setDataAndRefreshHolderView(mUser);
		/** 签名 */
		 InfoSignHolder signHolder = new InfoSignHolder();
		 mContainerSign.addView(signHolder.mHolderView);
		 signHolder.setDataAndRefreshHolderView(mUser);
	}

	@Override
	public void initListener()
	{
		super.initListener();
		mContainerPic.setOnClickListener(this);
		mIvLeftHeader.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if(v == mIvLeftHeader){
			reLogin();
		}
	}
	@Override
	public void onBackPressed()
	{
		reLogin();
		
	}
	public void reLogin(){
		String url = URLS.BASESERVER + URLS.User.login;
		RequestParams params = new RequestParams();
		params.add("mobile", BaseApplication.getUser().mobile);
		params.add("password", BaseApplication.getUser().password);
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance(UIUtils.getContext());
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				String json = new String(arg2);
				Gson gson = new Gson();
				UserBean userBean = gson.fromJson(json, UserBean.class);
				// 保存全局用戶信息
				String password = BaseApplication.getUser().password;
				if (userBean.content != null)
				{
					User user = userBean.content.member_data;
					if (user != null && password != null)
					{
						user.password = password;
						user.access_token = userBean.content.access_token;
						BaseApplication.setUser(user);
						PrintUtils.println("用户连接成功！");
						startActivity(UserCenterUI.class);
					}
				}

			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
			}
		});
	}
	@Override
	public void setOnModifyIcon(CircleImageView civ)
	{
		mCiv = civ;
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
						intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
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
		}, mCiv);
	}

	/**
	 * 上传给服务器
	 * 
	 * @param zoomBitMap
	 */
	private void upload(Bitmap zoomBitMap)
	{
		mUser = BaseApplication.getUser();
		String url = URLS.BASESERVER + URLS.User.modifyIcon;
		try
		{
			String iconDir = FileUtils.getIconDir();
			File file = new File(iconDir, "icon.jpg");
			FileOutputStream fos = new FileOutputStream(file);
			zoomBitMap.compress(CompressFormat.JPEG, 100, fos);

			AsyncHttpClient client = AsyncHttpClientUtil.getInstance();
			RequestParams params = new RequestParams();
			params.put(URLS.MOBILE, mUser.mobile);
			params.put(URLS.ACCESS_TOKEN, mUser.access_token);
			params.put(URLS.ICON, file);

			client.post(url, params, new MyAsyncResponseHandler() {

				@Override
				public void success(String json)
				{
					processJson(json);
				}
			});
		}
		catch (FileNotFoundException e2)
		{
		}
	}

	private void processJson(String json)
	{
		try
		{
			JSONObject content = JsonUtils.getContent(json);
			String iconUrl = content.getString("icon");
			// 将新的图片地址赋值给user
			mUser.icon = iconUrl;
			BaseApplication.setUser(mUser);
			ToastUtils.makeShortText(UIUtils.getContext(), "上传成功！");
			//TODO
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case 1:
				if (resultCode == RESULT_OK)
				{
					cropPhoto(data.getData());// 裁剪图片
				}

				break;
			case 2:
				if (resultCode == RESULT_OK)
				{
					File temp = new File(Environment.getExternalStorageDirectory()
											+ "/head.jpg");
					cropPhoto(Uri.fromFile(temp));// 裁剪图片
				}

				break;
			case 3:
				if (data != null)
				{
					Bundle extras = data.getExtras();
					head = extras.getParcelable("data");
					if (head != null)
					{
						/**
						 * 上传服务器代码
						 */
						upload(head);
						setPicToView(head);// 保存在SD卡中
						mCiv.setImageBitmap(head);// 用ImageView显示出来
					}
				}
				break;
			default:
				break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	};

	/**
	 * 调用系统的裁剪
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri)
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
		startActivityForResult(intent, 3);
	}

	private void setPicToView(Bitmap mBitmap)
	{
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED))
		{ // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		String fileName = path + "head.jpg";// 图片名字
		try
		{
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

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
