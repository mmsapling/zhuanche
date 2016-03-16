package com.baidu.zhuanche.bean;

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;


public class CartTypeBean extends BaseBean
{
	public List<LevelBean> content;
	public class LevelBean{
		public String name;
		public String eid;
		public String value;
	}
}
