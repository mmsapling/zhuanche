package com.baidu.zhuanche.bean;

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.bean
 * @类名:	FeeBean
 * @创建者:	陈选文
 * @创建时间:	2016-2-24	下午3:40:47 
 * @描述:	TODO
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class FeeBean extends BaseBean
{
	public List<Fee>  content;
	public class Fee{
		public String eid;//	1
		public String name;//	选项一
		public String select;//	1
		public String value;//	500
	}
}
