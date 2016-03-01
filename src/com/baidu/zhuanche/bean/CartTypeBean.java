package com.baidu.zhuanche.bean;

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;


/**
 * @项目名: 	ZhuanChe
 * @包名:	com.baidu.zhuanche.bean
 * @类名:	CartTypeBean
 * @创建者:	陈选文
 * @创建时间:	2015-12-4	下午6:44:36 
 * @描述:	车型级别信息类
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class CartTypeBean extends BaseBean
{
	public List<LevelBean> content;
	public class LevelBean{
		public String name;
		public String eid;
		public String value;
	}
}
