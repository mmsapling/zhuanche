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
	public static final String	PRIVATE			= "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMDZQZu7AHBXcqKXpfeDBx/p6T08lfgCImoUvtAdKoJDuxfT1zmL9RAKbhVEgleKaOltM5V9jntwP7pJp5U2Zc7ZjzsY3Rju4A2OTapO6gQhuQjkwLqX6oVtZFUFigOvf+JM89uivPJ8CQYhiTBSvMBUtXU9EPpi9B/DirRZdDNdAgMBAAECgYEAonLgh3SBLCEd2cTw84yWZdW+uezdT6tMnhuXmOvYsR6o7atvc3RoHPkMPjIVsfRP58IrX9QCQA0QQbZlypWmL58Y15Fwgfe5yNwQ5tblUmSwv3b91uRDut3QwufjFLiw+Dn926M026NrV9R862XarqGLPnopS/0y6bf+UGTEGEECQQDw5a29qItg+y0q4yjQU6vVYFfpS6vuQzvOBK6HdLLZF5Uk951gupLQU0JPAbxA04KDtz+Hn0o2J8/grksWRkJlAkEAzPBqGAfhkc8Y/YwyA6Lsmrf+bwAZU+ZgMsztPlmXOx57iGRP/EhKBerH1sX2jBmwPgg8Ozyqfy2RZWD6HRGhmQJADu1ch70S7e6CjP7If6g/pJg0ulsbC0eVRB3cJr6fen5QmprRYLYO9OPD3xtva+DvXHQVdrTzJ0nuQrDqLDYsVQJAU9SkEWBn7PUCC/UlIEes8T0CXvVzZtZ900nwYejT6+L0NT6TWWdYoEMBq+EekQCRD07fFHwkYhGJwCaDKRDlQQJAZlzTqxY6guGfdJ4+9/91dJUSlRBbh0v9nVfvjyKUdGktlPRgH6sN7Mj8HQ9Qpe5cxkdweujDKGFK02gZeOvsGQ==";
	public static final String	PUBLIC			= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDA2UGbuwBwV3Kil6X3gwcf6ek9PJX4AiJqFL7QHSqCQ7sX09c5i/UQCm4VRIJXimjpbTOVfY57cD+6SaeVNmXO2Y87GN0Y7uANjk2qTuoEIbkI5MC6l+qFbWRVBYoDr3/iTPPborzyfAkGIYkwUrzAVLV1PRD6YvQfw4q0WXQzXQIDAQAB";
}
