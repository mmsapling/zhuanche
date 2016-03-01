package com.baidu.zhuanche.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.baidu.zhuanche.bean.OrderListBean.OrderBean;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.utils
 * @类名:	OrderUtil
 * @创建者:	陈选文
 * @创建时间:	2015-12-24	下午7:06:42 
 * @描述:	TODO
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class OrderUtil
{
	/**
	 * 根据状态获取订单状态
	 * @param status
	 * @return
	 */
	public static String getStatusText(String status)
	{
		String text = "订单状态";
		switch (Integer.parseInt(status))
		{
			case 0:
				text = "预约中";
				break;
			case 1:
				text = "已预约";
				break;
			case 2:
				text = "已付款";
				break;
			case 3:
				text = "已完成";
				break;
			case 4:
				text = "已取消";
				break;
			case 5:
				text = "等待确认";
			default:
				break;
		}
		return text;
	}
	/**
	 * 根据状态获取是否是拼车
	 * @param status
	 * @return
	 */
	public static String getCarPoolText(String status)
	{
		String text = "拼车类型";
		switch (Integer.parseInt(status))
		{
			case 0:
				text = "专车";
				break;
			case 1:
				text = "顺风车";
				break;
			default:
				break;
		}
		return text;
	}
	/**
	 * 时间
	 * @param time
	 * @return
	 */
	public static String getDateText(String time)
	{
		Long timestamp = Long.parseLong(time)*1000;  
		String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date(timestamp));  
		return date;  
	}
	/**
	 * 订单按照时间排序
	 */
	public static void sortOrder(List<OrderBean> datas)
	{
		Collections.sort(datas, new Comparator<OrderBean>() {

			@Override
			public int compare(OrderBean o1, OrderBean o2)
			{
				long time1 = Long.parseLong(o1.time);
				long time2 = Long.parseLong(o2.time);
				if (time1 > time2)
				{
					return -1;
				}
				else if (time1 < time2) {
				return 1;
				}
				return 0;
			}
		});
	}
}
