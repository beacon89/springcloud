package cn.com.beacon.controller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tools-service", fallback = RegisterFallback.class)
public interface RegisterIntegerFaces {

	@RequestMapping("/add")
	public String add(@RequestParam("a") Integer a, @RequestParam("b") Integer b);
	
}
