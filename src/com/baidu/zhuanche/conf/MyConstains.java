package com.baidu.zhuanche.conf;

public interface MyConstains
{
	boolean	OPEN_LONG_SERVICE	= false;
	boolean	OPEN_SERVICE		= false;														// 这两个一定要取相反的值
																								// //
																								// 是否打开长连接服务
	String	SPFILENAME			= "zhuanche";													// sp名称
	String	ITEMBEAN			= "itembean";													// 订单列表数据传递
	long	TIME_LOCATION		= 1000 * 60 * 60 * 24;											// 首页定位时间

	int		TIME_USER_PEROID	= 1000 * 60 * 20;												// 30秒
	int		TIME_DRIVER_PEROID	= 1000 * 60 * 15;
	int		SERVICE_COUNT		= 50;															// service
																								// 服务的最大值
	String	ACTION_USER_NAME	= "com.cxw.longconnectiondemo.service.UserConnectService";
	String	ACTION_DRIVER_NAME	= "com.cxw.longconnectiondemo.service.DriverConnectService";
	String	ACTION_WXPAY		= "com.baidu.zhuanche.receiver.weixinpay";
}
