package com.baidu.zhuanche.bean;

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;

public class AllAssessBean extends BaseBean
{
	public Content content;
	
	public class Content {
		public List<Assess>  comment;
		public DriverInfo   driverInfo;
	}
	public class DriverInfo{
		public String carid	;//粤88888
		public String citizenid;//	441323199205234324
		public String id;//	1
		public String name;//	陈锦健
		public String icon;
		public String type;
	}
	public class Assess
	{
		public String	brief;		//
		public String	createtime;	// 1451985106
		public String	did;		// 1
		public String	id;			// 1
		public String	ip;			// 127.0.0.1
		public String	mobile;		// 17097200864
		public String	oid;		// 20151215092550590920
		public String	remark;		// 阿斯达山东省的
		public String	star;		// 5
		public String	status;		// 1
		public String	uid;		// 1
	}
}
