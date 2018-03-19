package cn.com.beacon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FindEurekaRegisteredService {

	@Autowired
	private DiscoveryClient client;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@RequestParam Integer a, @RequestParam Integer b) {
		ServiceInstance instance = client.getLocalServiceInstance();
		Integer r = a + b;
		return "From Service-B, Result is " + r + "\nPort:" + instance.getPort();
	}

}