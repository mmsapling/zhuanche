package com.baidu.zhuanche.bean;

import java.util.List;

import com.baidu.zhuanche.base.BaseBean;


public class FeeBean extends BaseBean
{
	public List<Fee>  content;
	public class Fee{
		public String eid;//	1
		public String name;//	选项一
		public String select;//	1
		public String value;//	500
	}
}
