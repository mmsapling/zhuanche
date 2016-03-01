package com.baidu.zhuanche.bean;

import com.baidu.zhuanche.base.BaseBean;

/**
 * @项目名: ZhuanChe
 * @包名: com.baidu.zhuanche.bean
 * @类名: UserBean
 * @创建者: 陈选文
 * @创建时间: 2015-12-9 上午9:35:37
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class UserBean extends BaseBean
{
	public ContentBean	content;

	public class ContentBean
	{
		public String	access_token;
		public User		member_data;
	}
	/**
	 * autograph : gender : 0 icon :
	 * /pinche/Upload/icon/member/20151208/20151208093602_20695.jpg id : 25
	 * location : loginip : 192.168.1.102 logintime : 1449625013 logintimes :
	 * 382 mobile : 15620606824 status : 1 username : user1511059918
	 */
}
