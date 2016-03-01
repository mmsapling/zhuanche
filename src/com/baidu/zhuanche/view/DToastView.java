package com.baidu.zhuanche.view;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.zhuanche.R;

public class DToastView {

	public static Toast makeText(Activity activity,int text,int duration){
		View view=activity.getLayoutInflater().inflate(R.layout.view_toast, null);
		TextView textView=(TextView)view.findViewById(R.id.toast_text);
		textView.setText(text);
		Toast toast = new Toast(activity);
        toast.setDuration(duration);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
	}
	public static Toast makeText(Activity activity,String text,int duration){
		View view=activity.getLayoutInflater().inflate(R.layout.view_toast, null);
		TextView textView=(TextView)view.findViewById(R.id.toast_text);
		textView.setText(text);
		Toast toast = new Toast(activity);
        toast.setDuration(duration);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
	}
}
