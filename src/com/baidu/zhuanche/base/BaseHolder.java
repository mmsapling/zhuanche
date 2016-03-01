package com.baidu.zhuanche.base;

import android.view.View;

/**
 * @param <HOLDERBEANTYPE>
 * @创建者 陈选文
 * @创时间 2015-11-13 上午10:43:58
 * @描述 1.提供视图
 * @描述 2.接收数据
 * @描述 3.绑定数据/刷新ui
 * @描述 4.作用:把代码模块化.减少冗余
 * 
 * @版本 $Rev: 18 $
 * @更新者 $Author: admin $
 * @更新时间 $Date: 2015-11-13 10:56:26 +0800 (星期五, 13 十一月 2015) $
 * @更新描述 TODO
 */
public abstract class BaseHolder<HOLDERBEANTYPE>
{

	// 可以提供的视图
	public View				mHolderView;	// -->view
	public HOLDERBEANTYPE	mData;			// -->model

	// 1.初始化holder
	public BaseHolder() {
		mHolderView = initHolderView();
		// 4.mHolderView去找一个类,作为它的tag,绑定在自己身上,方便以后进行复用
		mHolderView.setTag(this);
	}

	/**
	 * @des 初始化持有的视图
	 * @call 走到构造方法自动被调用
	 * @return
	 */
	protected abstract View initHolderView();

	/**
	 * @des 接收数据
	 * @des 绑定数据
	 * @des 必须实现,但是不知道具体实现,定义成为抽象方法,交给子类具体实现
	 * @call 外界刷新的时候
	 */
	public void setDataAndRefreshHolderView(HOLDERBEANTYPE data)
	{
		// 保存数据到成员变量
		mData = data;
		// 绑定数据
		refreshHolderView(data);
	}

	/**
	 * @des 绑定数据
	 * @des 必须实现,但是不知道具体实现,定义成为抽象方法,交给子类具体实现
	 * @call 外界调用了setDataAndRefreshHolderView()的时候
	 * @param data
	 */
	protected abstract void refreshHolderView(HOLDERBEANTYPE data);

}
