package com.kayak.util;

import java.util.Date;

import org.json.JSONObject;

import com.kayak.base.rsa.Base64;
import com.kayak.base.util.MD5;
import com.kayak.base.util.Tools;

public class JwtUtil {

	public static final String ase_key = "kayak2018";
	public static final String key = "kayak2018";

	public static String getToken(String uid) throws Exception {
		JSONObject json = new JSONObject();

		json.put("uid", uid);
		json.put("lat", new Date().getTime());

		String playLoad = Base64.encode(json.toString().getBytes("UTF-8"));

		String sign = MD5.MD5Encode(playLoad + key, "UTF-8");

		return playLoad + "." + sign;
	}

	public static JSONObject paseToken(String token) throws Exception {
		JSONObject json = new JSONObject();
		if (Tools.strIsEmpty(token)) {
			json.put("success", false);
			return json;
		}

		String[] args = token.split("[.]");

		String playLoad = args[0];
		String sign = args[1];

		String sign2 = MD5.MD5Encode(playLoad + key, "UTF-8");

		if (!sign.equals(sign2)) {
			json.put("success", false);
			return json;
		}

		JSONObject playLoadJson = new JSONObject(new String(Base64.decode(playLoad), "UTF-8"));

		long lat = playLoadJson.getLong("lat");

		// 判断时间有效性
		if ((new Date().getTime() - lat) < (30 * 60 * 60 * 1000)) {
			json.put("uid", playLoadJson.getInt("uid"));
			json.put("success", true);
			return json;
		}

		json.put("success", false);
		return json;
	}

}
