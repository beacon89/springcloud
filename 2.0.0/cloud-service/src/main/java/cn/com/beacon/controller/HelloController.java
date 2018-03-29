package cn.com.beacon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	//http://localhost:9000/hello?name=beacon
	@RequestMapping(value = "/hello")
	public String index(@RequestParam String name) {
		return "hello " + name + "，this is first messge";
	}
}