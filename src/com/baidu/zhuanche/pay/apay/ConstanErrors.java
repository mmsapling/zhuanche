package com.baidu.zhuanche.pay.apay;

/**
 * 因为这些涉及公司安全.所以所有数据经过修改.为无效数据.只是展示数据格式
 */
public class ConstanErrors {
	/*------------------alipay--------------------*/
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static final String ALIPAY_PartnerID = "20883165956724345";
	// 商户签约支付宝账号
	public static final String ALIPAY_SellerID = "billywu@163.com";
	// 商户的私钥（MD5）
	public static final String ALIPAY_MD5_KEY = "asd49fdf5t3kdf31a1cdfd45r6t56vf7";
	// 商户私钥（RSA）,用户自动生成
	public static final String ALIPAY_PartnerPrivKey = "JLJLelsjadfkjILLjJLJADSjljljlJcAgEAadKOPcFCda7Qty8DK4IhPaNLgw2EXlL8LFWXTcVWck/ruDqNtCQJni16fl1UwG6eduxj5jGmWy2q5v6odnml718T1uZktiFOweU3R5Dh2CiB4Ev4GzUJwN4b0uhHO+xbGxA7VKHQg9h0Y+FYBOz8sdfadfojooij6gV+acSa8yBhnAgMBAAECgYA9h8M5VAbPJg77CDCPfd9C+pwcTHFhb/ux5viuMzyqfMRWvs9V0DqbgReRgg4Ep/Ew748/z7TZ7v/e2QEEVOiQGK3svl0XwjiizK+AwoRnaPCQNp061HVaGQMh6UGHmAFB92DfTKqy7Yu1jCfRJoRdQd060I1kJYb/pOi6KATo+QJBAPlAM6LC6h7ZfMW5/3fBo5boMsCM8XLe/T5n/tcqcIQYhB5Cf9IIRHrX/c65Md1oiSDWtuvAGHU7evddtmm2qUsCQQDyRpwP7roZtmJBsk9dqEjs980om9pNJUjhHbjfLKEGrpE4F1jfjxrCW0zc9u2Nf3gTUij/c0esy0JWfM2IlpfVAkBj1oaYXHDA+KbU4KLmykaHrOyfWhVgyNcxhyB5+ULmdpd9M2/VpoAfoKLGikHiCPxcjgTpO6HQW4nuybfGcmcvAkAiZRPSzzlsXRAl84KA8VspCST/FuEvApQOIYIVbOiPYkkLhYUAd/h8jymiVQv0gpwxYgCgA2WxikmorZ8gPQDhAkEAoT1O9oMepSb9iB/LVchtwpWI3Px4dHJzcA7Fv3FvnTvmxfPlFBYaVRNyn91zKmMnEn/ONqFAv02dRrzP8pv4BA==";
	// 支付宝公钥，无需修改该值
	public static final String ALIPAY_PubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDjVAqkADFkWG9PPKJoZkS9WimGOxma6UUzKVuHnPWlsbiJXEgrtfQY+jAU4WxGhDbZN6fUavFq/2Aq8MKrOSavLxfB5YEl1dcStpvAklhpSecdUcMioLoDNuKRhXJGNkTUytBXvlEb+uQ769BY8T7Z8nftLnrEi7BRHPiIIGkDZQIDAQAB";

	/*------------------weixin--------------------*/
	public static final String WEIXIN_APP_ID = "wxb063aafadf342asd";
	public static final String WEIXIN_PARTNER_ID = "41343562432";
	/**
	 * 微信公众平台商户模块和商户约定的密钥
	 * 注意：不能hardcode在客户端，建议genPackage这个过程由服务器端完成
	 */
	public static final String WEIXIN_PARTNER_KEY = "6ca9bb8adfeavc389dadsadsfc728d";
	/**
	 * 微信开放平台和商户约定的密钥
	 * 注意：不能hardcode在客户端，建议genSign这个过程由服务器端完成
	 */
	public static final String WEIXIN_APP_SECRET = "8032jljljasdf79dsf321c323a"; // wxd930ea5d5a258f4f
	// wxd930ea5d5a258f4f 对应的支付密钥
	public static final String WEIXIN_APP_KEY = "P6oadsfjwJLJLildfjal7989HLLJIljlsdIJljlljdazDFItdnRL92scfxfgatWgcV4ovRGd0U9BeKhkE"; // wxd930ea5d5a258f4f
	/*------------------银联--------------------*/
	
}
