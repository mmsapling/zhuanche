/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.baidu.zhuanche.pay.apay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys
{

	// 合作身份者id，以2088开头的16位纯数字
	public static final String	DEFAULT_PARTNER	= "2088121000279565";

	// 收款支付宝账号
	public static final String	DEFAULT_SELLER	= "crossbordercar@163.com";

	// 商户私钥，自助生成
	public static final String	PRIVATE			= "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMs+ap2vGbtrdJASO3oVxnoolXRD1NNK/C/CvOMuzc02GqmTtUWewMPV4nF9/BhFsdRClstZlvbTlXJlNQDX+ZvxTGkwD3Dho2cI9AUq/HHxesqh8FbdPe6ZJeG45BPTKqhN9Fm/dhwK5exQkHVyumXcIuYvOZ9iicVIm1qRpqTlAgMBAAECgYEAyjeh8J3lZlp1sVrSnpfH0IdMnE9ZE5mJ7SM+uHM7BLCOOqbiNxDLDo5iLTWCe8vsMRrH2i+bSp4eFaGo6Nv6VmtqQvy8GWNZ/97tTBbNbt5JRcSaPnQPcPQFrtkEffct7lYLry3se8O3O1BdxvPzI6MzotsMFnHC5fPaqLxXEsECQQD99eQxE3u8erIihFjUc2CLkV7W51eDjJTS99tESKbDNZX6ZNpKgEVdZvc33VKBBzkTM26LKTHBdKPTN59ci/yZAkEAzOBCJdDtjexd5Vsc0W32yeQo9QEWGLWwWlJaaiKWT3UHwfXKF4/HbaOlXFE1Y8AZ3DMGq8FmQ712vJDezf3uLQJBAOguv+KyamUmU+hE0aXimjjfvjKCLTuC8qlym4/xUC2WtzjGreCBJ2m+cnUrrntyrBbG5eDq16eDySeZaeRFw7kCQHBj1eXI54ij0h6wDO8W0/uNHqrJHcYQZd4mr1LqBmswWEEfBO5IAa8zxtMN+avvBkC1/ULbpa4jka+ACboXQ0ECQCEY7Nz57i60A9o/TOChPG6aUMyX9bDsWn207pcnFZ4EpOvXNyGjDoN38gbJxHEPPb/hEpoGmZ6hkL6kr5ziASs=";
	public static final String	PUBLIC			= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDeAhjFUkJRkO7YbNwxoX4hyTtw2AolJOrV0mOhGvrdX986NhzMS2BJ6A7gPoY/uyuJs44A9lzHPo3NY/5vd/GRhQ/PAv1lJe6atmwNCpEVf7TQ8eQVqToKA3ElbieTckxxqJy0sLOfPr+RITF+qczX/3hFcheg5PCJw+4GbCoewwIDAQAB";

}
