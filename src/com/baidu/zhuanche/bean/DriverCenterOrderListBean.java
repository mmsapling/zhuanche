package com.baidu.zhuanche.bean;

import java.io.Serializable;
import java.util.List;

import com.baidu.zhuanche.base.BaseBean;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.bean
 * @类名: DriverOrderListBean
 * @创建者: 陈选文
 * @创建时间: 2016-1-7 下午6:42:54
 * @描述: 司机端订单列表数据
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
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
