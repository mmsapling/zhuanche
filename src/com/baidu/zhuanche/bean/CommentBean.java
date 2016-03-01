package com.baidu.zhuanche.bean;

import com.baidu.zhuanche.bean.OrderListBean.DriverInfo;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.bean
 * @类名: CommentBean
 * @创建者: 陈选文
 * @创建时间: 2016-1-16 下午6:13:32
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
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
