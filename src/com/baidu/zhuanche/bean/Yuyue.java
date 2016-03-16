package com.baidu.zhuanche.bean;

import java.util.ArrayList;
import java.util.List;

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
