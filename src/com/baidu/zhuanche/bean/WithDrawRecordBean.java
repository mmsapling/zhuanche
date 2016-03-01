package com.baidu.zhuanche.bean;//

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;
//
//

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.bean
 * @类名: WithDrawRecordBean
 * @创建者: 陈选文
 * @创建时间: 2016-1-6 下午3:13:48
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class WithDrawRecordBean extends BaseBean
{
	public List<WithDraw>	content;	//

	public class WithDraw
	{
		public String	bank_location;	// 即使电话
		public String	bank_name;		// 女神姐姐
		public String	bank_number;	// 6228210020037645
		public String	fee;			// 12
		public String	id;			// 1
		public String	money;			// 251
		public String	name;			// 马良
		public String	remark;		//
		public String	status;		// 0
		public String	user_id;		// 1

		@Override
		public String toString()
		{
			return "WithDraw [bank_location=" + bank_location
					+ ", bank_name="
					+ bank_name
					+ ", bank_number="
					+ bank_number
					+ ", fee="
					+ fee
					+ ", id="
					+ id
					+ ", money="
					+ money
					+ ", name="
					+ name
					+ ", remark="
					+ remark
					+ ", status="
					+ status
					+ ", user_id="
					+ user_id
					+ "]";
		}

	}
}
