package com.baidu.zhuanche.bean;

import java.io.Serializable;

import com.amap.api.maps.model.LatLng;


/**
 * @项目名: 	拼车
 * @包名:	com.baidu.zhuanche.bean
 * @类名:	Location
 * @创建者:	陈选文
 * @创建时间:	2015-12-30	上午10:49:25 
 * @描述:	TODO
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class Location implements Serializable
{
	/**详细地址*/
	public String address;
	public String province;
	public String city;
	public String district;
	/**经纬度*/
	public LatLng latLng;
	@Override
	public String toString()
	{
		return "Location [address=" + address + ", province=" + province + ", city=" + city + ", district=" + district + ", latLng=" + latLng + "]";
	}
	
	
}
