package com.baidu.zhuanche.utils;

import android.view.View;
import android.widget.ImageView;

/**
 * @创建者	 Administrator
 * @创时间 	 2015-11-13 下午2:40:18
 * @描述	     封装图片的加载
 *
 * @版本       $Rev: 21 $
 * @更新者     $Author: admin $
 * @更新时间    $Date: 2015-11-13 14:45:25 +0800 (星期五, 13 十一月 2015) $
 * @更新描述    TODO
 */
public class ImageViewHelper {
	static ImageUtils	mBitmapUtils;
	static {
		mBitmapUtils = new ImageUtils(UIUtils.getContext());
	}

	public static <T extends View> void display(ImageView container, String uri) {
		mBitmapUtils.display(container, uri);
	}
}
