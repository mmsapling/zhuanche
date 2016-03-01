package com.baidu.zhuanche.bean;

import java.io.Serializable;



/**
 * @项目名: 	ZhuanChe
 * @包名:	com.baidu.zhuanche.bean
 * @类名:	User
 * @创建者:	陈选文
 * @创建时间:	2015-12-3	下午6:38:07 
 * @描述:	司机信息类
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class Driver implements Serializable
{
	public String access_token;
	public String password;
	public String area;
	public String autograph;
	public String gender;//	2 女  1男   0不确定
	public String id	;//1
	public String icon;
	public String star;
	public String location;//	
	public String loginip	;//192.168.1.186
	public String logintime;//	1449459307
	public String logintimes;//	109
	public String mobile;//	17097200864
	public String status;//	1
	public String username;//	
	@Override
	public String toString()
	{
		return "Driver [access_token=" + access_token
				+ ", password="
				+ password
				+ ", autograph="
				+ autograph
				+ ", gender="
				+ gender
				+ ", id="
				+ id
				+ ", location="
				+ location
				+ ", loginip="
				+ loginip
				+ ", logintime="
				+ logintime
				+ ", logintimes="
				+ logintimes
				+ ", mobile="
				+ mobile
				+ ", status="
				+ status
				+ ", username="
				+ username
				+ "]";
	}
	
}
