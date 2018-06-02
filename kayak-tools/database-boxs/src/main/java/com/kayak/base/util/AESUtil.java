package com.kayak.base.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
	// 加密
	public static String encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null) {
			throw new Exception("秘钥为空");
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			throw new Exception("秘钥长度不是16位");
		}
		System.out.println("加密前数据：" + sSrc);
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

		// return new Base64().encodeToString(encrypted);//
		// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
		System.out.println("加密后数据：" + parseByte2HexStr(encrypted));
		return parseByte2HexStr(encrypted);
	}

	// 解密
	public static String decrypt(String sSrc, String sKey) throws Exception {
		// 判断Key是否正确
		if (sKey == null) {
			throw new Exception("秘钥为空");
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			throw new Exception("秘钥长度不是16位");
		}
		System.out.println("解密前数据：" + sSrc);
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		// byte[] encrypted1 = new Base64().decode(sSrc);// 先用base64解密
		// byte[] original = cipher.doFinal(encrypted1);
		byte[] original = cipher.doFinal(parseHexStr2Byte(sSrc));
		String originalString = new String(original, "utf-8");
		return originalString;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		/*
		 * 此处使用AES-128-ECB加密模式，key需要为16位。
		 */
		String cKey = "kayakoa201600000";
		// 需要加密的字串
		String cSrc = "34204821948294821";
		System.out.println(cSrc);
		// 加密
		String enString = encrypt(cSrc, cKey);
		System.out.println("加密后的字串是：" + enString);

		// 解密
		String DeString = decrypt(enString, cKey);
		System.out.println("解密后的字串是：" + DeString);
	}

}
