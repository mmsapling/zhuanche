package com.baidu.zhuanche.bean;

import java.io.Serializable;
import java.util.List;

import com.baidu.zhuanche.base.BaseBean;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.bean
 * @类名: DriverHomeBean
 * @创建者: 陈选文
 * @创建时间: 2016-1-13 下午4:50:19
 * @描述: 司機訂單首頁bean
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DriverHomeBean extends BaseBean
{
	public List<DriverHomeOrder> content;
	public class DriverHomeOrder implements Serializable
	{
		/**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;
		public String	budget;			// 1500
		public String	carpool;		// 1
		public String	cartype;		// 1
		public String	count;			// 5
		public String	createtime;		// 1448439212
		public String	driver_id;		// 1
		public String	fee;			// 1000
		public String	from;			// 广东省深圳市宝民一路宝通大厦
		public String	from_city;		// 深圳市
		public String	from_district;	//
		public String	from_province;	//
		public String	is_hk;			// 1
		public String	luggage;		// 4
		public String	p_lati;			// 39.9175450000
		public String	p_long;			// 116.4187550000
		public String	range;			// 7275647
		public String	remark;			// 1212
		public String	seaport;		// 3
		public String	sn;				// 20151008201023123892
		public String	status;			// 1
		public String	time;			// 1448439212
		public String	to;				// 香港特别行政区国际机场
		public String	to_city;		//
		public String	to_district;	//
		public String	to_province;	//
		public String	user_id;		// 1
		public String   air_number;
		public String   icon;
		public String   username;
		public String   mobile;
		
	}
}
