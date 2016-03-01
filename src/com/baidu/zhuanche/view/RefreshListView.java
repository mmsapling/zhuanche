package com.baidu.zhuanche.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.zhuanche.R;

/**
 * @项目名: Zhbj12
 * @包名: org.itheima12.zhbj.view
 * @类名: RefreshListView
 * @创建者: 肖琦
 * 
 * @描述: 下拉刷新的view
 * 
 * @版本号: $Rev: 52 $
 * @更新人: $Author: xq $
 * @更新时间: $Date: 2015-09-28 08:40:16 +0800 (Mon, 28 Sep 2015) $
 * 
 * @更新内容: TODO
 */
public class RefreshListView extends ListView implements OnScrollListener
{

	private static final String	TAG						= "RefreshListView";

	private static final int	STATE_PULL_REFRESH		= 0;					// 下拉刷新状态
	private static final int	STATE_RELEASE_REFRESH	= 1;					// 松开刷新状态
	private static final int	STATE_REFRESHING		= 2;					// 正在刷新状态

	private LinearLayout		mRefreshLayout;								// 刷新的头
	private int					mDownX;
	private int					mDownY					= -1;
	private int					mRefreshHeaderHeight;

	private int					mCurrentState			= STATE_PULL_REFRESH;	// 当前的状态

	private TextView			mTvRefreshState;
	private TextView			mTvRefreshDate;
	private ProgressBar			mRefreshProgress;
	private ImageView			mIvArrow;

	private OnRefreshListener	mListener;										// 下拉刷新的监听器

	private View				mFirstView;

	private View				mFooterLayout;

	private int					mFooterHeight;

	private boolean				isLoadMore				= false;				// 默认不是加载更多

	private int					space					= -1;
	private boolean				isInterceptTouch		= true;

	public RefreshListView(Context context) {
		this(context, null);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 1. 添加刷新的头
		initRefreshHeader();// measure()--》getMeasuredWidth --->layout()
							// ---->getWidth();
		// onMeasure()

		// 2. 添加加载更多的底部布局
		//initFooter(); TODO
	}

	private void initRefreshHeader()
	{
		mRefreshLayout = (LinearLayout) View.inflate(getContext(), R.layout.refresh_header, null);

		// 查找view
		mTvRefreshState = (TextView) mRefreshLayout.findViewById(R.id.refresh_header_tv_state);
		mTvRefreshDate = (TextView) mRefreshLayout.findViewById(R.id.refresh_header_tv_date);
		mIvArrow = (ImageView) mRefreshLayout.findViewById(R.id.refresh_header_iv_arrow);
		mRefreshProgress = (ProgressBar) mRefreshLayout.findViewById(R.id.refresh_header_progress);

		// 添加刷新的头
		addHeaderView(mRefreshLayout);

		// 隐藏刷新的头.设置paddingTop为自己高度的负数
		mRefreshLayout.measure(0, 0);// 安装自己大小测量
		mRefreshHeaderHeight = mRefreshLayout.getMeasuredHeight();

		Log.d(TAG, "refreshHeaderHeight : " + mRefreshHeaderHeight);

		int paddingTop = -mRefreshHeaderHeight;
		mRefreshLayout.setPadding(0, paddingTop, 0, 0);
	}

	private void initFooter()
	{
//		mFooterLayout = View.inflate(getContext(), R.layout.refresh_footer, null);
//		this.addFooterView(mFooterLayout);
//
//		// 隐藏footer
//		mFooterLayout.measure(0, 0);
//		mFooterHeight = mFooterLayout.getMeasuredHeight();
//		mFooterLayout.setPadding(0, -mFooterHeight, 0, 0);

		// 监听listView的滑动
		this.setOnScrollListener(this);
	}

