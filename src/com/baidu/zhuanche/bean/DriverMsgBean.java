package com.baidu.zhuanche.bean;

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;

public class DriverMsgBean extends BaseBean
{
	public Content content;
	public class Content{
		public List<Msg> message;
	}
}
