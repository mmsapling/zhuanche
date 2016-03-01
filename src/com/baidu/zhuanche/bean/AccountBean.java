package com.baidu.zhuanche.bean;//

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;
//
//

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.bean
 * @类名: AccountBean
 * @创建者: 陈选文
 * @创建时间: 2016-1-6 下午7:46:04
 * @描述: 今日进帐，历史进帐bean
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class AccountBean extends BaseBean
{
	public List<Account>	content;	//

	public class Account
	{
		public String	id;			// 19
		public String	money;		// 10000
		public String	status;		// 1
		public String	time;		// 1452079678
		public String	type;		// 1
		public String	user_id;	// 5
	}
}
