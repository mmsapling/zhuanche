package com.baidu.zhuanche.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @项目名: ZhuanChe
 * @包名: com.baidu.zhuanche.utils
 * @类名: JsonUtils
 * @创建者: 陈选文
 * @创建时间: 2015-12-4 上午10:01:36
 * @描述: 对于本应用的json封装
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class JsonUtils
{
	/**
	 * 得到json的code
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static long getCode(String json) throws JSONException
	{

		JSONObject jsonObject = new JSONObject(json);
		return jsonObject.getLong("code");

	}

	/**
	 * 得到json数据的content对象
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getContent(String json) throws JSONException
	{
		JSONObject jsonObject = new JSONObject(json);
		return jsonObject.getJSONObject("content");
	}

	/**
	 * 得到json的code,得到json数据的content对象
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static JsonBean getJsonBean(String json) throws JSONException
	{
		JSONObject jsonObject = new JSONObject(json);
		long code = jsonObject.getLong("code");
		JSONObject contentObject = jsonObject.getJSONObject("content");
		JsonBean bean = new JsonBean();
		bean.code = code;
		bean.contentObject = contentObject;
		return bean;
	}

	public static class JsonBean
	{
		public long			code;
		public JSONObject	contentObject;
	}
}
