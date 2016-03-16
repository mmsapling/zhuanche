package com.baidu.zhuanche.bean;

import java.io.Serializable;
import java.util.List;

import com.baidu.zhuanche.base.BaseBean;

public class OrderListBean extends BaseBean implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4169323749213443451L;
	public List<OrderBean>		content;

	public class OrderBean implements Serializable
	{
		private static final long	serialVersionUID	= -6039656737371070835L;
		public String				budget;										// 1500
		public String				carpool;										// 1
		public String				cartype;										// 五人豪华型
		public String				count;											// 5
		public String				fee;											// 1000
		public String				from;											// 广东省深圳市宝民一路宝通大厦
		public String				seaport;										// 罗湖
		public String				sn;											// 20151008201023123892
		public String				status;										// 1
		public String				time;											// 1448439212
		public String				to;											// 香港特别行政区国际机场
		public String				driver_id;
		public String				is_hk;
		public String				luggage;
		public DriverInfo			d_del;
		public String				air_number;
	}

	public class DriverInfo implements Serializable
	{
		/**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;
		public String				area;						//
		public String				area1;						// 852
		public String				carid;						// rhjg
		public String				citizenid;					// fhjjhg
		public String				icon;						// /pinche/Upload/icon/member/20160112/20160112110136_97835.jpg
		public String				id;						// 5
		public String				mobile;					// 13652304622
		public String				mobile1;					// 15580714398
		public String				name;						// fhkf
		public String				star;						// 0
		public String				type;						// dhjj
	}
}
