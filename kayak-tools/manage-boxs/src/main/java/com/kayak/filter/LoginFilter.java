package com.kayak.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.kayak.base.system.SysUtil;
import com.kayak.util.JwtUtil;

public class LoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest _request = (HttpServletRequest) request;
		_request.setCharacterEncoding("UTF-8");

		String method = _request.getMethod();

		if (method.equalsIgnoreCase("OPTIONS")) {
			chain.doFilter(request, response);
			return;
		}

		if (!_request.getRequestURI().endsWith(".json")) {
			chain.doFilter(request, response);
			return;

		}

		if (_request.getRequestURI().contains("login.json") || _request.getRequestURI().contains("comn-upload.json")) {
			chain.doFilter(request, response);
			return;
		}
		;

		String token = _request.getHeader("authorization");

		try {
			JSONObject json = JwtUtil.paseToken(token);
			if (!json.getBoolean("success")) {
				Map<String, Object> msg = new HashMap<String, Object>();
				msg.put("success", false);
				msg.put("token_error", true);
				OutputStream out = response.getOutputStream();
				out.write((new JSONObject(msg).toString()).getBytes());
				out.close();
				return;
			}

			int userid = json.getInt("uid");

			Map<String, Object> userParams = new HashMap<String, Object>();
			userParams.put("userid", userid);

			_request.getSession().setAttribute(SysUtil.SYS_USER_PARAMS_SESSION_KEY, userParams);

			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> msg = new HashMap<String, Object>();
			msg.put("success", false);
			msg.put("token_error", true);
			OutputStream out = response.getOutputStream();
			out.write((new JSONObject(msg).toString()).getBytes());
			out.close();
			return;
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

}
