package com.baidu.zhuanche.bean;

import com.baidu.zhuanche.base.BaseBean;

/**
 * 
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.bean
 * @类名: DriverBean
 * @创建者: 陈选文
 * @创建时间: 2016-1-5 下午2:13:17
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class DriverBean extends BaseBean
{
	public ContentBean	content;

	public class ContentBean
	{
		public String	access_token;
		public Driver	driver_data;
	}
	/*{
	    "code": 0,
	    "content": {
	        "access_token": "00dc959494ade75152cea545b4c5909d",
	        "driver_data": {
	            "autograph": "0",
	            "gender": "0",
	            "id": "5",
	            "location": "",
	            "loginip": "192.168.1.135",
	            "logintime": "0",
	            "logintimes": "0",
	            "mobile": "13652304622",
	            "status": "1",
	            "username": "user1601052219"
	        }
	    },
	    "message": "请求成功"
	}*/
}
