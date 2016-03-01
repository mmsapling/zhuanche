package com.baidu.zhuanche.bean;

import com.baidu.zhuanche.base.BaseBean;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.bean
 * @类名: IdentityCheckBean
 * @创建者: 陈选文
 * @创建时间: 2016-1-11 下午12:38:48
 * @描述: 身份審覈信息
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class IdentityCheckBean extends BaseBean
{
	public Identity	content;

	public class Identity
	{
		public String	carid;			// 111
		public String	carid_pic;		// /pinche/Upload/driver/private/20160111/20160111115925_56719.jpg
		public String	cartype;		// 五人豪华型
		public String	citizenid;		// 11111111
		public String	citizenid_pic;	// /pinche/Upload/driver/private/20160111/20160111115925_85410.jpg
		public String	createtime;	// 1452484765
		public String	driverid;		// 1111111
		public String	driverid_pic;	// /pinche/Upload/driver/private/20160111/20160111115925_48504.jpg
		public String	id;			// 5
		public String	name;			// 111
		public String	seaport;		// 3
		public String	type;			// 11111
		public String	user_id;		// 5
		public String	status;
		public String	remark;
	}
}
