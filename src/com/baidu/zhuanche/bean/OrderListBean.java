package com.baidu.zhuanche.bean;

import java.io.Serializable;
import java.util.List;

import com.baidu.zhuanche.base.BaseBean;

/**
 * @项目名: ZhuanChe
 * @包名: com.baidu.zhuanche.bean
 * @类名: OrderListBean
 * @创建者: 陈选文
 * @创建时间: 2015-12-8 上午11:57:46
 * @描述: 用户订单列表
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
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
