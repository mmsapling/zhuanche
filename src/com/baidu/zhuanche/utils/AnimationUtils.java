package com.baidu.zhuanche.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

public class AnimationUtils {
	public static void showAlpha(View v) {
		v.setVisibility(0);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(200);
		v.startAnimation(alphaAnimation);
	}

	public static void hideAlpha(View v) {
		v.setVisibility(8);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(200);
		v.startAnimation(alphaAnimation);
	}
}
