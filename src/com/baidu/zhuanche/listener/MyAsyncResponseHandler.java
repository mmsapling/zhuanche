package com.baidu.zhuanche.listener;


import org.apache.http.Header;
import org.json.JSONException;

import android.os.AsyncTask;

import com.baidu.zhuanche.base.BaseApplication;
import com.baidu.zhuanche.bean.Driver;
import com.baidu.zhuanche.bean.DriverBean;
import com.baidu.zhuanche.bean.User;
import com.baidu.zhuanche.bean.UserBean;
import com.baidu.zhuanche.conf.URLS;
import com.baidu.zhuanche.utils.AsyncHttpClientUtil;
import com.baidu.zhuanche.utils.JsonUtils;
import com.baidu.zhuanche.utils.PrintUtils;
import com.baidu.zhuanche.utils.ToastUtils;
import com.baidu.zhuanche.utils.UIUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @项目名: 拼车
 * @包名: com.baidu.zhuanche.listener
 * @类名: MyAsyncResponseHandler
 * @创建者: 陈选文
 * @创建时间: 2015-12-23 下午5:31:55
 * @描述: TODO
 * 
 * @svn版本: $Rev$
 * @更新人: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public abstract class MyAsyncResponseHandler extends AsyncHttpResponseHandler
{
	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
	{
		failure();
	}

	public void failure()
	{
		ToastUtils.closeProgress();// 关闭进度条
		ToastUtils.makeShortText(UIUtils.getContext(), "请求失败！请检查网络！");
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
	{
		final String json = new String(arg2);
		try
		{
			ToastUtils.closeProgress();// 关闭进度条
			PrintUtils.print("json =" + json);
			final long code = JsonUtils.getCode(json);
			User user = BaseApplication.getUser();
			Driver driver = BaseApplication.getDriver();
			

			new AsyncTask<Void, Integer, Void>() {

				@Override
				protected void onPreExecute()
				{
					User user = BaseApplication.getUser();
					Driver driver = BaseApplication.getDriver();
					if (user != null)
					{
						PrintUtils.println("用户不等于空！");
						//lianjieUser();
					}
					else if (driver != null)
					{
						PrintUtils.println("司机不等于空！");
						//lianjieDriver();
					}
					super.onPreExecute();

				}

				@Override
				protected Void doInBackground(Void... params)
				{
					return null;
				}

				@Override
				protected void onPostExecute(Void result)
				{
					super.onPostExecute(result);
					chuli(code, json);
					// success(json);
				}

			}.execute();

			// if (0 == code)
			// {
			//
			// //success(json);
			// }

		}
		catch (JSONException e)
		{
			ToastUtils.closeProgress();// 关闭进度条
			ToastUtils.makeShortText(UIUtils.getContext(), "JSON解析异常！");
			e.printStackTrace();
		}

	}

	/**
	 * 用户模块
	 */
	public void lianjieUser()
	{
		String url = URLS.BASESERVER + URLS.User.login;
		RequestParams params = new RequestParams();
		params.add("mobile", BaseApplication.getUser().mobile);
		params.add("password", BaseApplication.getUser().password);
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance(UIUtils.getContext());
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				String json = new String(arg2);
				Gson gson = new Gson();
				UserBean userBean = gson.fromJson(json, UserBean.class);
				// 保存全局用戶信息
				String password = BaseApplication.getUser().password;
				if (userBean.content != null)
				{
					User user = userBean.content.member_data;
					if (user != null && password != null)
					{
						user.password = password;
						user.access_token = userBean.content.access_token;
						BaseApplication.setUser(user);
						PrintUtils.println("用户连接成功！");
					}
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
			}
		});
	}

	public void lianjieDriver()
	{
		String url = URLS.BASESERVER + URLS.Driver.login;
		RequestParams params = new RequestParams();
		params.add("mobile", BaseApplication.getDriver().mobile);
		params.add("password", BaseApplication.getDriver().password);
		AsyncHttpClient client = AsyncHttpClientUtil.getInstance(UIUtils.getContext());
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				String json = new String(arg2);
				Gson gson = new Gson();
				DriverBean driverBean = gson.fromJson(json, DriverBean.class);
				String password = BaseApplication.getDriver().password;
				// 保存全局用戶信息
				if (driverBean.content != null && password != null)
				{
					Driver driver = driverBean.content.driver_data;
					driver.password = password;
					driver.access_token = driverBean.content.access_token;
					BaseApplication.setDriver(driver);
					PrintUtils.println("司机连接成功！");
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
			}
		});
	}

	public abstract void success(String json);

	public void chuli(long code, String json)
	{
		if (0 != code)
		{
			if (-1 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "系统繁忙");
				return;
			}
			else if (40000 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "参数错误！");
				return;
			}
			else if (40001 == code)
			{
				PrintUtils.println("未登录或登陆信息已失效！");
				User user = BaseApplication.getUser();
				Driver driver = BaseApplication.getDriver();
				if (user != null)
				{
					PrintUtils.println("用户不等于空！");
					lianjieUser();
				}
				else if (driver != null)
				{
					PrintUtils.println("司机不等于空！");
					lianjieDriver();
				}
			}
			else if (40002 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "密码错误！");
				return;
			}
			else if (40003 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "短信验证码错误！");
				return;
			}
			else if (41000 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "缺少请求参数！");
				return;
			}
			else if (41001 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "手机号码为空！");
				return;
			}
			else if (41002 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "登陆密码为空！");
				return;
			}
			else if (41003 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "短信验证码为空！");
				return;
			}
			else if (41004 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "accton_token为空！"); // TODO
				return;
			}
			else if (41005 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "枚举类型为空！");
				return;
			}
			else if (41006 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "起始地为空！");
				return;
			}
			else if (41007 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "出发地为空！");
				return;
			}
			else if (43001 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "需要POST请求！");
				return;
			}
			else if (43002 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "参数长度过短！");
				return;
			}
			else if (43003 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "参数不再接收范围内！");
				return;
			}
			else if (43004 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "文件格式错误！");
				return;
			}
			else if (43005 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "时间不正确！");
				return;
			}
			else if (43006 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "手机号码不合法！");
				return;
			}
			else if (45001 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "图片大小超过系统限制！");
				return;
			}
			else if (46001 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "用户不存在！");
				return;
			}
			else if (46002 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "短信验证码未获取或已失效！");
				return;
			}
			else if (46003 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "目录不存在！");
				return;
			}
			else if (46004 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "订单号不存在！");
				return;
			}
			else if (49001 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "请勿重复评论！");
				return;
			}
			else if (49002 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "认证信息已存在！");
				return;
			}
			else if (49003 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "数据重复提交！");
				return;
			}
			else if (49005 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "你還有訂單未完成，請完成後再來接單！");
				return;
			}
			else if (50001 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "账号被禁用！");
				return;
			}
			else if (50002 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "手机号码已注册！");
				return;
			}
			else if (50003 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "账号在其他地方登陆,请退出后重新登陆！");
				return;
			}
			else if (60001 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "系统错误！");
				return;
			}
			else if (60002 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "短信接口错误！");
				return;
			}
			else if (60003 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "图片接口错误！");
				return;
			}
			else if (40 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "常见错误/参数不合法！");
				return;
			}
			else if (41 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "缺少参数！");
				return;
			}
			else if (42 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "请求超时！");
				return;
			}
			else if (43 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "数据格式不正确！");
				return;
			}
			else if (45 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "参数内容过大！");
				return;
			}
			else if (46 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "不存在！");
				return;
			}
			else if (47 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "数据解析失败！");
				return;
			}
			if (48 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "接口未授权！");
				return;
			}
			else if (49 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "已存在！");
				return;
			}
			else if (5 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "用户账号故障！");
				return;
			}
			else if (6 == code)
			{
				ToastUtils.makeShortText(UIUtils.getContext(), "系统错误！");
				return;
			}
			// else
			// {
			// ToastUtils.makeShortText(UIUtils.getContext(), "未知状态码错误！");
			// return;
			// }
		}
		else
		{
			success(json);
		}
	}
}
