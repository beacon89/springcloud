package cn.com.beacon.controller;

import org.springframework.stereotype.Component;

@Component
public class RegisterFallback implements RegisterIntegerFaces{

	@Override
	public String add(Integer a, Integer b) {
		return "feign-hystrix";
	}

}
