package com.baidu.zhuanche.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.base.BaseActivity;
import com.baidu.zhuanche.ui.user.UserHomeUI;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui
 * @类名: MainActivity
 * @创建者: 陈选文
 * @创建时间: 2016-1-27 上午10:21:27
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class MainActivity extends BaseActivity implements OnPageChangeListener
{
	private LinearLayout	mContainerPoint;
	private ViewPager		mViewPager;
	private Button			mBtEnter;
	private int[]			icons	= new int[] { R.drawable.appbegin01, R.drawable.appbegin02 };
	private List<ImageView>	mDatas;
	private boolean			mIsshow;

	@Override
	public void init()
	{
		super.init();
		mIsshow = mSpUtils.getBoolean("isshow", false);
		if(mIsshow){
			startActivityAndFinish(UserHomeUI.class);
		}
	}

	public void initData()
	{
		mDatas = new ArrayList<ImageView>();
		for(int i = 0 ; i < icons.length; i++){
			ImageView view = new ImageView(this);
			view.setImageResource(icons[i]);
			view.setScaleType(ScaleType.FIT_XY);
			mDatas.add(view);
		}
		mViewPager.setAdapter(new PointAdapter());
		mViewPager.setOnPageChangeListener(this);
		mBtEnter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				mSpUtils.putBoolean("isshow", true);
				startActivityAndFinish(UserHomeUI.class);
			}
		});
	}

	class PointAdapter extends PagerAdapter
	{

		@Override
		public int getCount()
		{
			if (mDatas != null) { return mDatas.size(); }
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
			ImageView view = mDatas.get(position);
			container.addView(view);
			return view;
		}

	}

	public void initView()
	{
		setContentView(R.layout.activity_mabi);
		mViewPager = (ViewPager) findViewById(R.id.gainima_viewpager);
		mBtEnter = (Button) findViewById(R.id.gainima_bt_enter);
	//	mContainerPoint = (LinearLayout) findViewById(R.id.container_point);
	}

	@Override
	public void onPageScrollStateChanged(int position)
	{
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{

	}

	@Override
	public void onPageSelected(int position)
	{
		if (position == mDatas.size() - 1)
		{
			mBtEnter.setVisibility(0);
		}
		else
		{
			mBtEnter.setVisibility(8);
		}
	}
}
