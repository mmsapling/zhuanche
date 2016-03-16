package com.baidu.zhuanche.bean;

import java.io.Serializable;

import com.amap.api.maps.model.LatLng;


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
