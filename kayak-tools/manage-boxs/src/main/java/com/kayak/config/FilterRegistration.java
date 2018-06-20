package com.kayak.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kayak.base.filter.LocalRequestFilter;
import com.kayak.filter.LoginFilter;

/**
 * Created by daniel on 11/1/16.
 */
@Configuration
public class FilterRegistration {

	@Bean
	public FilterRegistrationBean<LocalRequestFilter> filterRegistrationBean() {
		FilterRegistrationBean<LocalRequestFilter> filterRegistrationBean = new FilterRegistrationBean<LocalRequestFilter>();
		LocalRequestFilter localRequestFilter = new LocalRequestFilter();
		filterRegistrationBean.setFilter(localRequestFilter);
		filterRegistrationBean.setOrder(1);
		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<LoginFilter> loginFilterRegistrationBean() {
		FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<LoginFilter>();
		LoginFilter loginFilter = new LoginFilter();
		filterRegistrationBean.setFilter(loginFilter);
		filterRegistrationBean.setOrder(2);
		return filterRegistrationBean;
	}

}
