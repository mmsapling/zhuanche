package com.baidu.zhuanche.bean;

import java.io.Serializable;
import java.util.List;

import com.baidu.zhuanche.base.BaseBean;

public class UserIndexBean extends BaseBean
{
	public Content	content;

	public class Content
	{
		public List<Article>	article;
		public List<Banner>		banner;
		public List<Cate>		cate;
	}

	public class Cate implements Serializable
	{
		/**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;
		public String	icon;	// /pinche/Upload/image/20160107/20160107102825_39443.png
		public String	id;		// 11
		public String	name;	// 帮助宝典
		public String	pid;	// 0
		public String	sort;	// 8
		public String	status; // 0
	}

	public class Article implements Serializable
	{
		private static final long	serialVersionUID	= 1L;
		public String				cate_id;					// 1
		public String				content;					// assafsdf
		public String				createtime;				// 1452062992
		public String				id;						// 1
		public String				img;						// /pinche/Upload/image/20160106/20160106102850_16580.png
		public String				status;					// 1
		public String				title;						// 阿萨达速度1
		public String				description;
	}

	public class Banner
	{
		public String	createtime; // 1452070408
		public String	id;		// 1
		public String	img;		// /pinche/Upload/image/20160106/20160106163340_65810.png
		public String	name;		// sadssd
		public String	sort;		// 2
		public String	status;	// 1
	}
}
