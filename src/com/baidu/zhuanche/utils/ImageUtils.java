package com.baidu.zhuanche.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

/**
 * @项目名: NetDemo
 * @包名: org.itheima.net
 * @类名: ImageUtils
 * @创建者: 陈选文
 * 
 * @描述: 三级缓存的工具类
 * 
 * @版本号: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * 
 * @更新内容: TODO
 */
public class ImageUtils
{
	private static final String				TAG			= "ImageUtils";
	// 保证全局只有一个缓存
	private static LruCache<String, Bitmap>	mCaches;
	private static Handler					mHandler;
	private static ExecutorService			mPool;
	private static Map<ImageView, String>	mRecentTags	= new LinkedHashMap<ImageView, String>();

	private Context							mContext;

	private Map<String, String>				mHeaders;

	public ImageUtils(Context context) {
		this.mContext = context;
		if (mCaches == null)
		{
			int maxSize = (int) (Runtime.getRuntime().freeMemory() / 8f + 0.5f);// lru申请的最大内存空间

			mCaches = new LruCache<String, Bitmap>(maxSize);
		}
		if (mHandler == null)
		{
			mHandler = new Handler();
		}
		if (mPool == null)
		{
			mPool = Executors.newFixedThreadPool(3);
		}
	}

	public void configHeader(Map<String, String> map)
	{
		this.mHeaders = map;
	}

	public void display(ImageView iv, String url)
	{
		// 存储最近使用标记
		mRecentTags.put(iv, url);

		// 1. 去内存中去图片
		Bitmap bitmap = mCaches.get(url);
		if (bitmap != null)
		{
			// 内存中有
			iv.setImageBitmap(bitmap);
			return;
		}

		// 2. 去本地获取
		bitmap = getBitmapFromDisk(url);
		if (bitmap != null)
		{
			// 存储到内存
			mCaches.put(url, bitmap);

			// 显示
			iv.setImageBitmap(bitmap);
			return;
		}

		// 3. 去网络获取
		getBitmapFromNet(url, iv);

	}

	private Bitmap getBitmapFromDisk(String url)
	{
		File file = getBitmapFile(url);

		// 如果文件存在
		if (file.exists()) { return BitmapFactory.decodeFile(file.getAbsolutePath()); }

		return null;
	}

	public File getBitmapFile(String url)
	{
		File file;
		String name = MD5Utils.encode(url);
		// 找本地存储图片的路径
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			// sd卡存在
			File dir = new File(Environment.getExternalStorageDirectory(), "/Android/data/" + mContext.getPackageName() + "/icon");

			if (!dir.exists())
			{
				dir.mkdirs();
			}

			file = new File(dir, name);
		}
		else
		{
			File dir = new File(mContext.getCacheDir(), "icon");

			if (!dir.exists())
			{
				dir.mkdirs();
			}

			file = new File(dir, name);
		}
		return file;
	}

	private void getBitmapFromNet(String url, ImageView iv)
	{
		// 网络是耗时操作
		// 开线程下载
		// new Thread(new LoadImageTask(url, iv)).start();

		mPool.execute(new LoadImageTask(url, iv));
	}

	private class LoadImageTask implements Runnable
	{

		private String		mUrl;
		private ImageView	mIv;

		public LoadImageTask(String url, ImageView iv) {
			this.mUrl = url;
			this.mIv = iv;
		}

		@Override
		public void run()
		{
			// 去网络加载图片
			HttpClient client = new DefaultHttpClient();

			HttpGet get = new HttpGet(mUrl);

			if (mHeaders != null && mHeaders.size() > 0)
			{
				for (Map.Entry<String, String> me : mHeaders.entrySet())
				{
					get.addHeader(me.getKey(), me.getValue());
				}
			}

			try
			{
				HttpResponse response = client.execute(get);

				// 获得结果流
				InputStream is = response.getEntity().getContent();

				// 将流转化为bitmap
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				// 存储到本地
				save2Disk(mUrl, bitmap);

				// 存储到内存中
				mCaches.put(mUrl, bitmap);

				// 是不是最近的图片,是就显示
				String recentUrl = mRecentTags.get(mIv);

				if (mUrl.equals(recentUrl))
				{
					// 显示图片
					mHandler.post(new Runnable() {

						@Override
						public void run()
						{
							display(mIv, mUrl);
						}
					});
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void save2Disk(String url, Bitmap bitmap)
	{
		File file = getBitmapFile(url);
		FileOutputStream stream;
		try
		{
			stream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, stream);
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
		}
	}

}
