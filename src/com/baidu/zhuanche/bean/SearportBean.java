package com.baidu.zhuanche.bean;

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;


public class SearportBean extends BaseBean
{
	public List<SearPort> content;
	public class SearPort{
		public String name;
		public String eid;
		public String value;
	}
}