	@Override
	public void addHeaderView(View v)
	{
		if (mFirstView == null && v != mRefreshLayout)
		{
			mFirstView = v;
		}

		super.addHeaderView(v);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{

		switch (ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:

				mDownX = (int) (ev.getX() + 0.5f);
				mDownY = (int) (ev.getY() + 0.5f);

				Log.d(TAG, "downY : " + mDownY);
				break;
			case MotionEvent.ACTION_MOVE:
				int moveX = (int) (ev.getX() + 0.5f);
				int moveY = (int) (ev.getY() + 0.5f);

				if (mDownY == -1)
				{
					mDownY = moveY;
				}

				// 如果用户是由上往下拖动,改变刷新部分的paddingTop的值
				// 由上往下:moveY > mDownY
				int diffY = moveY - mDownY;

				// 如果当前是正在刷新
				if (mCurrentState == STATE_REFRESHING)
				{
					break;
				}

				boolean canDrag = true;
				if (mFirstView != null)
				{
					// 获取listview的左上角点的坐标
					int[] lvLoc = new int[2];
					this.getLocationOnScreen(lvLoc);
					Log.d(TAG, "listView : " + lvLoc[0] + "   " + lvLoc[1]);

					// 获取第一个view的左上角坐标
					int[] fvLoc = new int[2];
					mFirstView.getLocationOnScreen(fvLoc);
					Log.d(TAG, "firstView : " + fvLoc[0] + "   " + (fvLoc[1] - getDividerHeight()));

					// 如果第一个view是全部露出来的时候
					// 没有完全露出

					if (lvLoc[1] > (fvLoc[1] - getDividerHeight()))
					{
						// 没有露出来
						canDrag = false;

						if (space == -1)
						{
							space = lvLoc[1] - (fvLoc[1] - getDividerHeight());
						}
					}
					else
					{
						canDrag = true;

						// 全部露出来时
						// 判断第一次是否是相等的,是相等的需要拦截
						if (space == -1 && lvLoc[1] == (fvLoc[1] - getDividerHeight()))
						{
							isInterceptTouch = false;
						}
						// space = 0;
					}
				}
				// 如果第一个是可见的情况下
				int firstVisiblePosition = getFirstVisiblePosition();
				Log.d(TAG, "firstVisiblePosition : " + firstVisiblePosition);

				if (diffY > 0 && firstVisiblePosition == 0 && canDrag)
				{
					// Log.d(TAG, "触摸滑动.....");

					// diffY - (两个点间的差值)
					int paddingTop = (diffY - space) - mRefreshHeaderHeight;
					mRefreshLayout.setPadding(0, paddingTop, 0, 0);

					// 如果刷新的view全部露出来状态改变，ui改变
					if (paddingTop >= 0 && mCurrentState != STATE_RELEASE_REFRESH)
					{

						// 状态改变为松开刷新
						mCurrentState = STATE_RELEASE_REFRESH;
						Log.d(TAG, "松开刷新");
						Log.d(TAG, "space : " + space);
						// UI更新
						refreshUI();
					}
					else if (paddingTop < 0 && mCurrentState != STATE_PULL_REFRESH)
					{
						// 状态改变为下拉刷新
						mCurrentState = STATE_PULL_REFRESH;
						Log.d(TAG, "下拉刷新");

						// UI更新
						refreshUI();
					}

					if (isInterceptTouch) { return true; }
				}
				break;
			case MotionEvent.ACTION_UP:
				mDownY = -1;
				space = -1;
				isInterceptTouch = true;

				// 松开
				if (mCurrentState == STATE_PULL_REFRESH)
				{
					// 如果当前是下拉刷新的状态时，松开，不显示刷新头--->下拉刷新:
					// 200,199,...-mRefreshHeaderHeight
					// mRefreshLayout.setPadding(0, -mRefreshHeaderHeight, 0,
					// 0);

					int start = mRefreshLayout.getPaddingTop();
					int end = -mRefreshHeaderHeight;

					doHeaderAnimator(start, end);
				}
				else if (mCurrentState == STATE_RELEASE_REFRESH)
				{
					// 如果当前是释放刷新时，松开，变为正在刷新
					mCurrentState = STATE_REFRESHING;
					// UI改变
					refreshUI();

					// 设置刷新部分padding: 1000,999,998,.....,0
					// mRefreshLayout.setPadding(0, 0, 0, 0);

					int start = mRefreshLayout.getPaddingTop();
					int end = 0;

					doHeaderAnimator(start, end);

					// 加载数据-->list--->adapter更新
					if (mListener != null)
					{
						mListener.onRefreshing();
					}
				}
				break;
			default:
				break;
		}
		return super.onTouchEvent(ev);
	}

	private void doHeaderAnimator(int start, int end)
	{
		long duration = Math.abs(end - start) * 10;
		if (duration > 600)
		{
			duration = 600;
		}

		ValueAnimator animator = ValueAnimator.ofInt(start, end);
		animator.setDuration(duration);
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator)
			{
				int value = (Integer) animator.getAnimatedValue();
				mRefreshLayout.setPadding(0, value, 0, 0);
			}
		});
		animator.setInterpolator(new AccelerateInterpolator(2f));
		animator.start();
	}

	private void refreshUI()
	{
		switch (mCurrentState)
		{
			case STATE_PULL_REFRESH:
				// 下拉刷新

				// 状态文本改变
				mTvRefreshState.setText("下拉刷新");

				// 箭头旋转
				// 隐藏进度
				mIvArrow.setVisibility(View.VISIBLE);
				mRefreshProgress.setVisibility(View.INVISIBLE);

				RotateAnimation up2DownAnim = new RotateAnimation(-180, 0,
																	Animation.RELATIVE_TO_SELF, 0.5f,
																	Animation.RELATIVE_TO_SELF, 0.5f);
				up2DownAnim.setDuration(300);
				up2DownAnim.setFillAfter(true);
				mIvArrow.startAnimation(up2DownAnim);

				break;
			case STATE_RELEASE_REFRESH:
				// 松开刷新

				// 状态文本改变
				mTvRefreshState.setText("松开刷新");
				// 箭头旋转
				// 隐藏进度
				mIvArrow.setVisibility(View.VISIBLE);
				mRefreshProgress.setVisibility(View.INVISIBLE);

				RotateAnimation down2upAnim = new RotateAnimation(0, 180,
																	Animation.RELATIVE_TO_SELF, 0.5f,
																	Animation.RELATIVE_TO_SELF, 0.5f);
				down2upAnim.setDuration(300);
				down2upAnim.setFillAfter(true);
				mIvArrow.startAnimation(down2upAnim);

				break;
			case STATE_REFRESHING:
				mIvArrow.clearAnimation();

				// 正在刷新
				// 状态文本改变
				mTvRefreshState.setText("正在刷新");

				// 隐藏箭头，显示进度
				mIvArrow.setVisibility(View.INVISIBLE);
				mRefreshProgress.setVisibility(View.VISIBLE);
				break;
			default:
				break;
		}
	}

	public void setRefreshFinish()
	{
		// 上拉加载
		if (isLoadMore)
		{
			// 隐藏footer
			mFooterLayout.setPadding(0, -mFooterHeight, 0, 0);

			// 状态改变
			isLoadMore = false;
		}
		else
		{
			// 下拉刷新
			// 隐藏刷新部分
			// mRefreshLayout.setPadding(0, -mRefreshHeaderHeight, 0, 0);
			int start = mRefreshLayout.getPaddingTop();
			int end = -mRefreshHeaderHeight;
			doHeaderAnimator(start, end);

			// 状态改变
			mCurrentState = STATE_PULL_REFRESH;
			// UI更新
			refreshUI();

			// 设置刷新的时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String format = sdf.format(new Date(System.currentTimeMillis()));
			mTvRefreshDate.setText(format);
		}
	}

	public void setOnRefreshListener(OnRefreshListener listener)
	{
		this.mListener = listener;
	}

	public interface OnRefreshListener
	{
		/**
		 * 正在刷新时的回调
		 */
		void onRefreshing();

		/**
		 * 加载更多数据
		 */
		void onLoadMore();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		// 滑动状态改变时的回调
		int lastVisiblePosition = view.getLastVisiblePosition();// 获得最后一个可见的
		Log.d(TAG, "lastVisiblePosition : " + lastVisiblePosition);

		// 1. 刷新的头，
		// 2. 轮播
		// 3. adapter 10
		// 4. footer 1.

		// int headerViewsCount = getHeaderViewsCount();
		// int footerViewsCount = getFooterViewsCount();

		int index = this.getAdapter().getCount() - 1;
		Log.d(TAG, "index : " + index);

		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
			&& lastVisiblePosition == index)
		{
			// 如果当不是加载更多
			if (!isLoadMore)
			{
				// 改变状态
				isLoadMore = true;

				// 显示footerView
				mFooterLayout.setPadding(0, 0, 0, 0);

				// 滚动到底部
				this.setSelection(getAdapter().getCount());

				// 通知加载更多数据
				if (mListener != null)
				{
					mListener.onLoadMore();
				}
			}
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		// 滑动时的回调
	}

}
