package com.baidu.zhuanche.utils;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	private static final String TAG = "MD5Utils";

	/**
	 * 加密文本
	 * 
	 * @param pwd
	 * @return
	 */
	public static String encode(String pwd) {

		try {
			MessageDigest instance = MessageDigest.getInstance("md5");

			// 加密byte
			byte[] digest = instance.digest(pwd.getBytes());

			StringBuffer sb = new StringBuffer();
			// -128--127--->0x11
			for (byte b : digest) {
				int r = b & 0xff;//

				String hex = Integer.toHexString(r);//

				if (hex.length() == 1) {
					hex = 0 + hex;
				}
				sb.append(hex);
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param is
	 * @return
	 */
	public static String encode(InputStream in) {

		try {
			MessageDigest digester = MessageDigest.getInstance("MD5");
			byte[] bytes = new byte[8192];
			int byteCount;
			while ((byteCount = in.read(bytes)) > 0) {
				digester.update(bytes, 0, byteCount);
			}
			byte[] digest = digester.digest();

			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				int r = b & 0xff;//
				String hex = Integer.toHexString(r);//
				if (hex.length() == 1) {
					hex = 0 + hex;
				}
				sb.append(hex);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
