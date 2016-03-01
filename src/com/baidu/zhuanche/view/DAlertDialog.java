package com.baidu.zhuanche.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.zhuanche.R;

public class DAlertDialog extends Dialog implements android.view.View.OnClickListener{

	private ImageView userImg;
	private TextView userName;
	private Button sure,canl;
	private Context context;
	private TextView textView;
	private Button cancelButton,confirmButton;
	private LinearLayout layout;
	private android.content.DialogInterface.OnClickListener cancelListener,confirmListener;
	private int message;
	private String messageStr;
	public DAlertDialog(Context context) {
		super(context, R.style.dialog_style_comment);
		this.context = context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_alterdialog);
		
		Window window = this.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		WindowManager m =  ((Activity)context).getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
		p.width = d.getWidth();
		window.setAttributes(p);
		
		this.initView();
		this.initListener();
	}
	private void initView() {
		layout = (LinearLayout) this.findViewById(R.id.layout);
		textView=(TextView)this.findViewById(R.id.alterdialog_text);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		cancelButton=(Button)this.findViewById(R.id.alterdialog_button_cancel);
		confirmButton=(Button)this.findViewById(R.id.alterdialog_button_confirm);
		
		if(message!=0){
			textView.setText(message);
		}else if (messageStr!=null) {
			textView.setText(messageStr);
		}
	}
	private void initListener() {
		cancelButton.setOnClickListener(this);
		confirmButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v==confirmButton){
			if(confirmListener!=null){
				confirmListener.onClick(this, 1);
			}
			this.cancel();
		}else if(v==cancelButton){
			if(cancelListener!=null){
				cancelListener.onClick(this, 0);
			}
			this.cancel();
		}
	}
	public void setMessage(int message){
		this.message=message;
		if(textView!=null){
			textView.setText(message);
		}
	}
	public void setMessage(String message){
		this.messageStr=message;
		if(textView!=null){
			textView.setText(message);
		}
	}
	
	private ProgressBar bar;
	//private WeixinUserBean bean;
	private ImageView userImgs;

	// public void addChildView(int viewID,WeixinUserBean object){
	// this.bean = object;
	// if ((viewID+"").length()!=0) {
	// if (layout!=null) {
	// String filename = "/temp_wx.jpg";
	// View view = LayoutInflater.from(context).inflate(viewID, null);
	// layout.addView(view);
	// bar = (ProgressBar) view.findViewById(R.id.progressBar1);
	// bar.setVisibility(View.VISIBLE);
	// userImgs = (ImageView) view.findViewById(R.id.userimage);
	// TextView userName = (TextView) view.findViewById(R.id.username);
	// userName.setText(bean.getNickname());
	// textView.setVisibility(View.GONE);
	// confirmButton.setText("授权登录");
	// cancelButton.setText("撤  消");
	// confirmButton.setEnabled(false);
	// new ImageSave().saveImageByURL(object.getHeadimgurl(), filename,handler);
	// }
	// }
	// }
	
	public void addCancelListener(
			android.content.DialogInterface.OnClickListener cancelListener) {
		this.cancelListener = cancelListener;
	}
	
	public void addConfirmListener(
			android.content.DialogInterface.OnClickListener confirmListener) {
		this.confirmListener = confirmListener;
	}
	
	
}
