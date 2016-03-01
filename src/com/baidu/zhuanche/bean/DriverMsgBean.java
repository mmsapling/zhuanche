package com.baidu.zhuanche.bean;

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.bean
 * @类名:	UserMsgBean
 * @创建者:	陈选文
 * @创建时间:	2016-1-8	下午5:28:15 
 * @描述:	司机端消息数据
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class DriverMsgBean extends BaseBean
{
	public Content content;
	public class Content{
		public List<Msg> message;
	}
}
