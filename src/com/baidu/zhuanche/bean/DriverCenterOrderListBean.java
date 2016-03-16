package com.baidu.zhuanche.bean;

import java.io.Serializable;
import java.util.List;

import com.baidu.zhuanche.base.BaseBean;

public class DriverCenterOrderListBean extends BaseBean
{
	public List<OrderBean>	content;

	public class OrderBean implements Serializable
	{
		private static final long	serialVersionUID	= -6039656737371070835L;
		public String				budget;										// 1500
		public String				carpool;										// 1
		public String				cartype;										// 五人豪华型
		public String				count;											// 5
		public String				driver_id;										// 1
		public String				fee;											// 1000
		public String				from;											// 广东省深圳市宝民一路宝通大厦
		public String				seaport;										// 罗湖
		public String				sn;											// 20151008201023123892
		public String				status;										// 1
		public String				time;											// 1448439212
		public String				to;											// 香港特别行政区国际机场
		public String				user_id;										// 1
		public String				username;
		public String 				mobile;
		public String				icon;
	}

}
