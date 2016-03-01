package com.baidu.zhuanche.ui.driver;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.zhuanche.R;
import com.baidu.zhuanche.adapter.FragAdapter;
import com.baidu.zhuanche.fragment.AllOrderFragment;
import com.baidu.zhuanche.fragment.AssessedOrderFragment;
import com.baidu.zhuanche.fragment.GoingOrderFragment;
import com.baidu.zhuanche.fragment.PayedOrderFragment;
import com.baidu.zhuanche.view.NoScrollViewPager;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.ui.driver
 * @类名: DriverAllOrderUI
 * @创建者: 陈选文
 * @创建时间: 2016-1-7 上午10:38:11
 * @描述: 司机全部订单
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DriverAllOrderUI extends FragmentActivity implements OnClickListener
{
	private TextView			mContainerAll;
	private TextView			mContainerGoing;
	private TextView			mContainerPayed;
	private TextView			mContainerAssessed;
	private ImageView			mIvLeftMenu;
	private TextView			mTvTitle;
	private NoScrollViewPager	mViewPager;
	private List<Fragment>		mFragmentDatas;
	private FragAdapter			mAdapter;

	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		initView();
		initData();
		initListener();
	}

	private void initListener()
	{
		mIvLeftMenu.setOnClickListener(this);
		mContainerAll.setOnClickListener(this);
		mContainerGoing.setOnClickListener(this);
		mContainerPayed.setOnClickListener(this);
		mContainerAssessed.setOnClickListener(this);
	}

	private void initData()
	{
		mTvTitle.setText("全部訂單");
		mViewPager.setNoScroll(true);
		/*
		 * 添加fragment数据
		 */
		mFragmentDatas = new ArrayList<Fragment>();
		mFragmentDatas.add(new AllOrderFragment());
		mFragmentDatas.add(new GoingOrderFragment());
		mFragmentDatas.add(new PayedOrderFragment());
		mFragmentDatas.add(new AssessedOrderFragment());
		mAdapter = new FragAdapter(getSupportFragmentManager(), mFragmentDatas);
		mViewPager.setAdapter(mAdapter);
		switchMenu(0);
	}

	public void initView()
	{
		setContentView(R.layout.ui_driver_allorder);
		mContainerAll = (TextView) findViewById(R.id.allorder_tv_all);
		mContainerGoing = (TextView) findViewById(R.id.allorder_tv_going);
		mContainerPayed = (TextView) findViewById(R.id.allorder_tv_payed);
		mContainerAssessed = (TextView) findViewById(R.id.allorder_tv_assesed);
		mTvTitle = (TextView) findViewById(R.id.header_title);
		mIvLeftMenu = (ImageView) findViewById(R.id.header_leftMenu);
		mViewPager = (NoScrollViewPager) findViewById(R.id.allorder_viewpager);
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvLeftMenu)
		{
			finishAcitity();
		}
		else if (v == mContainerAll)
		{
			switchMenu(0);
		}
		else if (v == mContainerGoing)
		{
			switchMenu(1);
		}
		else if (v == mContainerPayed)
		{
			switchMenu(2);
		}
		else if (v == mContainerAssessed)
		{
			switchMenu(3);
		}
		
	}

	private void finishAcitity()
	{
		finish();
		overridePendingTransition(R.anim.pre_enter, R.anim.pre_exit);
	}

	@Override
	public void onBackPressed()
	{
		finishAcitity();
	}

	private void switchMenu(int index)
	{

		mContainerAll.setSelected(false);
		mContainerAssessed.setSelected(false);
		mContainerGoing.setSelected(false);
		mContainerPayed.setSelected(false);
		mViewPager.setCurrentItem(index);
		switch (index)
		{
			case 0:
				mContainerAll.setSelected(true);
				break;
			case 1:
				mContainerGoing.setSelected(true);
				break;
			case 2:
				mContainerPayed.setSelected(true);
				break;
			case 3:
				mContainerAssessed.setSelected(true);
				break;
			default:
				break;
		}
	}
}
