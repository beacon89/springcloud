package com.kayak.filter;


import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 验证token是否有效，无效则剔除登陆
 * @author beacon
 *
 */
public class KayakTokenFilter extends ZuulFilter{
	
	
	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		System.out.println(String.format("%s KayakTokenFilter request to %s", request.getMethod(),
				request.getRequestURL().toString()));
		ctx.set("isTokenSuccess", true);
		return null;
	}

	@Override
	public boolean shouldFilter() {
		 return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

}
