package com.baidu.zhuanche.bean;

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.bean
 * @类名:	SearportBean
 * @创建者:	陈选文
 * @创建时间:	2016-1-9	下午6:48:21 
 * @描述:	港口数据
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class SearportBean extends BaseBean
{
	public List<SearPort> content;
	public class SearPort{
		public String name;
		public String eid;
		public String value;
	}
}
