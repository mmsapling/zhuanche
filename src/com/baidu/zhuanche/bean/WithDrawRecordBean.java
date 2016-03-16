package com.baidu.zhuanche.bean;//

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;
//
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
