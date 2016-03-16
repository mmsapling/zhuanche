package com.baidu.zhuanche.bean;

import com.baidu.zhuanche.bean.OrderListBean.DriverInfo;

public class CommentBean
{
	public CommentContent	content;

	public class CommentContent
	{
		public Comment		comment;
		public DriverInfo	driver_info;
	}

	public class Comment
	{
		public String	createtime; // 1452930048
		public String	did;		// 5
		public String	id;		// 7
		public String	ip;		// 192.168.1.142
		public String	mobile;	// 13652304622
		public String	oid;		// 20151211090850609806
		public String	remark;	// aaaaa
		public String	star;		// 2
		public String	status;	// 0
		public String	uid;		// 27
		public String	username;	// tylzcxw
	}
}
