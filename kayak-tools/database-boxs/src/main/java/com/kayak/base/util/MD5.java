package com.kayak.base.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

/**
 * MD5加密方法
 */
public class MD5 {
	protected static Logger log = Logger.getLogger(MD5.class);

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	/**
	 * MD5加密方法
	 * 
	 * @param origin
	 *            要加密的文本，默认iso-8859-1编码
	 * @return 返回MD5加密后的文本（全大写）
	 */
	public static String MD5Encode(String origin) {
		return MD5Encode(origin, "iso-8859-1");
	}

	/**
	 * MD5加密方法
	 * 
	 * @param origin
	 *            要加密的文本
	 * @param charsetName
	 *            编码
	 * @return
	 */
	public static String MD5Encode(String origin, String charsetName) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(origin.getBytes(charsetName));
			return byte2hex(md5.digest());
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}
}
