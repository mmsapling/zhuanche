package com.baidu.zhuanche.holder;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseHolder;
import com.baidu.zhuanche.bean.UserIndexBean.Banner;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.utils.ImageViewHelper;
import com.baidu.zhuanche.utils.UIUtils;
import com.baidu.zhuanche.view.InnerViewPager;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.holder
 * @类名: UserHomePicHolder
 * @创建者: 陈选文
 * @创建时间: 2015-12-28 下午2:10:00
 * @描述: 首页轮播图
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class UserHomePicHolder extends BaseHolder<List<Banner>> implements OnTouchListener
{
	private InnerViewPager	mViewPager;
	private LinearLayout	mContainerPoint;
	private AutoScrollTask	mAutoScrollTask;

	@Override
	protected View initHolderView()
	{
		View view = View.inflate(UIUtils.getContext(), R.layout.holder_uh_pic, null);
		mViewPager = (InnerViewPager) view.findViewById(R.id.holder_userhome_viewpager);
		mContainerPoint = (LinearLayout) view.findViewById(R.id.holder_userhome_container_indicator);
		return view;
	}

	@Override
	protected void refreshHolderView(List<Banner> data)
	{
		mViewPager.setAdapter(new PicturesPagerAdapter());
		// 添加indicatior
		for (int i = 0; i < mData.size(); i++)
		{
			ImageView ivIndicator = new ImageView(UIUtils.getContext());
			ivIndicator.setImageResource(R.drawable.indicator_normal);
			if (i == 0)
			{
				ivIndicator.setImageResource(R.drawable.indicator_selected);
			}
			int width = UIUtils.dip2Px(8);
			int height = UIUtils.dip2Px(8);
			LayoutParams params = new LayoutParams(width, height);
			params.leftMargin = UIUtils.dip2Px(6);
			params.bottomMargin = UIUtils.dip2Px(6);
			mContainerPoint.addView(ivIndicator, params);
		}
		// 滑动时切换indicator
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position)
			{
				position = position % mData.size();
				// 1.还原所有的效果
				for (int i = 0; i < mData.size(); i++)
				{
					ImageView ivIndicator = (ImageView) mContainerPoint.getChildAt(i);
					ivIndicator.setImageResource(R.drawable.indicator_normal);
				}
				// 设置选中的效果
				ImageView ivIndicator = (ImageView) mContainerPoint.getChildAt(position);
				ivIndicator.setImageResource(R.drawable.indicator_selected);
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{

			}
		});
		// 左右无限轮播
		int index = Integer.MAX_VALUE / 2;
		int diffx = Integer.MAX_VALUE / 2 % mData.size();
		index = index - diffx;
		mViewPager.setCurrentItem(index);
		mAutoScrollTask = new AutoScrollTask();
		mAutoScrollTask.start();
		// 按下去停止轮播
		mViewPager.setOnTouchListener(this);
	}

	class AutoScrollTask implements Runnable
	{                                                                      
		public void start()
		{
			UIUtils.getMainThreadHandler().removeCallbacks(this);
			UIUtils.getMainThreadHandler().postDelayed(this, 2000);
		}

		public void stop()
		{
			UIUtils.getMainThreadHandler().removeCallbacks(this);
		}

		@Override
		public void run()
		{
			int currentItem = mViewPager.getCurrentItem();
			currentItem++;
			mViewPager.setCurrentItem(currentItem);
			start();
		}
	}

	class PicturesPagerAdapter extends PagerAdapter
	{

		@Override
		public int getCount()
		{
			if (mData != null) { return Integer.MAX_VALUE; }
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			position = position % mData.size();
			ImageView iv = new ImageView(UIUtils.getContext());
			iv.setScaleType(ScaleType.FIT_XY);
			Banner banner = mData.get(position);
			String url = URLS.BASE + banner.img;
			ImageViewHelper.display(iv, url);
			container.addView(iv);
			return iv;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				mAutoScrollTask.stop();
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				mAutoScrollTask.start();
				break;
			default:
				break;
		}
		return false;
	}
}
