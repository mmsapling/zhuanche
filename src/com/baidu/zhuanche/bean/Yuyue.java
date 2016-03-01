package com.baidu.zhuanche.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.bean
 * @类名: Yuyue
 * @创建者: 陈选文
 * @创建时间: 2015-12-26 下午6:45:52
 * @描述: 预约接口所需要的值
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class Yuyue
{
	public Yuyue() {
		signs.add("1");
		signs.add("2");
		signs.add("3");
		signs.add("4");
	}

	/** 预约接口所需要的值 */
	public String		cartype;									// 级别
	public String		carpool			= "0";						// 类型
	public String		signtype;									// 签证类型
	public List<String>	signs			= new ArrayList<String>();
	public String		time;										// 时间
	public String		seaport			= "0";						// 港口
	public Location		getOnLocation	= new Location();			// 上车地点
	public Location		getOffLocation	= new Location();			// 下车地点
	public String		peopleCount;
	public String		xingliCount		= "0";
	public String		fee				= "0";
	public String		des;
	public String		budget;
	public String		maxPeopleCount	= "0";
	public String		phone;
}
